package com.example.assignmentai.service.google;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GoogleRoutesClient {
    private final WebClient.Builder webClientBuilder;

    @Value("${google.maps.api-key}")
    private String apiKey;

    public RoadRoute calculateDrivingRoute(
            Double originLatitude,
            Double originLongitude,
            Double destinationLatitude,
            Double destinationLongitude
    ) {
        GoogleRoutesRequest request = new GoogleRoutesRequest(
                new Waypoint(new Location(new LatLng(originLatitude, originLongitude))),
                new Waypoint(new Location(new LatLng(destinationLatitude, destinationLongitude))),
                "DRIVE",
                "TRAFFIC_UNAWARE",
                "METRIC"
        );

        GoogleRoutesResponse response = webClientBuilder.build()
                .post()
                .uri("https://routes.googleapis.com/directions/v2:computeRoutes")
                .header("Content-Type", "application/json")
                .header("X-Goog-Api-Key", apiKey)
                .header("X-Goog-FieldMask", "routes.distanceMeters,routes.duration")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(GoogleRoutesResponse.class)
                .block();

        if (response == null ||
                response.routes() == null ||
                response.routes().isEmpty()) {
            throw new IllegalStateException("Could not calculate route distance.");
        }

        Route route = response.routes().getFirst();

        return new RoadRoute(route.distanceMeters() / 1000.0, route.duration());
    }

    public record RoadRoute(Double distanceKm, String duration) {}

    public record GoogleRoutesRequest(
            Waypoint origin,
            Waypoint destination,
            String travelMode,
            String routingPreference,
            String units
    ) {}

    public record Waypoint(Location location) {}

    public record Location(LatLng latLng) {}

    public record LatLng(Double latitude, Double longitude) {}

    public record GoogleRoutesResponse(List<Route> routes) {}

    public record Route(Integer distanceMeters, String duration) {}
}