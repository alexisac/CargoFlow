package com.example.backendcargoflow.service;

import com.example.backendcargoflow.common.security.CurrentUserService;
import com.example.backendcargoflow.controller.common.models.GenericApplicationResponseDto;
import com.example.backendcargoflow.controller.trip.models.AddNewTripRequestDto;
import com.example.backendcargoflow.domain.trip.entity.Trip;
import com.example.backendcargoflow.domain.trip.entity.TripStatus;
import com.example.backendcargoflow.domain.trip.mapper.TripMapper;
import com.example.backendcargoflow.domain.trip.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TripService {
    private final TripRepository tripRepository;
    private final TripMapper tripMapper;
    private final CurrentUserService currentUserService;

    @PreAuthorize("hasAnyRole('DISPATCHER', 'MANAGER', 'ADMIN')")
    public GenericApplicationResponseDto addNewTrip(AddNewTripRequestDto addNewTripRequestDto) {
        Long currentUserId = currentUserService.getCurrentUserId();
        Trip trip = tripMapper.mapAddNewTripRequestDtoToTrip(addNewTripRequestDto);
        trip.setTripStatus(TripStatus.PLANNED);
        trip.setCreatedByUserId(currentUserId);
        tripRepository.save(trip);
        return buildSuccessResponse();
    }

    private GenericApplicationResponseDto buildSuccessResponse(){
        GenericApplicationResponseDto response = new GenericApplicationResponseDto();
        response.setSuccess(true);
        response.setCode("201 - TRIP_CREATED");
        response.setMessage("Trip was created successfully");
        return response;
    }
}
