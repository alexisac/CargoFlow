package com.example.backendcargoflow.common;

import com.fasterxml.jackson.databind.Module;
import jakarta.validation.valueextraction.ExtractedValue;
import jakarta.validation.valueextraction.UnwrapByDefault;
import jakarta.validation.valueextraction.ValueExtractor;
import org.openapitools.jackson.nullable.JsonNullable;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.boot.autoconfigure.validation.ValidationConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public Module jsonNullableModule() {
        return new JsonNullableModule();
    }

    @Bean
    public ValidationConfigurationCustomizer jsonNullableValidationCustomizer() {
        return configuration ->
                configuration.addValueExtractor(new JsonNullableJakartaValueExtractor());
    }

    @UnwrapByDefault
    private static class JsonNullableJakartaValueExtractor
            implements ValueExtractor<JsonNullable<@ExtractedValue ?>> {

        @Override
        public void extractValues(JsonNullable<?> originalValue, ValueReceiver receiver) {
            if (originalValue != null && originalValue.isPresent()) {
                receiver.value(null, originalValue.get());
            }
        }
    }
}