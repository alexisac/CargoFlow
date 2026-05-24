package com.example.backendcargoflow.domain.trip.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "trips")
@NoArgsConstructor
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "trip_status", nullable = false)
    private TripStatus tripStatus;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "country", column = @Column(name = "pickup_country", nullable = false)),
            @AttributeOverride(name = "administrativeArea", column = @Column(name = "pickup_administrative_area", nullable = false)),
            @AttributeOverride(name = "city", column = @Column(name = "pickup_city", nullable = false)),
            @AttributeOverride(name = "streetName", column = @Column(name = "pickup_street_name", nullable = false)),
            @AttributeOverride(name = "streetNumber", column = @Column(name = "pickup_street_number", nullable = false)),
            @AttributeOverride(name = "postalCode", column = @Column(name = "pickup_postal_code", nullable = false)),
            @AttributeOverride(name = "additionalDetails", column = @Column(name = "pickup_additional_details"))
    })
    private Address pickupAddress;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "country", column = @Column(name = "delivery_country", nullable = false)),
            @AttributeOverride(name = "administrativeArea", column = @Column(name = "delivery_administrative_area", nullable = false)),
            @AttributeOverride(name = "city", column = @Column(name = "delivery_city", nullable = false)),
            @AttributeOverride(name = "streetName", column = @Column(name = "delivery_street_name", nullable = false)),
            @AttributeOverride(name = "streetNumber", column = @Column(name = "delivery_street_number", nullable = false)),
            @AttributeOverride(name = "postalCode", column = @Column(name = "delivery_postal_code", nullable = false)),
            @AttributeOverride(name = "additionalDetails", column = @Column(name = "delivery_additional_details"))
    })
    private Address deliveryAddress;

    @Column(name = "pickup_date_time", nullable = false)
    private LocalDateTime pickupDateTime;

    @Column(name = "pickup_time_zone", nullable = false)
    private String pickupTimeZone;

    @Column(name = "delivery_date_time", nullable = false)
    private LocalDateTime deliveryDateTime;

    @Column(name = "delivery_time_zone", nullable = false)
    private String deliveryTimeZone;

    @Column(name = "cargo_description")
    private String cargoDescription;

    @Column(name = "cargo_weight")
    private Integer cargoWeight;

    @Column(name = "cargo_volume")
    private Integer cargoVolume;

    @Enumerated(EnumType.STRING)
    @Column(name = "cargo_type", nullable = false)
    private CargoType cargoType;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private Currency currency;

    @Column(name = "additional_info")
    private String additionalInfo;

    @Column(name = "created_by_user_id", nullable = false)
    private Long createdByUserId;

    @CreationTimestamp
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "pickup_instant", nullable = false)
    private Instant pickupInstant;

    @Column(name = "delivery_instant", nullable = false)
    private Instant deliveryInstant;
}
