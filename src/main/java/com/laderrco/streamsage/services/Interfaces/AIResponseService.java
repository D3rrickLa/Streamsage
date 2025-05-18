package com.laderrco.streamsage.services.Interfaces;

import java.io.IOException;

public interface AIResponseService {
    public String sendPrompt(String prompt) throws IOException;
}
