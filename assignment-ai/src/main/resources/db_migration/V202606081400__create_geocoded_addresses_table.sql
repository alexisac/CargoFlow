CREATE TABLE geocoded_addresses (
    id              BIGSERIAL           PRIMARY KEY,
    address_hash    VARCHAR(128)        NOT NULL UNIQUE,
    full_address    TEXT                NOT NULL,
    latitude        DOUBLE PRECISION    NOT NULL,
    longitude       DOUBLE PRECISION    NOT NULL,
    created_at      TIMESTAMP           NOT NULL,
    updated_at      TIMESTAMP           NOT NULL
);

CREATE INDEX idx_geocoded_addresses_address_hash
    ON geocoded_addresses(address_hash);