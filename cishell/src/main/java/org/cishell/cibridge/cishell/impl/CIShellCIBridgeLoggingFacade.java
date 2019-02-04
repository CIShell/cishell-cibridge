package org.cishell.cibridge.cishell.impl;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.cibridge.cishell.util.PaginationUtil;
import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.model.Log;
import org.cishell.cibridge.core.model.LogFilter;
import org.cishell.cibridge.core.model.LogLevel;
import org.cishell.cibridge.core.model.LogQueryResults;
import org.cishell.cibridge.core.model.QueryResults;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogReaderService;

import com.google.common.base.Preconditions;

public class CIShellCIBridgeLoggingFacade implements CIBridge.LoggingFacade {
	private CIShellCIBridge cibridge;

	public void setCIBridge(CIShellCIBridge cibridge) {
		this.cibridge = cibridge;
	}

	@SuppressWarnings("unchecked")
	@Override
	public LogQueryResults getLogs(LogFilter filter) {

		Preconditions.checkNotNull(filter, "filter can't be empty");

		LogQueryResults results = null;
		ArrayList<LogEntry> listOfLogEntries;
		List<Predicate<LogEntry>> criteria = new ArrayList<>();

		// Maps integers with LogLevel
		HashMap<Integer, LogLevel> logLevelMap = new HashMap<>();
		logLevelMap.put(1, LogLevel.ERROR);
		logLevelMap.put(2, LogLevel.WARNING);
		logLevelMap.put(3, LogLevel.INFO);
		logLevelMap.put(4, LogLevel.DEBUG);

		try {

			LogReaderService logReaderService = getLogReaderService();
			// Getting the Logs from service
			listOfLogEntries = Collections.list(logReaderService.getLog());

			// Adding LogLevel to Criteria
			criteria.add(data -> {
				if (data == null)
					return false;
				if (filter.getLogLevel() == null)
					return true;
				return filter.getLogLevel().contains(logLevelMap.get(data.getLevel()));
			});

			// Adding LogsBefore timestamp to the criteria
			criteria.add(data -> {
				if (data == null)
					return false;
				if (filter.getLogsBefore() == null)
					return true;
				else {
					return filter.getLogsBefore().toInstant().toEpochMilli()>data.getTime();
				}
			});

			// Adding LogsSince timestamp to the criteria
			criteria.add(data -> {
				if (data == null)
					return false;
				if (filter.getLogsSince() == null)
					return true;
				else {
					return filter.getLogsSince().toInstant().toEpochMilli()<data.getTime();
				}

			});

			// Paginating the results
			QueryResults<LogEntry> paginatedQueryResults = PaginationUtil.getPaginatedResults(listOfLogEntries,
					criteria, filter.getOffset(), filter.getLimit());

			// Converting the LogLevel to List Of Logs
			List<Log> listOfLogs = new ArrayList<Log>();
			for (LogEntry logEntry : paginatedQueryResults.getResults()) {
				Log log = new Log();
				log.setLogLevel(logLevelMap.get(logEntry.getLevel()));
				log.setMessage(logEntry.getMessage());
				List<String> stacktrace = new ArrayList<String>();
				// Adding Stacktrace if it exists
				if (logEntry.getException() != null) {
					StackTraceElement[] stackTraceArray = logEntry.getException().getStackTrace();
					if (stackTraceArray != null) {
						for (StackTraceElement e : stackTraceArray) {
							stacktrace.add(e.toString());
						}
						log.setStackTrace(stacktrace);
					} else {
						log.setStackTrace(null);
					}
				} else {
					log.setStackTrace(null);
				}
				Instant logEntryInstant = Instant.ofEpochSecond(logEntry.getTime());
				ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(logEntryInstant, ZoneOffset.UTC);
				log.setTimestamp(zonedDateTime);
				listOfLogs.add(log);
			}

			return new LogQueryResults(listOfLogs, paginatedQueryResults.getPageInfo());

		} catch (Exception e) {
			System.out.println("LogReader service returned null");
			e.printStackTrace();
		}

		return results;

	}

	// TODO use log service listener for subscriptions
	@Override
	public Log logAdded(List<LogLevel> logLevels) {
		return null;
	}

	private LogReaderService getLogReaderService() {
		ServiceReference<LogReaderService> ref = this.cibridge.getBundleContext()
				.getServiceReference(LogReaderService.class);
		if (ref == null) {
			// TODO why are we throwing a runtime exception here? it could break the
			// application.
			throw new RuntimeException("The required OSGi LogService is not installed.");
		} else {
			return (LogReaderService) this.cibridge.getBundleContext().getService(ref);
		}
	}
}