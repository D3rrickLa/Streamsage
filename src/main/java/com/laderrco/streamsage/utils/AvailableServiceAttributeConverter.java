package com.laderrco.streamsage.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laderrco.streamsage.domains.AvailableService;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;

@Converter
@AllArgsConstructor
public class AvailableServiceAttributeConverter implements AttributeConverter<AvailableService, String> {

    private final ObjectMapper objectMapper;
    @Override
    public String convertToDatabaseColumn(AvailableService attribute) {
       try {
            return objectMapper.writeValueAsString(attribute);
       }
       catch (JsonProcessingException jpe) {
            System.out.println(jpe);
            return null;
       }
    }

    @Override
    public AvailableService convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, AvailableService.class);
        } catch (JsonProcessingException e) {
            System.out.println("Cannot convert JSON into SuggestionPackage");
            return null;
        }
    }
    
}
