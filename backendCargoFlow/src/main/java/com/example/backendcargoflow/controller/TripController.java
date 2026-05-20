package com.example.backendcargoflow.controller;

import com.example.backendcargoflow.common.LogMessage;
import com.example.backendcargoflow.controller.common.models.GenericApplicationResponseDto;
import com.example.backendcargoflow.controller.trip.api.TripsApi;
import com.example.backendcargoflow.controller.trip.models.AddNewTripRequestDto;
import com.example.backendcargoflow.controller.trip.models.TripPageResponseDto;
import com.example.backendcargoflow.controller.trip.models.TripSearchRequestDto;
import com.example.backendcargoflow.service.TripService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TripController implements TripsApi {
    private final TripService tripService;

    @Override
    public GenericApplicationResponseDto addNewTrip(@RequestBody AddNewTripRequestDto addNewTripRequestDto) {
        log.info(String.format(LogMessage.ADD_NEW_TRIP,
                addNewTripRequestDto.getPickupAddress().getCountry(),
                addNewTripRequestDto.getPickupAddress().getAdministrativeArea(),
                addNewTripRequestDto.getPickupAddress().getCity(),
                addNewTripRequestDto.getPickupAddress().getStreetName(),
                addNewTripRequestDto.getPickupAddress().getStreetNumber(),
                addNewTripRequestDto.getPickupAddress().getPostalCode(),
                addNewTripRequestDto.getPickupAddress().getAdditionalDetails(),
                addNewTripRequestDto.getDeliveryAddress().getCountry(),
                addNewTripRequestDto.getDeliveryAddress().getAdministrativeArea(),
                addNewTripRequestDto.getDeliveryAddress().getCity(),
                addNewTripRequestDto.getDeliveryAddress().getStreetName(),
                addNewTripRequestDto.getDeliveryAddress().getStreetNumber(),
                addNewTripRequestDto.getDeliveryAddress().getPostalCode(),
                addNewTripRequestDto.getDeliveryAddress().getAdditionalDetails(),
                addNewTripRequestDto.getPickupDateTime(),
                addNewTripRequestDto.getPickupTimeZone(),
                addNewTripRequestDto.getDeliveryDateTime(),
                addNewTripRequestDto.getDeliveryTimeZone(),
                addNewTripRequestDto.getCargoDescription(),
                addNewTripRequestDto.getCargoWeight(),
                addNewTripRequestDto.getCargoVolume(),
                addNewTripRequestDto.getCargoType(),
                addNewTripRequestDto.getPrice(),
                addNewTripRequestDto.getCurrency(),
                addNewTripRequestDto.getAdditionalInfo()
        ));
        return tripService.addNewTrip(addNewTripRequestDto);
    }

    @Override
    public TripPageResponseDto searchTrips(@RequestBody TripSearchRequestDto tripSearchRequestDto) {
        log.info(String.format(LogMessage.SEARCH_TRIPS,
                tripSearchRequestDto.getTripStatusList(),
                tripSearchRequestDto.getPickupCountries(),
                tripSearchRequestDto.getPickupCities(),
                tripSearchRequestDto.getDeliveryCountries(),
                tripSearchRequestDto.getDeliveryCities(),
                tripSearchRequestDto.getPickupDateTimeFrom(),
                tripSearchRequestDto.getPickupDateTimeTo(),
                tripSearchRequestDto.getDeliveryDateTimeFrom(),
                tripSearchRequestDto.getDeliveryDateTimeTo(),
                tripSearchRequestDto.getPageNumber(),
                tripSearchRequestDto.getPageSize()
        ));
        return tripService.searchTrips(tripSearchRequestDto);
    }
}
