package com.example.backendcargoflow.controller;

import com.example.backendcargoflow.common.LogMessage;
import com.example.backendcargoflow.controller.tripAssignment.api.TripAssignmentsApi;
import com.example.backendcargoflow.controller.tripAssignment.models.AvailableDriversResponseDto;
import com.example.backendcargoflow.service.TripAssignmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
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
}
