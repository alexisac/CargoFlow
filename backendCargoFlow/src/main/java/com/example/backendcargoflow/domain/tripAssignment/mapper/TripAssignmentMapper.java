package com.example.backendcargoflow.domain.tripAssignment.mapper;

import com.example.backendcargoflow.controller.tripAssignment.models.AssignTripRequestDto;
import com.example.backendcargoflow.controller.tripAssignment.models.AvailableDriverDto;
import com.example.backendcargoflow.controller.tripAssignment.models.AvailableVehicleDto;
import com.example.backendcargoflow.domain.tripAssignment.entity.TripAssignment;
import com.example.backendcargoflow.domain.user.entity.AvailableDriverProjection;
import com.example.backendcargoflow.domain.vehicle.entity.AvailableVehicleProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface TripAssignmentMapper {
    AvailableDriverDto mapAvailableDriverProjectionToAvailableDriverDto(AvailableDriverProjection availableDriverProjection);

    List<AvailableDriverDto> mapAvailableDriverProjectionsToAvailableDriverDtos(List<AvailableDriverProjection> availableDriverProjections);

    AvailableVehicleDto mapAvailableVehicleProjectionToAvailableVehicleDto(AvailableVehicleProjection availableVehicleProjection);

    List<AvailableVehicleDto> mapAvailableVehicleProjectionsToAvailableVehicleDtos(List<AvailableVehicleProjection> availableVehicleProjections);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "assignedByUserId", ignore = true)
    @Mapping(target = "assignedDate", ignore = true)
    TripAssignment mapAssignTripRequestDtoToTripAssignment(AssignTripRequestDto assignTripRequestDto);

    default <T> T map(JsonNullable<T> value) {
        return value == null || !value.isPresent() ? null : value.get();
    }
}
