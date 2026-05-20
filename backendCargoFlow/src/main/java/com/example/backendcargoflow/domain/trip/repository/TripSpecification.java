package com.example.backendcargoflow.domain.trip.repository;

import com.example.backendcargoflow.domain.trip.entity.Trip;
import com.example.backendcargoflow.domain.trip.entity.TripStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

public class TripSpecification {
    private TripSpecification(){}

    public static Specification<Trip> hasTripStatusList(List<TripStatus> statusList) {
        return (root, query, criteriaBuilder) -> {
            if (statusList == null || statusList.isEmpty()) {
                return null;
            }

            return root.get("tripStatus").in(statusList);
        };
    }

    public static Specification<Trip> hasPickupCountries(List<String> countries) {
        return (root, query, criteriaBuilder) -> {
            if (countries == null || countries.isEmpty()) {
                return null;
            }

            return root.get("pickupAddress").get("country").in(countries);
        };
    }

    public static Specification<Trip> hasPickupCities(List<String> cities) {
        return (root, query, criteriaBuilder) -> {
            if (cities == null || cities.isEmpty()) {
                return null;
            }

            return root.get("pickupAddress").get("city").in(cities);
        };
    }

    public static Specification<Trip> hasDeliveryCountries(List<String> countries) {
        return (root, query, criteriaBuilder) -> {
            if (countries == null || countries.isEmpty()) {
                return null;
            }

            return root.get("deliveryAddress").get("country").in(countries);
        };
    }

    public static Specification<Trip> hasDeliveryCities(List<String> cities) {
        return (root, query, criteriaBuilder) -> {
            if (cities == null || cities.isEmpty()) {
                return null;
            }

            return root.get("deliveryAddress").get("city").in(cities);
        };
    }

    public static Specification<Trip> pickupDateTimeFrom(LocalDateTime from) {
        return (root, query, criteriaBuilder) -> {
            if (from == null) {
                return null;
            }

            return criteriaBuilder.greaterThanOrEqualTo(root.get("pickupDateTime"), from);
        };
    }

    public static Specification<Trip> pickupDateTimeTo(LocalDateTime to) {
        return (root, query, criteriaBuilder) -> {
            if (to == null) {
                return null;
            }

            return criteriaBuilder.lessThanOrEqualTo(root.get("pickupDateTime"), to);
        };
    }

    public static Specification<Trip> deliveryDateTimeFrom(LocalDateTime from) {
        return (root, query, criteriaBuilder) -> {
            if (from == null) {
                return null;
            }

            return criteriaBuilder.greaterThanOrEqualTo(root.get("deliveryDateTime"), from);
        };
    }

    public static Specification<Trip> deliveryDateTimeTo(LocalDateTime to) {
        return (root, query, criteriaBuilder) -> {
            if (to == null) {
                return null;
            }

            return criteriaBuilder.lessThanOrEqualTo(root.get("deliveryDateTime"), to);
        };
    }
}
