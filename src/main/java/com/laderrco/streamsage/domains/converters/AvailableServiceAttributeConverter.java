package com.laderrco.streamsage.domains.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laderrco.streamsage.domains.AvailableService;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
@Deprecated
public class AvailableServiceAttributeConverter implements AttributeConverter<AvailableService, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public String convertToDatabaseColumn(AvailableService attribute) {
        if (attribute == null) {
            return null; // No need to process null
        }
        try {
            // Convert AvailableService to JSON String
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException jpe) {
            // Log the error, not just print the stack trace
            System.err.println("Error converting AvailableService to JSON: " + jpe.getMessage());
            return null;
        }
    }

    @Override
    public AvailableService convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null; // No need to process null
        }
        try {
            // Convert JSON String to AvailableService object
            return objectMapper.readValue(dbData, AvailableService.class);
        } catch (JsonProcessingException e) {
            // Log the error, include raw data for debugging
            System.err.println("Error converting JSON to AvailableService. Raw Data: " + dbData);
            return null;
        }
    }
    
}
