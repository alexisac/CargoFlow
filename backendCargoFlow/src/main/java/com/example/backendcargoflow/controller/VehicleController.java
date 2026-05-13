package com.example.backendcargoflow.controller;

import com.example.backendcargoflow.common.LogMessage;
import com.example.backendcargoflow.controller.common.models.GenericApplicationResponseDto;
import com.example.backendcargoflow.controller.vehicle.api.VehiclesApi;
import com.example.backendcargoflow.controller.vehicle.models.AddNewVehicleRequestDto;
import com.example.backendcargoflow.service.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class VehicleController implements VehiclesApi {
    private final VehicleService vehicleService;

    @Override
    public GenericApplicationResponseDto addNewVehicle(@RequestBody AddNewVehicleRequestDto addNewVehicleRequestDto) {
        log.info(String.format(LogMessage.ADD_NEW_VEHICLE,
                addNewVehicleRequestDto.getLicencePlate(),
                addNewVehicleRequestDto.getVin(),
                addNewVehicleRequestDto.getBrand(),
                addNewVehicleRequestDto.getModel(),
                addNewVehicleRequestDto.getManufactureYear(),
                addNewVehicleRequestDto.getVehicleType(),
                addNewVehicleRequestDto.getMaxWeight(),
                addNewVehicleRequestDto.getMaxVolume(),
                addNewVehicleRequestDto.getVehicleStatus(),
                addNewVehicleRequestDto.getAdditionalInfo()
        ));
        return vehicleService.addNewVehicle(addNewVehicleRequestDto);
    }
}
