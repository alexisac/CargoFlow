package com.example.locationservice.simulator;

import com.example.locationservice.controller.location.models.DriverLocationDto;
import com.example.locationservice.domain.entity.DriverLocation;
import com.example.locationservice.domain.repository.DriverLocationRepository;
import com.example.locationservice.service.DriverLocationWebSocketPublisher;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class DriverLocationSimulator implements ApplicationRunner {
    private final LocationSimulatorProperties locationSimulatorProperties;
    private final DriverLocationRepository driverLocationRepository;
    private final DriverLocationWebSocketPublisher driverLocationWebSocketPublisher;

    private ScheduledExecutorService executorService;

    @Override
    public void run(ApplicationArguments args) {
        if (!Boolean.TRUE.equals(locationSimulatorProperties.getEnabled())) {
            log.info("Driver location simulator is disabled.");
            return;
        }

        if (locationSimulatorProperties.getDrivers().isEmpty()) {
            log.warn("Driver location simulator is enabled, but no drivers are configured.");
            return;
        }

        executorService = Executors.newScheduledThreadPool(
                locationSimulatorProperties.getDrivers().size()
        );

        for (LocationSimulatorProperties.SimulatedDriver driver : locationSimulatorProperties.getDrivers()) {
            startDriverSimulation(driver);
        }

        log.info("Driver location simulator started with {} drivers.",
                locationSimulatorProperties.getDrivers().size()
        );
    }

    private void startDriverSimulation(LocationSimulatorProperties.SimulatedDriver driver) {
        List<LocationSimulatorProperties.LocationPoint> interpolatedRoute = interpolateRoute(
                driver.getRoute(),
                locationSimulatorProperties.getStepsBetweenPoints()
        );

        if (interpolatedRoute.isEmpty()) {
            log.warn("Driver {} has no route. Simulation skipped.", driver.getDriverId());
            return;
        }

        SimulatedDriverTask task = new SimulatedDriverTask(
                driver.getDriverId(),
                driver.getName(),
                interpolatedRoute
        );

        executorService.scheduleAtFixedRate(
                task,
                locationSimulatorProperties.getInitialDelaySeconds(),
                locationSimulatorProperties.getIntervalSeconds(),
                TimeUnit.SECONDS
        );
    }

    private List<LocationSimulatorProperties.LocationPoint> interpolateRoute(
            List<LocationSimulatorProperties.LocationPoint> route,
            Integer stepsBetweenPoints
    ) {
        if (route == null || route.isEmpty()) {
            return List.of();
        }

        if (route.size() < 2) {
            return route;
        }

        int steps = stepsBetweenPoints == null || stepsBetweenPoints <= 0 ? 10 : stepsBetweenPoints;

        List<LocationSimulatorProperties.LocationPoint> interpolatedRoute = new ArrayList<>();

        for (int index = 0; index < route.size() - 1; index++) {
            LocationSimulatorProperties.LocationPoint start = route.get(index);
            LocationSimulatorProperties.LocationPoint end = route.get(index + 1);

            for (int step = 0; step < steps; step++) {
                double ratio = (double) step / steps;

                double latitude = start.getLatitude() + (end.getLatitude() - start.getLatitude()) * ratio;
                double longitude = start.getLongitude() + (end.getLongitude() - start.getLongitude()) * ratio;

                LocationSimulatorProperties.LocationPoint point = new LocationSimulatorProperties.LocationPoint();

                point.setLatitude(latitude);
                point.setLongitude(longitude);

                interpolatedRoute.add(point);
            }
        }

        interpolatedRoute.add(route.get(route.size() - 1));

        return interpolatedRoute;
    }

    @PreDestroy
    public void stopSimulator() {
        if (executorService != null) {
            executorService.shutdownNow();
            log.info("Driver location simulator stopped.");
        }
    }

    private class SimulatedDriverTask implements Runnable {
        private final Long driverId;
        private final String driverName;
        private final List<LocationSimulatorProperties.LocationPoint> route;

        private int currentIndex = 0;

        private SimulatedDriverTask(
                Long driverId,
                String driverName,
                List<LocationSimulatorProperties.LocationPoint> route
        ) {
            this.driverId = driverId;
            this.driverName = driverName;
            this.route = route;
        }

        @Override
        public void run() {
            try {
                LocationSimulatorProperties.LocationPoint point = route.get(currentIndex);

                DriverLocation savedLocation = saveDriverLocation(
                        driverId,
                        point.getLatitude(),
                        point.getLongitude()
                );

                publishDriverLocation(savedLocation);

                log.info(
                        "Simulated location sent. driverId={}, name={}, latitude={}, longitude={}",
                        driverId,
                        driverName,
                        point.getLatitude(),
                        point.getLongitude()
                );

                currentIndex = (currentIndex + 1) % route.size();
            } catch (Exception ex) {
                log.error("Error while simulating driver location for driverId={}", driverId, ex);
            }
        }
    }

    private DriverLocation saveDriverLocation(
            Long driverId,
            Double latitude,
            Double longitude
    ) {
        DriverLocation driverLocation = driverLocationRepository.findByDriverId(driverId)
                .orElseGet(() -> {
                    DriverLocation newLocation = new DriverLocation();
                    newLocation.setDriverId(driverId);
                    return newLocation;
                });

        driverLocation.setLatitude(latitude);
        driverLocation.setLongitude(longitude);
        driverLocation.setUpdatedAt(Instant.now());

        return driverLocationRepository.save(driverLocation);
    }

    private void publishDriverLocation(
            DriverLocation driverLocation
    ) {
        DriverLocationDto driverLocationDto = new DriverLocationDto();

        driverLocationDto.setDriverId(driverLocation.getDriverId());
        driverLocationDto.setLatitude(driverLocation.getLatitude());
        driverLocationDto.setLongitude(driverLocation.getLongitude());
        driverLocationDto.setUpdatedAt(driverLocation.getUpdatedAt().atOffset(ZoneOffset.UTC));

        driverLocationWebSocketPublisher.publishDriverLocation(driverLocationDto);
    }
}