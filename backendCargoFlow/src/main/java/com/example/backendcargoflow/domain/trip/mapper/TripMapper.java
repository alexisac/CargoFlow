package com.example.backendcargoflow.domain.trip.mapper;

import com.example.backendcargoflow.controller.trip.models.*;
import com.example.backendcargoflow.domain.trip.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.openapitools.jackson.nullable.JsonNullable;
import java.math.BigDecimal;
import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface TripMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tripStatus", ignore = true)
    @Mapping(target = "createdByUserId", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "pickupInstant", ignore = true)
    @Mapping(target = "deliveryInstant", ignore = true)
    Trip mapAddNewTripRequestDtoToTrip(AddNewTripRequestDto addNewTripRequestDto);

    Address mapAddressDtoToAddress(AddressDto addressDto);

    List<TripStatus> mapTripStatusList(List<TripStatusDto> statuses);

    @Mapping(target = "pickupCountry", source = "pickupAddress.country")
    @Mapping(target = "pickupCity", source = "pickupAddress.city")
    @Mapping(target = "deliveryCountry", source = "deliveryAddress.country")
    @Mapping(target = "deliveryCity", source = "deliveryAddress.city")
    TripSummaryDto mapTripToTripSummaryDto(Trip trip);

    List<TripSummaryDto> mapTripsToTripSummaryDtos(List<Trip> trips);

    TripDto mapTripToTripDto(Trip trip, String createdBy);

    default CargoType mapTripCargoType(CargoTypeDto tripCargoTypeDto) {
        return tripCargoTypeDto == null ? null : CargoType.valueOf(tripCargoTypeDto.name());
    }

    default Currency mapCurrency(CurrencyDto currencyDto) {
        return currencyDto == null ? null : Currency.valueOf(currencyDto.name());
    }

    default BigDecimal map(Double value) {
        return value == null ? null : BigDecimal.valueOf(value);
    }

    default <T> T map(JsonNullable<T> value) {
        return value == null || !value.isPresent() ? null : value.get();
    }

    default TripStatusDto mapTripStatus(TripStatus tripStatus) {
        return tripStatus == null ? null : TripStatusDto.valueOf(tripStatus.name());
    }

    default <T> JsonNullable<T> map(T value) {
        return JsonNullable.of(value);
    }
}
