package com.example.backendcargoflow.domain.trip.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
public class Address {
    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "administrative_area", nullable = false)
    private String administrativeArea;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "street_name", nullable = false)
    private String streetName;

    @Column(name = "street_number", nullable = false)
    private String streetNumber;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Column(name = "additional_details")
    private String additionalDetails;
}
