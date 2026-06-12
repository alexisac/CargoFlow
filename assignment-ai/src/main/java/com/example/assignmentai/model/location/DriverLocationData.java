package com.example.assignmentai.model.location;

import java.time.OffsetDateTime;

public record DriverLocationData(
        Long driverId,
        Double latitude,
        Double longitude,
        OffsetDateTime updatedAt
) { }
