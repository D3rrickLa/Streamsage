package com.laderrco.streamsage.services;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LocaleService {
    public Locale getUserLocale() {
        return LocaleContextHolder.getLocale();
    }
}
