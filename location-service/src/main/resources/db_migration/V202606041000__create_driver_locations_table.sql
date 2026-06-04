CREATE TABLE IF NOT EXISTS driver_locations (
    id          BIGSERIAL                   PRIMARY KEY,
    driver_id   BIGINT                      NOT NULL UNIQUE,
    latitude    DOUBLE PRECISION            NOT NULL,
    longitude   DOUBLE PRECISION            NOT NULL,
    updated_at  TIMESTAMP WITH TIME ZONE    NOT NULL
);