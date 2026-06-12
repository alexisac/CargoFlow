package com.example.backendcargoflow.controller;

import com.example.backendcargoflow.controller.assignmentAiInternal.api.AssignmentAiInternalApi;
import com.example.backendcargoflow.controller.assignmentAiInternal.models.AssignmentAiTripContextResponseDto;
import com.example.backendcargoflow.service.AssignmentAiContextService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AssignmentAiInternalController implements AssignmentAiInternalApi {
    private final AssignmentAiContextService assignmentAiContextService;

    @Override
    public AssignmentAiTripContextResponseDto getTripAssignmentContext(Long tripId) {
        log.info("Getting assignment AI context for tripId={}", tripId);
        return assignmentAiContextService.getTripAssignmentContext(tripId);
    }
}