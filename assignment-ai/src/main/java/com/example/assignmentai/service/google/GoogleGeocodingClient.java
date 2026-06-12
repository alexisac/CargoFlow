package com.example.assignmentai.service.google;

import com.example.assignmentai.model.assignment.AddressData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GoogleGeocodingClient {
    private final WebClient.Builder webClientBuilder;

    @Value("${google.maps.api-key}")
    private String apiKey;

    public Coordinates geocodeAddress(AddressData addressData) {
        String fullAddress = addressData.toFullAddress();

        GoogleGeocodingResponse response = webClientBuilder.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("maps.googleapis.com")
                        .path("/maps/api/geocode/json")
                        .queryParam("address", fullAddress)
                        .queryParam("key", apiKey)
                        .build()
                )
                .retrieve()
                .bodyToMono(GoogleGeocodingResponse.class)
                .block();

        if (response == null ||
                response.results() == null ||
                response.results().isEmpty()) {
            throw new IllegalStateException("Could not geocode address: " + fullAddress);
        }

        Location location = response.results()
                .getFirst()
                .geometry()
                .location();

        return new Coordinates(location.lat(), location.lng());
    }

    public record Coordinates(Double latitude, Double longitude) {}

    public record GoogleGeocodingResponse(String status, List<Result> results) {}

    public record Result(Geometry geometry) {}

    public record Geometry(Location location) {}

    public record Location(Double lat, Double lng) {}
}