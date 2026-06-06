package com.example.locationservice.simulator;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "location-simulator")
public class LocationSimulatorProperties {
    private Boolean enabled = false;
    private Integer initialDelaySeconds = 30;
    private Integer intervalSeconds = 5;
    private Integer stepsBetweenPoints = 10;
    private List<SimulatedDriver> drivers = new ArrayList<>();

    @Getter
    @Setter
    public static class SimulatedDriver {
        private Long driverId;
        private String name;
        private List<LocationPoint> route = new ArrayList<>();
    }

    @Getter
    @Setter
    public static class LocationPoint {
        private Double latitude;
        private Double longitude;
    }
}