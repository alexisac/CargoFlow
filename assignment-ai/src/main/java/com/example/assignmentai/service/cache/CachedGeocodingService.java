package com.example.assignmentai.service.cache;

import com.example.assignmentai.model.assignment.AddressData;
import com.example.assignmentai.model.geocoding.entity.GeocodedAddress;
import com.example.assignmentai.model.geocoding.repository.GeocodedAddressRepository;
import com.example.assignmentai.service.google.GoogleGeocodingClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.HexFormat;

@Slf4j
@Service
@RequiredArgsConstructor
public class CachedGeocodingService {
    private final GoogleGeocodingClient googleGeocodingClient;
    private final GeocodedAddressRepository geocodedAddressRepository;

    public GoogleGeocodingClient.Coordinates geocodeAddress(AddressData addressData) {
        String fullAddress = addressData.toFullAddress();
        String normalizedAddress = normalizeAddress(fullAddress);
        String addressHash = sha256(normalizedAddress);

        return geocodedAddressRepository.findByAddressHash(addressHash)
                .map(cachedAddress -> {
                    log.info("Geocoding DB cache HIT. address='{}', latitude={}, longitude={}\n",
                            cachedAddress.getFullAddress(),
                            cachedAddress.getLatitude(),
                            cachedAddress.getLongitude()
                    );

                    return new GoogleGeocodingClient.Coordinates(cachedAddress.getLatitude(), cachedAddress.getLongitude());
                })
                .orElseGet(() -> {
                    log.info("Geocoding DB cache MISS. Calling Google Geocoding API. address='{}'\n", fullAddress);

                    GoogleGeocodingClient.Coordinates coordinates = googleGeocodingClient.geocodeAddress(addressData);

                    GeocodedAddress geocodedAddress = new GeocodedAddress();
                    geocodedAddress.setAddressHash(addressHash);
                    geocodedAddress.setFullAddress(fullAddress);
                    geocodedAddress.setLatitude(coordinates.latitude());
                    geocodedAddress.setLongitude(coordinates.longitude());
                    geocodedAddress.setCreatedAt(LocalDateTime.now());
                    geocodedAddress.setUpdatedAt(LocalDateTime.now());

                    geocodedAddressRepository.save(geocodedAddress);

                    log.info("Geocoding result saved in DB. address='{}', latitude={}, longitude={}\n",
                            fullAddress,
                            coordinates.latitude(),
                            coordinates.longitude()
                    );

                    return coordinates;
                });
    }

    private String normalizeAddress(String fullAddress) {
        return fullAddress == null ? "" : fullAddress.trim().toLowerCase().replaceAll("\\s+", " ");
    }

    private String sha256(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (Exception exception) {
            throw new IllegalStateException("Could not hash address", exception);
        }
    }
}