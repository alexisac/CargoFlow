package com.example.backendcargoflow.domain.trip.mapper;

import com.example.backendcargoflow.controller.trip.models.AddNewTripRequestDto;
import com.example.backendcargoflow.controller.trip.models.AddressDto;
import com.example.backendcargoflow.controller.trip.models.CargoTypeDto;
import com.example.backendcargoflow.controller.trip.models.CurrencyDto;
import com.example.backendcargoflow.domain.trip.entity.Address;
import com.example.backendcargoflow.domain.trip.entity.CargoType;
import com.example.backendcargoflow.domain.trip.entity.Currency;
import com.example.backendcargoflow.domain.trip.entity.Trip;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.openapitools.jackson.nullable.JsonNullable;
import java.math.BigDecimal;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface TripMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tripStatus", ignore = true)
    @Mapping(target = "createdByUserId", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    Trip mapAddNewTripRequestDtoToTrip(AddNewTripRequestDto addNewTripRequestDto);

    Address mapAddressDtoToAddress(AddressDto addressDto);

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
}
