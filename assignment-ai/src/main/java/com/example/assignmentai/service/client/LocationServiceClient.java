package com.example.assignmentai.service.client;

import com.example.assignmentai.model.location.LatestDriverLocationsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "location-service")
public interface LocationServiceClient {

    @GetMapping("/locations/drivers/latest")
    LatestDriverLocationsResponse getLatestDriverLocations();
}