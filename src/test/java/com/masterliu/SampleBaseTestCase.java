package com.masterliu;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleBaseTestCase {

    private AutoCloseable closeable;

    @BeforeAll
    public static void beforeAll() {
        ILoggerFactory logFactory = LoggerFactory.getILoggerFactory();
        if (logFactory instanceof LoggerContext logContext) {
            logContext.getLogger(Logger.ROOT_LOGGER_NAME).setLevel(Level.DEBUG);
        }
    }

    @BeforeEach
    public void openMocks() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void releaseMocks() throws Exception {
        closeable.close();
    }

}
