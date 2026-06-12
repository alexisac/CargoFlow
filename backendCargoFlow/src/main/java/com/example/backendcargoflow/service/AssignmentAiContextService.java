package com.example.backendcargoflow.service;

import com.example.backendcargoflow.common.ErrorMessage;
import com.example.backendcargoflow.common.exceptions.NotFoundException;
import com.example.backendcargoflow.controller.assignmentAiInternal.models.AssignmentAiDriverDataDto;
import com.example.backendcargoflow.controller.assignmentAiInternal.models.AssignmentAiTripContextResponseDto;
import com.example.backendcargoflow.domain.assignmentAi.AssignmentAiContextMapper;
import com.example.backendcargoflow.domain.trip.entity.DriverCompletedTripsCountProjection;
import com.example.backendcargoflow.domain.trip.entity.Trip;
import com.example.backendcargoflow.domain.trip.entity.TripStatus;
import com.example.backendcargoflow.domain.trip.repository.TripRepository;
import com.example.backendcargoflow.domain.tripAssignment.entity.TripAssignment;
import com.example.backendcargoflow.domain.tripAssignment.repository.TripAssignmentRepository;
import com.example.backendcargoflow.domain.user.entity.AvailableDriverProjection;
import com.example.backendcargoflow.domain.user.entity.UserRole;
import com.example.backendcargoflow.domain.user.repository.UserRepository;
import com.example.backendcargoflow.domain.vehicle.entity.AssignmentAiAvailableVehicleProjection;
import com.example.backendcargoflow.domain.vehicle.entity.VehicleStatus;
import com.example.backendcargoflow.domain.vehicle.entity.VehicleType;
import com.example.backendcargoflow.domain.vehicle.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssignmentAiContextService {
    private static final int MAX_INTERNAL_PAGE_SIZE = 200;

    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final TripAssignmentRepository tripAssignmentRepository;
    private final AssignmentAiContextMapper assignmentAiContextMapper;

    @PreAuthorize("hasAnyRole('DISPATCHER', 'MANAGER', 'ADMIN')")
    public AssignmentAiTripContextResponseDto getTripAssignmentContext(Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.TRIP_NOT_FOUND));

        Page<AvailableDriverProjection> availableDriverPage =
                userRepository.findAvailableDriversForTrip(
                        UserRole.DRIVER,
                        getBlockingTripStatuses(),
                        trip.getPickupInstant(),
                        trip.getDeliveryInstant(),
                        PageRequest.of(0, MAX_INTERNAL_PAGE_SIZE)
                );

        List<AvailableDriverProjection> availableDrivers =
                availableDriverPage.getContent();

        List<Long> driverIds = availableDrivers.stream()
                .map(AvailableDriverProjection::getId)
                .toList();

        Map<Long, TripAssignment> latestAssignmentByDriverId =
                getLatestAssignmentByDriverId(driverIds);

        Map<Long, Integer> completedTripsByDriverId =
                getCompletedTripsByDriverId(driverIds);

        List<AssignmentAiDriverDataDto> drivers = availableDrivers.stream()
                .map(driver -> {
                    TripAssignment latestAssignment =
                            latestAssignmentByDriverId.get(driver.getId());

                    return new AssignmentAiDriverDataDto()
                            .driverId(driver.getId())
                            .driverAvailable(true)
                            .driverCompletedTripsLast30Days(
                                    completedTripsByDriverId.getOrDefault(driver.getId(), 0)
                            )
                            .lastPrimaryVehicleId(
                                    latestAssignment == null ? null : latestAssignment.getPrimaryVehicleId()
                            )
                            .lastTrailerId(
                                    latestAssignment == null ? null : latestAssignment.getTrailerVehicleId()
                            );
                })
                .toList();

        List<AssignmentAiAvailableVehicleProjection> primaryVehicles =
                vehicleRepository.findAssignmentAiAvailableVehiclesForTrip(
                        VehicleStatus.AVAILABLE,
                        getPrimaryVehicleTypes(),
                        getBlockingTripStatuses(),
                        trip.getPickupInstant(),
                        trip.getDeliveryInstant()
                );

        List<AssignmentAiAvailableVehicleProjection> trailers =
                vehicleRepository.findAssignmentAiAvailableVehiclesForTrip(
                        VehicleStatus.AVAILABLE,
                        getTrailerVehicleTypes(),
                        getBlockingTripStatuses(),
                        trip.getPickupInstant(),
                        trip.getDeliveryInstant()
                );

        return new AssignmentAiTripContextResponseDto()
                .trip(assignmentAiContextMapper.mapTripToAssignmentAiTripDataDto(trip))
                .drivers(drivers)
                .primaryVehicles(primaryVehicles.stream()
                        .map(assignmentAiContextMapper::mapAssignmentAiAvailableVehicleProjectionToAssignmentAiVehicleDataDto)
                        .toList())
                .trailers(trailers.stream()
                        .map(assignmentAiContextMapper::mapAssignmentAiAvailableVehicleProjectionToAssignmentAiVehicleDataDto)
                        .toList());
    }

    private Map<Long, TripAssignment> getLatestAssignmentByDriverId(List<Long> driverIds) {
        if (driverIds.isEmpty()) {
            return Map.of();
        }

        List<TripAssignment> assignments =
                tripAssignmentRepository.findByDriverIdInOrderByAssignedDateDesc(driverIds);

        return assignments.stream()
                .collect(Collectors.toMap(
                        TripAssignment::getDriverId,
                        Function.identity(),
                        (first, ignored) -> first,
                        LinkedHashMap::new
                ));
    }

    private Map<Long, Integer> getCompletedTripsByDriverId(List<Long> driverIds) {
        if (driverIds.isEmpty()) {
            return Map.of();
        }

        Instant fromInstant = Instant.now().minus(30, java.time.temporal.ChronoUnit.DAYS);

        List<DriverCompletedTripsCountProjection> projections =
                tripRepository.countCompletedTripsForDriversSince(
                        driverIds,
                        TripStatus.COMPLETED,
                        fromInstant
                );

        return projections.stream()
                .collect(Collectors.toMap(
                        DriverCompletedTripsCountProjection::getDriverId,
                        DriverCompletedTripsCountProjection::getCompletedTripsCount
                ));
    }

    private List<VehicleType> getPrimaryVehicleTypes() {
        return List.of(
                VehicleType.TRACTOR_UNIT,
                VehicleType.VAN,
                VehicleType.BOX_TRUCK,
                VehicleType.REFRIGERATED_TRUCK,
                VehicleType.TANKER_TRUCK
        );
    }

    private List<VehicleType> getTrailerVehicleTypes() {
        return List.of(
                VehicleType.SEMI_TRAILER,
                VehicleType.REFRIGERATED_TRAILER,
                VehicleType.TANKER_TRAILER
        );
    }

    private List<TripStatus> getBlockingTripStatuses() {
        return List.of(
                TripStatus.ASSIGNED,
                TripStatus.IN_PROGRESS
        );
    }
}