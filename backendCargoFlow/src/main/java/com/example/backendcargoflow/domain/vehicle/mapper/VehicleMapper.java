package com.example.backendcargoflow.domain.vehicle.mapper;

import com.example.backendcargoflow.controller.vehicle.models.AddNewVehicleRequestDto;
import com.example.backendcargoflow.controller.vehicle.models.VehicleStatusDto;
import com.example.backendcargoflow.controller.vehicle.models.VehicleTypeDto;
import com.example.backendcargoflow.domain.vehicle.entity.Vehicle;
import com.example.backendcargoflow.domain.vehicle.entity.VehicleStatus;
import com.example.backendcargoflow.domain.vehicle.entity.VehicleType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.openapitools.jackson.nullable.JsonNullable;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface VehicleMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    Vehicle mapAddNewVehicleRequestDtoToVehicle(AddNewVehicleRequestDto addNewVehicleRequestDto);

    default VehicleType mapType(VehicleTypeDto typeDto) {
        return typeDto == null ? null : VehicleType.valueOf(typeDto.name());
    }

    default VehicleStatus mapStatus(VehicleStatusDto statusDto) {
        return statusDto == null ? null : VehicleStatus.valueOf(statusDto.name());
    }

    default <T> T map(JsonNullable<T> value) {
        return value == null || !value.isPresent() ? null : value.get();
    }
}
