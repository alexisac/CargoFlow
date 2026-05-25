package com.example.backendcargoflow.controller;

import com.example.backendcargoflow.common.LogMessage;
import com.example.backendcargoflow.controller.common.models.GenericApplicationResponseDto;
import com.example.backendcargoflow.controller.tripAssignment.api.TripAssignmentsApi;
import com.example.backendcargoflow.controller.tripAssignment.models.AssignTripRequestDto;
import com.example.backendcargoflow.controller.tripAssignment.models.AvailableDriversResponseDto;
import com.example.backendcargoflow.controller.tripAssignment.models.AvailableVehiclesResponseDto;
import com.example.backendcargoflow.service.TripAssignmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TripAssignmentController implements TripAssignmentsApi {
    private final TripAssignmentService tripAssignmentService;

    @Override
    public AvailableDriversResponseDto getAvailableDriversForTrip(@PathVariable Long tripId) {
        log.info(String.format(LogMessage.GET_AVAILABLE_DRIVERS_FOR_TRIP, tripId));
        return tripAssignmentService.getAvailableDriversForTrip(tripId);
    }

    @Override
    public AvailableVehiclesResponseDto getAvailableVehiclesForTrip(@PathVariable Long tripId) {
        log.info(String.format(LogMessage.GET_AVAILABLE_VEHICLES_FOR_TRIP, tripId));
        return tripAssignmentService.getAvailableVehiclesForTrip(tripId);
    }

    @Override
    public GenericApplicationResponseDto assignTrip(@RequestBody AssignTripRequestDto assignTripRequestDto) {
        log.info(String.format(
                LogMessage.ASSIGN_TRIP,
                assignTripRequestDto.getTripId(),
                assignTripRequestDto.getDriverId(),
                assignTripRequestDto.getPrimaryVehicleId(),
                assignTripRequestDto.getTrailerVehicleId()
        ));

        return tripAssignmentService.assignTrip(assignTripRequestDto);
    }
}
