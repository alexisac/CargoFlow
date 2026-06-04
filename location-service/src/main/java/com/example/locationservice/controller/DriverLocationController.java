package com.example.locationservice.controller;

import com.example.locationservice.common.LogMessage;
import com.example.locationservice.common.security.CurrentUserService;
import com.example.locationservice.controller.common.models.GenericApplicationResponseDto;
import com.example.locationservice.controller.location.api.LocationsApi;
import com.example.locationservice.controller.location.models.UpdateMyLocationRequestDto;
import com.example.locationservice.service.DriverLocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DriverLocationController implements LocationsApi {
    private final DriverLocationService driverLocationService;
    private final CurrentUserService currentUserService;

    @Override
    public GenericApplicationResponseDto updateMyLocation(
            @RequestBody UpdateMyLocationRequestDto updateMyLocationRequestDto
    ) {
        Long driverId = currentUserService.getCurrentUserId();

        log.info(String.format(LogMessage.UPDATE_DRIVER_LOCATION,
                driverId,
                updateMyLocationRequestDto.getLatitude(),
                updateMyLocationRequestDto.getLongitude(),
                updateMyLocationRequestDto.getUpdatedAt()
        ));

        return driverLocationService.updateMyLocation(driverId, updateMyLocationRequestDto);
    }
}