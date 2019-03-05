package org.cishell.cibridge.cishell.impl;

import io.reactivex.functions.Action;
import io.reactivex.subscribers.TestSubscriber;
import org.cishell.cibridge.cishell.IntegrationTestCase;
import org.cishell.cibridge.core.model.Log;
import org.cishell.cibridge.core.model.LogFilter;
import org.cishell.cibridge.core.model.LogLevel;
import org.cishell.cibridge.core.model.LogQueryResults;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

public class CIShellCIBridgeLoggingFacadeIT extends IntegrationTestCase {

    private CIShellCIBridgeLoggingFacade ciShellCIBridgeLoggingFacade = getCIShellCIBridge().cishellLogging;

    @Before
    public void creatingLogsToTest() {
        getLogService().log(1, "Error Log");
        getLogService().log(2, "Warning Log");
        getLogService().log(3, "Info Log");
        getLogService().log(4, "Debug Log");
    }

    @Test(expected = NullPointerException.class)
    public void validateResultsWithNullFilter() {
        LogFilter filter = null;
        ciShellCIBridgeLoggingFacade.getLogs(filter);
    }

    @Test
    public void validateResultsWithEmptyFilter() {
        LogFilter filter = new LogFilter();
        LogQueryResults logQueryResults = ciShellCIBridgeLoggingFacade.getLogs(filter);
        assertNotNull(logQueryResults);
    }

    @Test
    public void validateResultsWithLoggingFilter() {

        LogFilter filter = new LogFilter();

        List<LogLevel> logLevels = new ArrayList<LogLevel>();
        logLevels.add(LogLevel.ERROR);
        filter.setLogLevel(logLevels);

        // Checking if Error level logs are only returned
        LogQueryResults logQueryResults = ciShellCIBridgeLoggingFacade.getLogs(filter);
        assertNotNull(logQueryResults);
        assertTrue(logQueryResults.getResults().size() >= 1);
        assertEquals(LogLevel.ERROR, logQueryResults.getResults().get(0).getLogLevel());

        HashSet<LogLevel> set = new HashSet<LogLevel>();
        for (Log log : logQueryResults.getResults()) {
            set.add(log.getLogLevel());
        }
        assertTrue(set.size() == 1);

        // Adding WARNING level messages and Checking if both ERROR and WARNING messages
        // are returned
        logLevels.add(LogLevel.WARNING);
        filter.setLogLevel(logLevels);
        logQueryResults = ciShellCIBridgeLoggingFacade.getLogs(filter);
        for (Log log : logQueryResults.getResults()) {
            set.add(log.getLogLevel());
        }
        assertTrue(set.size() == 2);

    }

    @Test
    public void validateResultsWithLogsSinceFilter() {
        LogFilter filter = new LogFilter();

        // Creating a ZonedTime which is a minute earlier to fetch all Logs since the
        // last minute
        Instant i = Instant.now().minusSeconds(60);
        ZonedDateTime z = ZonedDateTime.ofInstant(i, ZoneOffset.systemDefault());
        filter.setLogsSince(z);
        LogQueryResults logQueryResults = ciShellCIBridgeLoggingFacade.getLogs(filter);
        assertNotNull(logQueryResults);
        assertTrue(logQueryResults.getResults().size() >= 1);
        getLogService().log(1, "new log");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        filter = new LogFilter();
        getLogService().log(1, "new log");
        i = Instant.now().minusMillis(1000);
        z = ZonedDateTime.ofInstant(i, ZoneOffset.UTC);
        filter.setLogsSince(z);
        logQueryResults = ciShellCIBridgeLoggingFacade.getLogs(filter);
        assertNotNull(logQueryResults);
//		assertTrue(logQueryResults.getResults().size() == 1);

    }

    @Test
    public void validateResultsWithLogsBeforeFilter() {
        LogFilter filter = new LogFilter();

        // Creating a zoned time with current time to fetch all logs before current time
        Instant i = Instant.now();
        ZonedDateTime z = ZonedDateTime.ofInstant(i, ZoneOffset.UTC);
        filter.setLogsBefore(z);

        LogQueryResults logQueryResults = ciShellCIBridgeLoggingFacade.getLogs(filter);
        assertNotNull(logQueryResults);
        assertTrue(logQueryResults.getResults().size() >= 1);
        assertTrue(z.isBefore(logQueryResults.getResults().get(0).getTimestamp()));
    }

    @Test
    public void validateResultsWithMultipleFilters() {
        LogFilter filter = new LogFilter();

        // Adding multiple filters at Once
        List<LogLevel> logLevels = new ArrayList<LogLevel>();
        logLevels.add(LogLevel.ERROR);
        filter.setLogLevel(logLevels);

        Instant i1 = Instant.now();
        ZonedDateTime z2 = ZonedDateTime.ofInstant(i1, ZoneOffset.UTC);
        filter.setLogsSince(z2);

        getLogService().log(1, "Test Log for unit test cases");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Instant i = Instant.now();
        ZonedDateTime z = ZonedDateTime.ofInstant(i, ZoneOffset.UTC);
        filter.setLogsBefore(z);

        LogQueryResults logQueryResults = ciShellCIBridgeLoggingFacade.getLogs(filter);

        HashSet<LogLevel> set = new HashSet<LogLevel>();
        for (Log log : logQueryResults.getResults()) {
            set.add(log.getLogLevel());
        }

        assertTrue(set.size() == 1);

        logLevels.add(LogLevel.WARNING);
        filter.setLogLevel(logLevels);
        logQueryResults = ciShellCIBridgeLoggingFacade.getLogs(filter);
        for (Log log : logQueryResults.getResults()) {
            set.add(log.getLogLevel());
        }

        assertTrue(set.size() == 1);
        assertNotNull(logQueryResults);
        assertTrue(logQueryResults.getResults().size() == 1);
        assertNotNull(logQueryResults);
        assertTrue(z.isBefore(logQueryResults.getResults().get(0).getTimestamp()));

    }

    @Test
    public void validateLogAddedTests() {

        // Log filter
        List<LogLevel> logLevelList = new ArrayList<>();
        logLevelList.add(LogLevel.DEBUG);
        logLevelList.add(LogLevel.INFO);
        logLevelList.add(LogLevel.ERROR);

        //Setting up a mock Subscriber
        TestSubscriber<Log> testSubscriber = new TestSubscriber<>();
        ciShellCIBridgeLoggingFacade.logAdded(logLevelList).subscribe(testSubscriber);
        testSubscriber.assertNoErrors();

        // Adding Logs for testing
        getLogService().log(1, "Error Log"); //Expected since present in filter
        getLogService().log(2, "Warning Log"); //Not Expected since not present in filter
        getLogService().log(3, "Info Log"); //Expected since present in filter
        getLogService().log(4, "Debug Log"); //Expected since present in filter

        List<String> expectedLogMessages = new ArrayList<>();
        expectedLogMessages.add("Error Log");
        expectedLogMessages.add("Info Log");
        expectedLogMessages.add("Debug Log");

        // Wait till the subscriber collects 3 onNext values
        testSubscriber.awaitCount(3);

        // Getting values from subscriber
        List<Log> resultLogs = testSubscriber.values();
        assertTrue(resultLogs.size()==3);

        // Assert the log messages with the expected results
        for (Log l: resultLogs) {
            assertTrue(expectedLogMessages.contains(l.getMessage()));
        }
    }

}
