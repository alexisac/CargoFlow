package com.example.assignmentai.model.location;

import java.util.List;

public record LatestDriverLocationsResponse(
        List<DriverLocationData> driverLocations
) { }
