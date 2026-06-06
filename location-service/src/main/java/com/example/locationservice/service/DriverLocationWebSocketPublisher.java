package com.example.locationservice.service;

import com.example.locationservice.controller.location.models.DriverLocationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DriverLocationWebSocketPublisher {
    private final SimpMessagingTemplate messagingTemplate;

    @Value("${websocket.topics.driver-locations}")
    private String driverLocationsTopic;

    public void publishDriverLocation(DriverLocationDto driverLocationDto) {
        messagingTemplate.convertAndSend(
                driverLocationsTopic,
                driverLocationDto
        );
    }
}