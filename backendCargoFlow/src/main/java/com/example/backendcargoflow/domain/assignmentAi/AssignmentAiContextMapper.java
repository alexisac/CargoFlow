package com.example.backendcargoflow.domain.assignmentAi;

import com.example.backendcargoflow.controller.assignmentAiInternal.models.AssignmentAiAddressDataDto;
import com.example.backendcargoflow.controller.assignmentAiInternal.models.AssignmentAiTripDataDto;
import com.example.backendcargoflow.controller.assignmentAiInternal.models.AssignmentAiVehicleDataDto;
import com.example.backendcargoflow.controller.assignmentAiInternal.models.AssignmentAiVehicleTypeDto;
import com.example.backendcargoflow.domain.trip.entity.Trip;
import com.example.backendcargoflow.domain.vehicle.entity.AssignmentAiAvailableVehicleProjection;
import com.example.backendcargoflow.domain.vehicle.entity.VehicleType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface AssignmentAiContextMapper {

    default AssignmentAiTripDataDto mapTripToAssignmentAiTripDataDto(Trip trip) {
        return new AssignmentAiTripDataDto()
                .tripId(trip.getId())
                .pickupAddress(new AssignmentAiAddressDataDto()
                        .country(trip.getPickupAddress().getCountry())
                        .administrativeArea(trip.getPickupAddress().getAdministrativeArea())
                        .city(trip.getPickupAddress().getCity())
                        .streetName(trip.getPickupAddress().getStreetName())
                        .streetNumber(trip.getPickupAddress().getStreetNumber())
                        .postalCode(trip.getPickupAddress().getPostalCode())
                        .additionalDetails(trip.getPickupAddress().getAdditionalDetails()))
                .deliveryAddress(new AssignmentAiAddressDataDto()
                        .country(trip.getDeliveryAddress().getCountry())
                        .administrativeArea(trip.getDeliveryAddress().getAdministrativeArea())
                        .city(trip.getDeliveryAddress().getCity())
                        .streetName(trip.getDeliveryAddress().getStreetName())
                        .streetNumber(trip.getDeliveryAddress().getStreetNumber())
                        .postalCode(trip.getDeliveryAddress().getPostalCode())
                        .additionalDetails(trip.getDeliveryAddress().getAdditionalDetails()))
                .pickupInstant(OffsetDateTime.ofInstant(trip.getPickupInstant(), ZoneOffset.UTC))
                .deliveryInstant(OffsetDateTime.ofInstant(trip.getDeliveryInstant(), ZoneOffset.UTC))
                .cargoWeight(trip.getCargoWeight() == null ? null : trip.getCargoWeight().doubleValue())
                .cargoVolume(trip.getCargoVolume() == null ? null : trip.getCargoVolume().doubleValue())
                .cargoType(trip.getCargoType().name());
    }

    default AssignmentAiVehicleDataDto mapAssignmentAiAvailableVehicleProjectionToAssignmentAiVehicleDataDto(
            AssignmentAiAvailableVehicleProjection projection
    ) {
        return new AssignmentAiVehicleDataDto()
                .vehicleId(projection.getId())
                .vehicleType(mapVehicleType(projection.getVehicleType()))
                .maxWeight(projection.getMaxWeight() == null ? null : projection.getMaxWeight().doubleValue())
                .maxVolume(projection.getMaxVolume() == null ? null : projection.getMaxVolume().doubleValue());
    }

    default AssignmentAiVehicleTypeDto mapVehicleType(VehicleType vehicleType) {
        return vehicleType == null
                ? null
                : AssignmentAiVehicleTypeDto.valueOf(vehicleType.name());
    }
}