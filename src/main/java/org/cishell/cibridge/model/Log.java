package org.cishell.cibridge.model;

import java.util.Date;
import java.util.List;

public class Log {
	private final LogLevel loglevel;
	private final String message;
	private final List<String> stackTrace;
	private final Date time;

	public Log(LogLevel loglevel, String message, List<String> stackTrace, Date time) {
		// TODO Auto-generated constructor stub
		this.loglevel = loglevel;
		this.message = message;
		this.stackTrace = stackTrace;
		this.time = time;
	}

	public LogLevel getLoglevel() {
		return loglevel;
	}

	public String getMessage() {
		return message;
	}

	public List<String> getStackTrace() {
		return stackTrace;
	}

	public Date getTime() {
		return time;
	}
}
