CREATE TABLE IF NOT EXISTS vehicles
(
    id               BIGSERIAL    PRIMARY KEY,
    licence_plate    VARCHAR(20)  NOT NULL UNIQUE,
    vin              VARCHAR(17)  NOT NULL UNIQUE,
    brand            VARCHAR(50)  NOT NULL,
    model            VARCHAR(50)  NOT NULL,
    manufacture_year INTEGER      NOT NULL,
    vehicle_type     VARCHAR(50)  NOT NULL,
    max_weight       INTEGER      NOT NULL,
    max_volume       INTEGER      NOT NULL,
    vehicle_status   VARCHAR(50)  NOT NULL,
    additional_info  VARCHAR(250),
    create_date      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
)