ALTER TABLE trips
    ALTER COLUMN pickup_instant SET NOT NULL,
    ALTER COLUMN delivery_instant SET NOT NULL;

