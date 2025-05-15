package com.laderrco.streamsage.Unit.ServiceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.laderrco.streamsage.entities.ErrorLog;
import com.laderrco.streamsage.repositories.ErrorLogRepository;
import com.laderrco.streamsage.services.ErrorLogServiceImpl;
import com.laderrco.streamsage.utils.TimestampGenerator;


@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
public class ErrorLogServiceTest {
    @Mock
    private ErrorLogRepository errorLogRepository;

    @Mock
    private TimestampGenerator timestampGenerator;

    private ErrorLogServiceImpl errorLogService;

    private List<ErrorLog> errorLogs;
    
    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        errorLogService = new ErrorLogServiceImpl(errorLogRepository, timestampGenerator);
        errorLogs = new ArrayList<>();
        errorLogs.add(new ErrorLog());
        errorLogs.add(new ErrorLog());
        errorLogs.add(new ErrorLog());
    }

    @Test
    void testfindAll_Correct() {
        when(errorLogRepository.findByOrderByTimestampDesc()).thenReturn(errorLogs);


        List<ErrorLog> logs = errorLogService.findAll();

        assertEquals(errorLogs, logs);
    }
    @Test
    void testfindAll_NoLogs() {
        List<ErrorLog> logs = errorLogService.findAll();

        assertEquals(logs.size(), 0);
    }

    @Test
    void testSave_Correct() {
        ErrorLog logs = new ErrorLog("test message", "001", "sometrace");
        when(errorLogRepository.save(any())).thenReturn(logs);

        errorLogs.add(logs); // track it manually if needed
        ErrorLog s = errorLogRepository.save(logs);

        assertEquals(4, errorLogs.size());
        assertEquals(logs, s);
    }


    @Test 
    void testSave() {
        ErrorLog errorLogTest = new ErrorLog("message", "code_01", "stacktrace");
        when(errorLogRepository.save(any())).thenReturn(errorLogTest);
        errorLogService.save(errorLogTest);
    }
}
