CREATE TABLE IF NOT EXISTS trip_assignments
(
    id                      BIGSERIAL   PRIMARY KEY,
    trip_id                 BIGINT      NOT NULL,
    driver_id               BIGINT      NOT NULL,
    primary_vehicle_id      BIGINT      NOT NULL,
    trailer_vehicle_id      BIGINT,
    assigned_by_user_id     BIGINT      NOT NULL,
    assigned_date           TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_trip_assignments_trip
    FOREIGN KEY (trip_id)
    REFERENCES trips (id),

    CONSTRAINT fk_trip_assignments_driver
    FOREIGN KEY (driver_id)
    REFERENCES users (id),

    CONSTRAINT fk_trip_assignments_primary_vehicle
    FOREIGN KEY (primary_vehicle_id)
    REFERENCES vehicles (id),

    CONSTRAINT fk_trip_assignments_trailer_vehicle
    FOREIGN KEY (trailer_vehicle_id)
    REFERENCES vehicles (id),

    CONSTRAINT fk_trip_assignments_assigned_by_user
    FOREIGN KEY (assigned_by_user_id)
    REFERENCES users (id)
    );

CREATE INDEX idx_trip_assignments_trip_id
    ON trip_assignments(trip_id);

CREATE INDEX idx_trip_assignments_driver_id
    ON trip_assignments(driver_id);

CREATE INDEX idx_trip_assignments_trip_driver
    ON trip_assignments(trip_id, driver_id);