CREATE TABLE IF NOT EXISTS trips
(
    id                              BIGSERIAL      PRIMARY KEY,
    trip_status                     VARCHAR(50)    NOT NULL,
    pickup_country                  VARCHAR(50)    NOT NULL,
    pickup_administrative_area      VARCHAR(50)    NOT NULL,
    pickup_city                     VARCHAR(50)    NOT NULL,
    pickup_street_name              VARCHAR(100)   NOT NULL,
    pickup_street_number            VARCHAR(15)    NOT NULL,
    pickup_postal_code              VARCHAR(15)    NOT NULL,
    pickup_additional_details       VARCHAR(250),
    delivery_country                VARCHAR(50)    NOT NULL,
    delivery_administrative_area    VARCHAR(50)    NOT NULL,
    delivery_city                   VARCHAR(50)    NOT NULL,
    delivery_street_name            VARCHAR(100)   NOT NULL,
    delivery_street_number          VARCHAR(15)    NOT NULL,
    delivery_postal_code            VARCHAR(15)    NOT NULL,
    delivery_additional_details     VARCHAR(250),
    pickup_date_time                TIMESTAMP      NOT NULL,
    pickup_time_zone                VARCHAR(50)    NOT NULL,
    delivery_date_time              TIMESTAMP      NOT NULL,
    delivery_time_zone              VARCHAR(50)    NOT NULL,
    cargo_description               VARCHAR(250),
    cargo_weight                    INTEGER,
    cargo_volume                    INTEGER,
    cargo_type                      VARCHAR(50)    NOT NULL,
    price                           NUMERIC(12, 2) NOT NULL,
    currency                        VARCHAR(3)     NOT NULL,
    additional_info                 VARCHAR(250),
    created_by_user_id              BIGINT         NOT NULL,
    created_date                    TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_trips_created_by_user
        FOREIGN KEY (created_by_user_id)
        REFERENCES users (id)
);