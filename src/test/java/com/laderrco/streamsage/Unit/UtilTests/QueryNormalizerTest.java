package com.laderrco.streamsage.Unit.UtilTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.laderrco.streamsage.utils.QueryNormalizer;

@ExtendWith(MockitoExtension.class)
public class QueryNormalizerTest {

    private QueryNormalizer queryNormalizerTest;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        queryNormalizerTest = new QueryNormalizer();
        queryNormalizerTest.hashCode();
    }


    @Test
    void testNormalizeQuery_Null() {
        String testQuery = QueryNormalizer.normalizeQuery(null);
        assertNull(testQuery);
    }
    @Test
    void testNormalizeQuery_Correct() {
        String testQuery = QueryNormalizer.normalizeQuery("give me 5 movies like Star Wars");
    
        assertNotNull(testQuery);
        assertEquals("recommendations:similar_to:star wars:5", testQuery);
    }
    
    @Test
    void testNormalizeQuery_IncorrectTitle() {
        String testQuery = QueryNormalizer.normalizeQuery("Star Wars");
    
        assertNotNull(testQuery);
        assertEquals("recommendations:unknown", testQuery);
    }
}
