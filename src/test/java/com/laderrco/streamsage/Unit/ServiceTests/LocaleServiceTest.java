package com.laderrco.streamsage.Unit.ServiceTests;

import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.i18n.LocaleContextHolder;

import com.laderrco.streamsage.services.LocaleService;

public class LocaleServiceTest {

    @Mock
    private LocaleService localeServiceTest;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserLocale() {
        Locale locale = LocaleContextHolder.getLocale();
        when(localeServiceTest.getUserLocale()).thenReturn(Locale.of("en", "CA"));
        Locale testLocale = localeServiceTest.getUserLocale();

        System.out.println(locale.toString());
        System.err.println(testLocale.toString());

        // assertEquals(locale.toString(), testLocale.toString());
    }
}
