package com.example.backendcargoflow.service;

import com.example.backendcargoflow.common.ErrorMessage;
import com.example.backendcargoflow.common.exceptions.BadRequestException;
import com.example.backendcargoflow.common.exceptions.NotFoundException;
import com.example.backendcargoflow.common.security.CurrentUserService;
import com.example.backendcargoflow.controller.common.models.GenericApplicationResponseDto;
import com.example.backendcargoflow.controller.trip.models.AddNewTripRequestDto;
import com.example.backendcargoflow.controller.trip.models.TripDto;
import com.example.backendcargoflow.controller.trip.models.TripPageResponseDto;
import com.example.backendcargoflow.controller.trip.models.TripSearchRequestDto;
import com.example.backendcargoflow.domain.trip.entity.Trip;
import com.example.backendcargoflow.domain.trip.entity.TripStatus;
import com.example.backendcargoflow.domain.trip.mapper.TripMapper;
import com.example.backendcargoflow.domain.trip.repository.TripRepository;
import com.example.backendcargoflow.domain.trip.repository.TripSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripService {
    private final TripRepository tripRepository;
    private final TripMapper tripMapper;
    private final CurrentUserService currentUserService;
    private final UserLookupService userLookupService;

    @PreAuthorize("hasAnyRole('DISPATCHER', 'MANAGER', 'ADMIN')")
    public GenericApplicationResponseDto addNewTrip(AddNewTripRequestDto addNewTripRequestDto) {
        Long currentUserId = currentUserService.getCurrentUserId();
        Trip trip = tripMapper.mapAddNewTripRequestDtoToTrip(addNewTripRequestDto);

        Instant pickupInstant = convertToInstant(
                trip.getPickupDateTime(),
                trip.getPickupTimeZone()
        );

        Instant deliveryInstant = convertToInstant(
                trip.getDeliveryDateTime(),
                trip.getDeliveryTimeZone()
        );

        trip.setPickupInstant(pickupInstant);
        trip.setDeliveryInstant(deliveryInstant);
        trip.setTripStatus(TripStatus.PLANNED);
        trip.setCreatedByUserId(currentUserId);
        tripRepository.save(trip);
        return GenericApplicationResponseFactory.success(
                "201 - TRIP_CREATED",
                "Trip was created successfully"
        );
    }

    @PreAuthorize("hasAnyRole('DISPATCHER', 'MANAGER', 'ADMIN')")
    public TripPageResponseDto searchTrips(TripSearchRequestDto tripSearchRequestDto) {
        List<TripStatus> tripStatusList = tripMapper.mapTripStatusList(tripSearchRequestDto.getTripStatusList());

        Specification<Trip> specification = Specification
                .where(TripSpecification.hasTripStatusList(tripStatusList))
                .and(TripSpecification.hasPickupCountries(tripSearchRequestDto.getPickupCountries()))
                .and(TripSpecification.hasPickupCities(tripSearchRequestDto.getPickupCities()))
                .and(TripSpecification.hasDeliveryCountries(tripSearchRequestDto.getDeliveryCountries()))
                .and(TripSpecification.hasDeliveryCities(tripSearchRequestDto.getDeliveryCities()))
                .and(TripSpecification.pickupDateTimeFrom(parseDateTime(tripSearchRequestDto.getPickupDateTimeFrom())))
                .and(TripSpecification.pickupDateTimeTo(parseDateTime(tripSearchRequestDto.getPickupDateTimeTo())))
                .and(TripSpecification.deliveryDateTimeFrom(parseDateTime(tripSearchRequestDto.getDeliveryDateTimeFrom())))
                .and(TripSpecification.deliveryDateTimeTo(parseDateTime(tripSearchRequestDto.getDeliveryDateTimeTo())));

        Pageable pageable = PageRequest.of(
                tripSearchRequestDto.getPageNumber(),
                tripSearchRequestDto.getPageSize()
        );
        Page<Trip> tripPage = tripRepository.findAll(specification, pageable);

        TripPageResponseDto response = new TripPageResponseDto();
        response.setTrips(tripMapper.mapTripsToTripSummaryDtos(tripPage.getContent()));
        response.setPageNumber(tripPage.getNumber());
        response.setPageSize(tripPage.getSize());
        response.setLastPage(tripPage.isLast());

        return response;
    }

    @PreAuthorize("hasAnyRole('DISPATCHER', 'MANAGER', 'ADMIN')")
    public TripDto getTrip(Long id) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.TRIP_NOT_FOUND));

        String createdBy = userLookupService.getUserFullNameById(trip.getCreatedByUserId());
        return tripMapper.mapTripToTripDto(trip, createdBy);
    }

    private LocalDateTime parseDateTime(String value) {
        return value == null || value.isBlank() ? null : LocalDateTime.parse(value);
    }

    private Instant convertToInstant(LocalDateTime dateTime, String timeZone) {
        try {
            return dateTime
                    .atZone(ZoneId.of(timeZone))
                    .toInstant();
        } catch (DateTimeException exception) {
            throw new BadRequestException(String.format(ErrorMessage.INVALID_TIME_ZONE, timeZone));
        }
    }
}
