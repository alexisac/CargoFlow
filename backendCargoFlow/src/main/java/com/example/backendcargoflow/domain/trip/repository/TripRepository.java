package com.example.backendcargoflow.domain.trip.repository;

import com.example.backendcargoflow.domain.trip.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepository extends JpaRepository<Trip, Long> {

}
