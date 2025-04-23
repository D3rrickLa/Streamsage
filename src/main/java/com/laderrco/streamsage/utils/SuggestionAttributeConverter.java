package com.laderrco.streamsage.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laderrco.streamsage.domains.SuggestionPackage;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;

@Converter
@AllArgsConstructor
public class SuggestionAttributeConverter implements AttributeConverter<SuggestionPackage, String> {

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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'convertToEntityAttribute'");
    }
    
}
