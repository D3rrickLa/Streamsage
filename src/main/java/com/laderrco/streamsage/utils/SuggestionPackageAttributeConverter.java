package com.laderrco.streamsage.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laderrco.streamsage.domains.SuggestionPackage;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;

@Converter
@AllArgsConstructor
public class SuggestionPackageAttributeConverter implements AttributeConverter<SuggestionPackage, String> {

    private final ObjectMapper objectMapper;
    @Override
    public String convertToDatabaseColumn(SuggestionPackage attribute) {
       try {
            return objectMapper.writeValueAsString(attribute);
       }
       catch (JsonProcessingException jpe) {
            System.out.println(jpe);
            return null;
       }
    }

    @Override
    public SuggestionPackage convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, SuggestionPackage.class);
        } catch (JsonProcessingException e) {
            System.out.println("Cannot convert JSON into SuggestionPackage");
            return null;
        }
    }
    
}
