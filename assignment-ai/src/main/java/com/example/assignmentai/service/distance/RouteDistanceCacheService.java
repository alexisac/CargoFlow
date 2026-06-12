package com.example.assignmentai.service.distance;

import com.example.assignmentai.service.google.GoogleRoutesClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class RouteDistanceCacheService {
    private final GoogleRoutesClient googleRoutesClient;

    private final Map<String, GoogleRoutesClient.RoadRoute> cache = new ConcurrentHashMap<>();

    public GoogleRoutesClient.RoadRoute getRoadRoute(
            Double originLatitude,
            Double originLongitude,
            Double destinationLatitude,
            Double destinationLongitude
    ) {
        String cacheKey = buildCacheKey(
                originLatitude,
                originLongitude,
                destinationLatitude,
                destinationLongitude
        );

        GoogleRoutesClient.RoadRoute cachedRoute = cache.get(cacheKey);

        if (cachedRoute != null) {
            log.info("Google Routes cache HIT. routeKey={}, distanceKm={}, duration={}",
                    cacheKey,
                    String.format("%.2f", cachedRoute.distanceKm()),
                    cachedRoute.duration()
            );

            return cachedRoute;
        }

        log.info("Google Routes cache MISS. Calling Google Routes API. routeKey={}", cacheKey);

        GoogleRoutesClient.RoadRoute route = googleRoutesClient.calculateDrivingRoute(
                originLatitude,
                originLongitude,
                destinationLatitude,
                destinationLongitude
        );

        log.info("Google Routes API response. routeKey={}, distanceKm={}, duration={}",
                cacheKey,
                String.format("%.2f", route.distanceKm()),
                route.duration()
        );

        cache.put(cacheKey, route);

        return route;
    }

    private String buildCacheKey(
            Double originLatitude,
            Double originLongitude,
            Double destinationLatitude,
            Double destinationLongitude
    ) {
        return round(originLatitude) + "," +
                round(originLongitude) + "->" +
                round(destinationLatitude) + "," +
                round(destinationLongitude);
    }

    private double round(Double value) {
        return Math.round(value * 1000.0) / 1000.0;
    }
}