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
		ArrayList<LogEntry> listOfLogs;
		List<Predicate<LogEntry>> criteria = new ArrayList<>();
		HashMap<Integer, LogLevel> logLevelMap = new HashMap<>();
		logLevelMap.put(1, LogLevel.ERROR);
		logLevelMap.put(2, LogLevel.WARNING);
		logLevelMap.put(3, LogLevel.INFO);
		logLevelMap.put(4, LogLevel.DEBUG);

		try {
			LogReaderService logReaderService = getLogReaderService();
			listOfLogs = Collections.list(logReaderService.getLog());

			criteria.add(data -> {
				if (data == null)
					return false;
				if (filter.getLogLevel() == null)
					return true;
				return filter.getLogLevel().contains(logLevelMap.get(data.getLevel()));
			});

			criteria.add(data -> {
				if (data == null)
					return false;
				if (filter.getLogsBefore() == null)
					return true;
				else {
					Instant i = Instant.ofEpochMilli(data.getTime());
					ZonedDateTime logCreatedTIme = ZonedDateTime.ofInstant(i, ZoneOffset.systemDefault());
					return filter.getLogsBefore().isAfter(logCreatedTIme);
				}

			});

			criteria.add(data -> {
				if (data == null)
					return false;
				if (filter.getLogsSince() == null)
					return true;
				else {
					Instant i = Instant.ofEpochMilli(data.getTime());
					ZonedDateTime logCreatedTIme = ZonedDateTime.ofInstant(i, ZoneOffset.systemDefault());
					return filter.getLogsSince().isBefore(logCreatedTIme);
				}

			});

			QueryResults<LogEntry> paginatedQueryResults = PaginationUtil.getPaginatedResults(listOfLogs, criteria,
					filter.getOffset(), filter.getLimit());

			List<Log> log = new ArrayList<Log>();
			for (LogEntry ll : paginatedQueryResults.getResults()) {
				Log temp = new Log();
				temp.setLogLevel(logLevelMap.get(ll.getLevel()));
				temp.setMessage(ll.getMessage());
				List<String> stacktrace = new ArrayList<String>();
				if (ll.getException() != null) {
					StackTraceElement[] stackTraceArray = ll.getException().getStackTrace();
					if (stackTraceArray != null) {
						for (StackTraceElement e : stackTraceArray) {
							stacktrace.add(e.toString());
						}
						temp.setStackTrace(stacktrace);
					} else {
						temp.setStackTrace(null);
					}
				} else {
					temp.setStackTrace(null);
				}
				Instant i = Instant.ofEpochSecond(ll.getTime());
				ZonedDateTime z = ZonedDateTime.ofInstant(i, ZoneOffset.UTC);
				temp.setTimestamp(z);
				log.add(temp);
			}

			return new LogQueryResults(log, paginatedQueryResults.getPageInfo());

		} catch (Exception e) {
			System.out.println("LogReader service returned null");
			e.printStackTrace();
		}

		return results;

	}

	// TODO use log service listener for subscriptions
	@Override
	public Log logAdded(List<LogLevel> logLevels) {
		// TODO Auto-generated method stub
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