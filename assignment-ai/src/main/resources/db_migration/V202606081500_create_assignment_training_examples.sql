CREATE TABLE assignment_training_examples (
    id                                      BIGSERIAL           PRIMARY KEY,
    trip_id                                 BIGINT              NOT NULL,
    driver_id                               BIGINT              NOT NULL,
    primary_vehicle_id                      BIGINT              NOT NULL,
    trailer_id                              BIGINT,
    road_distance_to_pickup_km              DOUBLE PRECISION    NOT NULL,
    cargo_weight                            DOUBLE PRECISION,
    cargo_volume                            DOUBLE PRECISION,
    weight_usage_ratio                      DOUBLE PRECISION    NOT NULL,
    volume_usage_ratio                      DOUBLE PRECISION    NOT NULL,
    has_weight_capacity                     DOUBLE PRECISION    NOT NULL,
    has_volume_capacity                     DOUBLE PRECISION    NOT NULL,
    keeps_previous_primary_vehicle          DOUBLE PRECISION    NOT NULL,
    keeps_previous_trailer                  DOUBLE PRECISION    NOT NULL,
    trailer_required                        DOUBLE PRECISION    NOT NULL,
    driver_available                        DOUBLE PRECISION    NOT NULL,
    primary_vehicle_available               DOUBLE PRECISION    NOT NULL,
    trailer_available                       DOUBLE PRECISION    NOT NULL,
    minutes_until_pickup                    DOUBLE PRECISION    NOT NULL,
    driver_completed_trips_last_30_days     DOUBLE PRECISION    NOT NULL,
    was_selected                            BOOLEAN             NOT NULL,
    source                                  VARCHAR(32)         NOT NULL,
    created_at                              TIMESTAMP           NOT NULL
);

CREATE INDEX idx_assignment_training_examples_trip_id
    ON assignment_training_examples(trip_id);

CREATE INDEX idx_assignment_training_examples_driver_id
    ON assignment_training_examples(driver_id);

CREATE INDEX idx_assignment_training_examples_created_at
    ON assignment_training_examples(created_at);
