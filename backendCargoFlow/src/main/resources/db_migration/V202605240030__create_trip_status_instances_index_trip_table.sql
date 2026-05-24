CREATE INDEX idx_trips_status_instants
    ON trips(trip_status, pickup_instant, delivery_instant);