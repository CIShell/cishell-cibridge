package org.cishell.cibridge.model;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

public class Log {
	private final LogLevel logLevel;
	private final String message;
	private final List<String> stackTrace;
	private final ZonedDateTime timestamp;

	public Log(LogLevel logLevel, String message, List<String> stackTrace, ZonedDateTime timestamp) {
		// TODO Auto-generated constructor stub
		this.logLevel = logLevel;
		this.message = message;
		this.stackTrace = stackTrace;
		this.timestamp = timestamp;
	}

	public LogLevel getLogLevel() {
		return logLevel;
	}

	public String getMessage() {
		return message;
	}

	public List<String> getStackTrace() {
		return stackTrace;
	}

	public ZonedDateTime getTimestamp() {
		return timestamp;
	}

}