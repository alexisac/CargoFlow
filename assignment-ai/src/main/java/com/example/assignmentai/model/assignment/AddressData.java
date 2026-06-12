package com.example.assignmentai.model.assignment;

public record AddressData(
        String country,
        String administrativeArea,
        String city,
        String streetName,
        String streetNumber,
        String postalCode,
        String additionalDetails
) {
    public String toFullAddress() {
        StringBuilder builder = new StringBuilder();

        appendIfPresent(builder, streetName);
        appendIfPresent(builder, streetNumber);
        appendIfPresent(builder, city);
        appendIfPresent(builder, administrativeArea);
        appendIfPresent(builder, postalCode);
        appendIfPresent(builder, country);
        appendIfPresent(builder, additionalDetails);

        return builder.toString();
    }

    private static void appendIfPresent(
            StringBuilder builder,
            String value
    ) {
        if (value == null || value.isBlank()) {
            return;
        }

        if (!builder.isEmpty()) {
            builder.append(", ");
        }

        builder.append(value);
    }
}