package com.example.assignmentai.service.client;

import com.example.assignmentai.controller.cargoCoreInternal.models.CargoCoreAssignmentContextResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cargo-core-service")
public interface CargoCoreAssignmentAiClient {

    @GetMapping("/internal/assignment-ai/trips/{tripId}/context")
    CargoCoreAssignmentContextResponseDto getTripAssignmentContext(@PathVariable("tripId") Long tripId);
}