package com.example.backendcargoflow.domain.vehicle.repository;

import com.example.backendcargoflow.domain.vehicle.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByLicencePlateOrVin(String licencePlate, String vin);
}
