package com.laderrco.streamsage.domains.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laderrco.streamsage.domains.SuggestionPackage;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;

@Converter
@AllArgsConstructor
// This is a canadidate for error logging
public class SuggestionPackageAttributeConverter implements AttributeConverter<SuggestionPackage, String> {

    private final ObjectMapper objectMapper;
    @Override
    public String convertToDatabaseColumn(SuggestionPackage attribute) {
       try {
            return objectMapper.writeValueAsString(attribute);
       }
       catch (JsonProcessingException jpe) {
            jpe.printStackTrace();;
            return null;
       }
    }

    @Override
    public SuggestionPackage convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, SuggestionPackage.class);
        } catch (JsonProcessingException e) {
            System.err.println("Cannot convert JSON into SuggestionPackage. Raw Data: " + dbData);
            return null;
        }
    }
    
}
