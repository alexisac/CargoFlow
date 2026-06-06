package com.example.locationservice.service;

import com.example.locationservice.controller.common.models.GenericApplicationResponseDto;
import com.example.locationservice.controller.location.models.DriverLocationDto;
import com.example.locationservice.controller.location.models.GetLatestDriverLocationsResponseDto;
import com.example.locationservice.controller.location.models.UpdateMyLocationRequestDto;
import com.example.locationservice.domain.entity.DriverLocation;
import com.example.locationservice.domain.repository.DriverLocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverLocationService {
    private final DriverLocationRepository driverLocationRepository;
    private final DriverLocationWebSocketPublisher driverLocationWebSocketPublisher;

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

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('DISPATCHER', 'MANAGER', 'ADMIN')")
    public GetLatestDriverLocationsResponseDto getLatestDriverLocations() {
        List<DriverLocationDto> driverLocationDtos = driverLocationRepository.findAll()
                .stream()
                .map(this::mapDriverLocationToDriverLocationDto)
                .toList();

        GetLatestDriverLocationsResponseDto response = new GetLatestDriverLocationsResponseDto();
        response.setDriverLocations(driverLocationDtos);

        return response;
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

        DriverLocation savedLocation = driverLocationRepository.save(existingLocation);

        publishLocationUpdate(savedLocation);

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

        DriverLocation savedLocation = driverLocationRepository.save(driverLocation);

        publishLocationUpdate(savedLocation);

        return GenericApplicationResponseFactory.success(
                "201 - LOCATION_CREATED",
                "Driver location was created successfully."
        );
    }

    private void publishLocationUpdate(DriverLocation driverLocation) {
        DriverLocationDto driverLocationDto = mapDriverLocationToDriverLocationDto(driverLocation);
        driverLocationWebSocketPublisher.publishDriverLocation(driverLocationDto);
    }

    private DriverLocationDto mapDriverLocationToDriverLocationDto(DriverLocation driverLocation) {
        DriverLocationDto driverLocationDto = new DriverLocationDto();

        driverLocationDto.setDriverId(driverLocation.getDriverId());
        driverLocationDto.setLatitude(driverLocation.getLatitude());
        driverLocationDto.setLongitude(driverLocation.getLongitude());
        driverLocationDto.setUpdatedAt(driverLocation.getUpdatedAt().atOffset(ZoneOffset.UTC));

        return driverLocationDto;
    }
}