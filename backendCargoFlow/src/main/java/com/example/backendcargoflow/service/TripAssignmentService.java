package com.example.backendcargoflow.service;

import com.example.backendcargoflow.common.ErrorMessage;
import com.example.backendcargoflow.common.exceptions.BadRequestException;
import com.example.backendcargoflow.common.exceptions.NotFoundException;
import com.example.backendcargoflow.common.security.CurrentUserService;
import com.example.backendcargoflow.controller.common.models.GenericApplicationResponseDto;
import com.example.backendcargoflow.controller.tripAssignment.models.*;
import com.example.backendcargoflow.domain.trip.entity.Trip;
import com.example.backendcargoflow.domain.trip.entity.TripStatus;
import com.example.backendcargoflow.domain.trip.repository.TripRepository;
import com.example.backendcargoflow.domain.tripAssignment.entity.TripAssignment;
import com.example.backendcargoflow.domain.tripAssignment.mapper.TripAssignmentMapper;
import com.example.backendcargoflow.domain.tripAssignment.repository.TripAssignmentRepository;
import com.example.backendcargoflow.domain.user.entity.AvailableDriverProjection;
import com.example.backendcargoflow.domain.user.entity.UserRole;
import com.example.backendcargoflow.domain.user.repository.UserRepository;
import com.example.backendcargoflow.domain.vehicle.entity.AvailableVehicleProjection;
import com.example.backendcargoflow.domain.vehicle.entity.VehicleStatus;
import com.example.backendcargoflow.domain.vehicle.entity.VehicleType;
import com.example.backendcargoflow.domain.vehicle.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TripAssignmentService {
    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final TripAssignmentMapper tripAssignmentMapper;
    private final TripAssignmentRepository tripAssignmentRepository;
    private final CurrentUserService currentUserService;

    @PreAuthorize("hasAnyRole('DISPATCHER', 'MANAGER', 'ADMIN')")
    public AvailableDriversResponseDto getAvailableDriversForTrip(Long tripId) {
        Trip trip = getPlannedTrip(tripId);

        List<AvailableDriverProjection> availableDriverProjections = userRepository.findAvailableDriversForTrip(
                UserRole.DRIVER,
                getBlockingTripStatuses(),
                trip.getPickupInstant(),
                trip.getDeliveryInstant()
        );

        List<AvailableDriverDto> availableDrivers = tripAssignmentMapper.mapAvailableDriverProjectionsToAvailableDriverDtos(availableDriverProjections);

        AvailableDriversResponseDto response = new AvailableDriversResponseDto();
        response.setDrivers(availableDrivers);

        return response;
    }

    @PreAuthorize("hasAnyRole('DISPATCHER', 'MANAGER', 'ADMIN')")
    public AvailableVehiclesResponseDto getAvailableVehiclesForTrip(Long tripId) {
        Trip trip = getPlannedTrip(tripId);

        List<AvailableVehicleProjection> primaryVehicleProjections = vehicleRepository.findAvailableVehiclesForTrip(
                VehicleStatus.AVAILABLE,
                getPrimaryVehicleTypes(),
                getBlockingTripStatuses(),
                trip.getPickupInstant(),
                trip.getDeliveryInstant()
        );

        List<AvailableVehicleProjection> trailerProjections = vehicleRepository.findAvailableVehiclesForTrip(
                VehicleStatus.AVAILABLE,
                getTrailerVehicleTypes(),
                getBlockingTripStatuses(),
                trip.getPickupInstant(),
                trip.getDeliveryInstant()
        );

        List<AvailableVehicleDto> primaryVehicles =
                tripAssignmentMapper.mapAvailableVehicleProjectionsToAvailableVehicleDtos(
                        primaryVehicleProjections
                );

        List<AvailableVehicleDto> trailers =
                tripAssignmentMapper.mapAvailableVehicleProjectionsToAvailableVehicleDtos(
                        trailerProjections
                );

        AvailableVehiclesResponseDto response = new AvailableVehiclesResponseDto();
        response.setPrimaryVehicles(primaryVehicles);
        response.setTrailers(trailers);

        return response;
    }

    @Transactional
    @PreAuthorize("hasAnyRole('DISPATCHER', 'MANAGER', 'ADMIN')")
    public GenericApplicationResponseDto assignTrip(AssignTripRequestDto assignTripRequestDto) {
        Trip trip = getPlannedTrip(assignTripRequestDto.getTripId());

        Long currentUserId = currentUserService.getCurrentUserId();
        TripAssignment tripAssignment = tripAssignmentMapper.mapAssignTripRequestDtoToTripAssignment(assignTripRequestDto);
        tripAssignment.setAssignedByUserId(currentUserId);
        tripAssignmentRepository.save(tripAssignment);

        trip.setTripStatus(TripStatus.ASSIGNED);
        tripRepository.save(trip);

        return GenericApplicationResponseFactory.success(
                "201 - TRIP_ASSIGNED",
                "Trip was assigned successfully"
        );
    }

    private Trip getPlannedTrip(Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.TRIP_NOT_FOUND));

        if (trip.getTripStatus() != TripStatus.PLANNED) {
            throw new BadRequestException(ErrorMessage.TRIP_IS_NOT_PLANNED);
        }

        return trip;
    }

    private List<TripStatus> getBlockingTripStatuses() {
        return List.of(
                TripStatus.ASSIGNED,
                TripStatus.IN_PROGRESS
        );
    }

    private List<VehicleType> getPrimaryVehicleTypes() {
        return List.of(
                VehicleType.VAN,
                VehicleType.BOX_TRUCK,
                VehicleType.REFRIGERATED_TRUCK,
                VehicleType.TANKER_TRUCK,
                VehicleType.TRACTOR_UNIT
        );
    }

    private List<VehicleType> getTrailerVehicleTypes() {
        return List.of(
                VehicleType.SEMI_TRAILER,
                VehicleType.REFRIGERATED_TRAILER,
                VehicleType.TANKER_TRAILER
        );
    }
}
