package org.cishell.cibridge.cishell.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.cishell.cibridge.cishell.IntegrationTestCase;
import org.cishell.cibridge.core.model.Log;
import org.cishell.cibridge.core.model.LogFilter;
import org.cishell.cibridge.core.model.LogLevel;
import org.cishell.cibridge.core.model.LogQueryResults;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
		System.out.println(logQueryResults.getResults().size());
		for (Log log : logQueryResults.getResults()) {
			System.out.println(log.getTimestamp());
		}
		assertTrue(logQueryResults.getResults().size() >= 1);
	}

	@Test
	public void validateResultsWithLogsBeforeFilter() {
		LogFilter filter = new LogFilter();

		// Creating a zoned time with current time to fetch all logs before current time
		Instant i = Instant.now();
		ZonedDateTime z = ZonedDateTime.ofInstant(i, ZoneOffset.systemDefault());
		filter.setLogsBefore(z);

		LogQueryResults logQueryResults = ciShellCIBridgeLoggingFacade.getLogs(filter);
		assertNotNull(logQueryResults);
		assertTrue(logQueryResults.getResults().size() >= 1);
		assertTrue(z.isBefore(logQueryResults.getResults().get(0).getTimestamp()));
	}

	@Test
	public void validateResultsWithMutlipleFilters() {

		LogFilter filter = new LogFilter();

		// Adding multiple filters at Once
		List<LogLevel> logLevels = new ArrayList<LogLevel>();
		logLevels.add(LogLevel.ERROR);
		filter.setLogLevel(logLevels);

		Instant i = Instant.now();
		ZonedDateTime z = ZonedDateTime.ofInstant(i, ZoneOffset.systemDefault());
		filter.setLogsBefore(z);

		Instant i1 = Instant.now().minusSeconds(60);
		ZonedDateTime z2 = ZonedDateTime.ofInstant(i1, ZoneOffset.systemDefault());
		filter.setLogsSince(z2);

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
		assertTrue(set.size() == 2);

		assertNotNull(logQueryResults);

		for (Log log : logQueryResults.getResults()) {
			System.out.println(log.getTimestamp());
		}
		assertTrue(logQueryResults.getResults().size() >= 1);

		assertNotNull(logQueryResults);
		assertTrue(logQueryResults.getResults().size() >= 1);
		assertTrue(z.isBefore(logQueryResults.getResults().get(0).getTimestamp()));

	}

	@After
	public void tearDown() {

	}
}