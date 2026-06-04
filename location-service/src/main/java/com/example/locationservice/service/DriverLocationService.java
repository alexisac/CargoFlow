package com.example.locationservice.service;

import com.example.locationservice.controller.common.models.GenericApplicationResponseDto;
import com.example.locationservice.controller.location.models.UpdateMyLocationRequestDto;
import com.example.locationservice.domain.entity.DriverLocation;
import com.example.locationservice.domain.repository.DriverLocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class DriverLocationService {
    private final DriverLocationRepository driverLocationRepository;

    @Transactional
    @PreAuthorize("hasRole('DRIVER')")
    public GenericApplicationResponseDto updateMyLocation(
            Long driverId,
            UpdateMyLocationRequestDto request
    ) {
        Instant updatedAt = request.getUpdatedAt().toInstant();

        return driverLocationRepository.findByDriverId(driverId)
                .map(existingLocation -> updateExistingLocationIfNewer(
                        existingLocation,
                        request.getLatitude(),
                        request.getLongitude(),
                        updatedAt
                ))
                .orElseGet(() -> createNewLocation(
                        driverId,
                        request.getLatitude(),
                        request.getLongitude(),
                        updatedAt
                ));
    }

    private GenericApplicationResponseDto updateExistingLocationIfNewer(
            DriverLocation existingLocation,
            Double latitude,
            Double longitude,
            Instant updatedAt
    ) {
        if (!updatedAt.isAfter(existingLocation.getUpdatedAt())) {
            return GenericApplicationResponseFactory.success(
                    "200 - LOCATION_IGNORED",
                    "Location update was ignored because it is older than the current stored location."
            );
        }

        existingLocation.setLatitude(latitude);
        existingLocation.setLongitude(longitude);
        existingLocation.setUpdatedAt(updatedAt);

        driverLocationRepository.save(existingLocation);

        return GenericApplicationResponseFactory.success(
                "200 - LOCATION_UPDATED",
                "Driver location was updated successfully."
        );
    }

    private GenericApplicationResponseDto createNewLocation(
            Long driverId,
            Double latitude,
            Double longitude,
            Instant updatedAt
    ) {
        DriverLocation driverLocation = new DriverLocation();
        driverLocation.setDriverId(driverId);
        driverLocation.setLatitude(latitude);
        driverLocation.setLongitude(longitude);
        driverLocation.setUpdatedAt(updatedAt);

        driverLocationRepository.save(driverLocation);

        return GenericApplicationResponseFactory.success(
                "201 - LOCATION_CREATED",
                "Driver location was created successfully."
        );
    }
}