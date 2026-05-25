package com.example.backendcargoflow.service;

import com.example.backendcargoflow.common.ErrorMessage;
import com.example.backendcargoflow.common.exceptions.BadRequestException;
import com.example.backendcargoflow.common.exceptions.ConflictException;
import com.example.backendcargoflow.controller.common.models.GenericApplicationResponseDto;
import com.example.backendcargoflow.controller.vehicle.models.AddNewVehicleRequestDto;
import com.example.backendcargoflow.domain.vehicle.entity.Vehicle;
import com.example.backendcargoflow.domain.vehicle.mapper.VehicleMapper;
import com.example.backendcargoflow.domain.vehicle.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;

    @PreAuthorize("hasAnyRole('DISPATCHER', 'MANAGER', 'ADMIN')")
    public GenericApplicationResponseDto addNewVehicle(AddNewVehicleRequestDto addNewVehicleRequestDto){
        Optional<Vehicle> vehicle = vehicleRepository.findByLicencePlateOrVin(
                addNewVehicleRequestDto.getLicencePlate(),
                addNewVehicleRequestDto.getVin()
        );

        if (vehicle.isPresent()) {
            throw new ConflictException(ErrorMessage.VEHICLE_ALREADY_EXIST);
        }

        validateVehicleCapacity(addNewVehicleRequestDto);

        Vehicle newVehicle = vehicleMapper.mapAddNewVehicleRequestDtoToVehicle(addNewVehicleRequestDto);
        vehicleRepository.save(newVehicle);
        return GenericApplicationResponseFactory.success(
                "201 - VEHICLE_CREATED",
                "Vehicle was created successfully"
        );
    }

    private void validateVehicleCapacity(AddNewVehicleRequestDto addNewVehicleRequestDto) {
        switch (addNewVehicleRequestDto.getVehicleType()) {
            case VAN,
                 BOX_TRUCK,
                 REFRIGERATED_TRUCK,
                 SEMI_TRAILER,
                 REFRIGERATED_TRAILER -> {
                if (!hasValue(addNewVehicleRequestDto.getMaxWeight()))
                    throw new BadRequestException(ErrorMessage.MAX_WEIGHT_REQUIRED);

                if (!hasValue(addNewVehicleRequestDto.getMaxVolume()))
                    throw new BadRequestException(ErrorMessage.MAX_VOLUME_REQUIRED);
            }

            case TANKER_TRUCK,
                 TANKER_TRAILER -> {
                if (hasValue(addNewVehicleRequestDto.getMaxWeight()))
                    throw new BadRequestException(ErrorMessage.MAX_WEIGHT_NOT_REQUIRED);

                if (!hasValue(addNewVehicleRequestDto.getMaxVolume()))
                    throw new BadRequestException(ErrorMessage.MAX_VOLUME_REQUIRED);
            }

            case TRACTOR_UNIT -> {
                if (hasValue(addNewVehicleRequestDto.getMaxWeight()))
                    throw new BadRequestException(ErrorMessage.MAX_WEIGHT_NOT_REQUIRED);

                if (hasValue(addNewVehicleRequestDto.getMaxVolume()))
                    throw new BadRequestException(ErrorMessage.MAX_VOLUME_NOT_REQUIRED);
            }
        }
    }

    private boolean hasValue(org.openapitools.jackson.nullable.JsonNullable<?> value) {
        return value != null && value.isPresent();
    }
}
