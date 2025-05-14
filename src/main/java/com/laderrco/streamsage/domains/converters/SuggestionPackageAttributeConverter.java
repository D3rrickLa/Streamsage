// package com.laderrco.streamsage.domains.converters;

// import com.fasterxml.jackson.core.JsonProcessingException;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.laderrco.streamsage.domains.SuggestionPackage;

// import jakarta.persistence.AttributeConverter;
// import jakarta.persistence.Converter;

// @Converter
// @Deprecated
// // This is a canadidate for error logging
// public class SuggestionPackageAttributeConverter implements AttributeConverter<SuggestionPackage, String> {

//     private static final ObjectMapper objectMapper = new ObjectMapper();
//     @Override
//     public String convertToDatabaseColumn(SuggestionPackage attribute) {
//        try {
//            return attribute == null ? null : objectMapper.writeValueAsString(attribute);
//         } catch (Exception e) {
//             throw new IllegalArgumentException("Error converting SuggestionPackage to JSON", e);
//         }
//     }

//     @Override
//     public SuggestionPackage convertToEntityAttribute(String dbData) {
//         try {
//             return objectMapper.readValue(dbData, SuggestionPackage.class);
//         } catch (JsonProcessingException e) {
//             throw new IllegalArgumentException("Error converting JSON to SuggestionPackage", e);
//         }
//     }
    
// }
