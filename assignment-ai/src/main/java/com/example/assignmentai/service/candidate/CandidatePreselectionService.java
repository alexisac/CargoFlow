package com.example.assignmentai.service.candidate;

import com.example.assignmentai.model.assignment.AddressData;
import com.example.assignmentai.model.assignment.AssignmentCandidate;
import com.example.assignmentai.model.assignment.DriverDistanceEstimate;
import com.example.assignmentai.model.assignment.EnrichedAssignmentCandidate;
import com.example.assignmentai.service.distance.FallbackDistanceCalculator;
import com.example.assignmentai.service.distance.RouteDistanceCacheService;
import com.example.assignmentai.service.cache.CachedGeocodingService;
import com.example.assignmentai.service.google.GoogleGeocodingClient;
import com.example.assignmentai.service.google.GoogleRoutesClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CandidatePreselectionService {
    @Value("${assignment-ai.maps.max-drivers-for-routes-api:5}")
    private Integer maxDriversForRoutesApi;
    private final CachedGeocodingService cachedGeocodingService;
    private final RouteDistanceCacheService routeDistanceCacheService;
    private final FallbackDistanceCalculator fallbackDistanceCalculator;

    public List<EnrichedAssignmentCandidate> preselectAndEnrichCandidates(List<AssignmentCandidate> candidates) {
        if (candidates == null || candidates.isEmpty()) {
            return List.of();
        }

        AddressData pickupAddress = candidates.getFirst().pickupAddress();

        if (pickupAddress == null) {
            return List.of();
        }

        GoogleGeocodingClient.Coordinates pickupCoordinates = cachedGeocodingService.geocodeAddress(pickupAddress);

        log.info("Pickup address geocoded. address='{}', latitude={}, longitude={}",
                pickupAddress.toFullAddress(),
                pickupCoordinates.latitude(),
                pickupCoordinates.longitude()
        );

        List<DriverDistanceEstimate> closestDrivers = findClosestDriversByAirDistance(candidates, pickupCoordinates);

        log.info("Selected top {} drivers for Google Routes API from {} candidates.",
                closestDrivers.size(),
                candidates.size()
        );

        closestDrivers.forEach(driver ->
                log.info("Driver preselected by air distance. driverId={}, driverLocation=({}, {}), pickupAddress='{}', airDistanceKm={}",
                        driver.driverId(),
                        driver.driverLatitude(),
                        driver.driverLongitude(),
                        pickupAddress.toFullAddress(),
                        String.format("%.2f", driver.airDistanceToPickupKm())
                )
        );

        Map<Long, Double> roadDistanceByDriverId = calculateRoadDistancesForTopDrivers(
                closestDrivers,
                pickupCoordinates,
                pickupAddress
        );

        Set<Long> selectedDriverIds = roadDistanceByDriverId.keySet();

        return candidates.stream()
                .filter(candidate -> selectedDriverIds.contains(candidate.driverId()))
                .map(candidate -> new EnrichedAssignmentCandidate(
                        candidate,
                        roadDistanceByDriverId.get(candidate.driverId())
                ))
                .toList();
    }

    private List<DriverDistanceEstimate> findClosestDriversByAirDistance(
            List<AssignmentCandidate> candidates,
            GoogleGeocodingClient.Coordinates pickupCoordinates
    ) {
        Map<Long, AssignmentCandidate> firstCandidateByDriverId = candidates.stream()
                .filter(candidate -> candidate.driverId() != null)
                .filter(candidate -> candidate.driverLatitude() != null)
                .filter(candidate -> candidate.driverLongitude() != null)
                .collect(Collectors.toMap(
                        AssignmentCandidate::driverId,
                        candidate -> candidate,
                        (first, ignored) -> first,
                        LinkedHashMap::new
                ));

        return firstCandidateByDriverId.values()
                .stream()
                .map(candidate -> new DriverDistanceEstimate(
                        candidate.driverId(),
                        candidate.driverLatitude(),
                        candidate.driverLongitude(),
                        fallbackDistanceCalculator.calculateAirDistanceKm(
                                candidate.driverLatitude(),
                                candidate.driverLongitude(),
                                pickupCoordinates.latitude(),
                                pickupCoordinates.longitude()
                        )
                ))
                .sorted(Comparator.comparing(DriverDistanceEstimate::airDistanceToPickupKm))
                .limit(resolveMaxDriversForRoutesApi())
                .toList();
    }

    private Map<Long, Double> calculateRoadDistancesForTopDrivers(
            List<DriverDistanceEstimate> closestDrivers,
            GoogleGeocodingClient.Coordinates pickupCoordinates,
            AddressData pickupAddress
    ) {
        Map<Long, Double> roadDistanceByDriverId = new LinkedHashMap<>();

        for (DriverDistanceEstimate driverDistanceEstimate : closestDrivers) {
            GoogleRoutesClient.RoadRoute route =
                    routeDistanceCacheService.getRoadRoute(
                            driverDistanceEstimate.driverLatitude(),
                            driverDistanceEstimate.driverLongitude(),
                            pickupCoordinates.latitude(),
                            pickupCoordinates.longitude()
                    );

            log.info("Road distance calculated. driverId={}, fromDriverLocation='{},{}', toPickupAddress='{}', toPickupCoordinates='{},{}', roadDistanceKm={}, duration={}",
                    driverDistanceEstimate.driverId(),
                    driverDistanceEstimate.driverLatitude(),
                    driverDistanceEstimate.driverLongitude(),
                    pickupAddress.toFullAddress(),
                    pickupCoordinates.latitude(),
                    pickupCoordinates.longitude(),
                    String.format("%.2f", route.distanceKm()),
                    route.duration()
            );

            roadDistanceByDriverId.put(driverDistanceEstimate.driverId(), route.distanceKm());
        }

        return roadDistanceByDriverId;
    }

    private int resolveMaxDriversForRoutesApi() {
        if (maxDriversForRoutesApi == null || maxDriversForRoutesApi <= 0) {
            return 5;
        }

        return maxDriversForRoutesApi;
    }
}