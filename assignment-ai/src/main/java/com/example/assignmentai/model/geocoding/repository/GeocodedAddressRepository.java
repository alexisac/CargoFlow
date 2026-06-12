package com.example.assignmentai.model.geocoding.repository;

import com.example.assignmentai.model.geocoding.entity.GeocodedAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GeocodedAddressRepository extends JpaRepository<GeocodedAddress, Long> {
    Optional<GeocodedAddress> findByAddressHash(String addressHash);
}