package com.example.backendcargoflow.service;

import com.example.backendcargoflow.common.ErrorMessage;
import com.example.backendcargoflow.common.exceptions.BadRequestException;
import com.example.backendcargoflow.common.exceptions.NotFoundException;
import com.example.backendcargoflow.controller.tripAssignment.models.AvailableDriverDto;
import com.example.backendcargoflow.controller.tripAssignment.models.AvailableDriversResponseDto;
import com.example.backendcargoflow.domain.trip.entity.Trip;
import com.example.backendcargoflow.domain.trip.entity.TripStatus;
import com.example.backendcargoflow.domain.trip.repository.TripRepository;
import com.example.backendcargoflow.domain.tripAssignment.mapper.TripAssignmentMapper;
import com.example.backendcargoflow.domain.user.entity.AvailableDriverProjection;
import com.example.backendcargoflow.domain.user.entity.UserRole;
import com.example.backendcargoflow.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TripAssignmentService {
    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final TripAssignmentMapper tripAssignmentMapper;

    @PreAuthorize("hasAnyRole('DISPATCHER', 'MANAGER', 'ADMIN')")
    public AvailableDriversResponseDto getAvailableDriversForTrip(Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.TRIP_NOT_FOUND));

        if (trip.getTripStatus() != TripStatus.PLANNED) {
            throw new BadRequestException(ErrorMessage.TRIP_IS_NOT_PLANNED);
        }

        List<AvailableDriverProjection> availableDriverProjections = userRepository.findAvailableDriversForTrip(
                UserRole.DRIVER,
                List.of(
                        TripStatus.ASSIGNED,
                        TripStatus.IN_PROGRESS
                ),
                trip.getPickupInstant(),
                trip.getDeliveryInstant()
        );

        List<AvailableDriverDto> availableDrivers = tripAssignmentMapper.mapAvailableDriverProjectionsToAvailableDriverDtos(availableDriverProjections);

        AvailableDriversResponseDto response = new AvailableDriversResponseDto();
        response.setDrivers(availableDrivers);

        return response;
    }
}
