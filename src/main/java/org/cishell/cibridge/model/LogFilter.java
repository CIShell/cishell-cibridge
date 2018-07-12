package org.cishell.cibridge.model;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

public class LogFilter {
	public List<LogLevel> logLevel;
	public ZonedDateTime logsSince;
	public ZonedDateTime logsBefore;
	public int limit;
	public int offset;

	public LogFilter(List<LogLevel> logLevel, ZonedDateTime logsSince, ZonedDateTime logsBefore, int limit, int offset) {
		// TODO Auto-generated constructor stub
		this.logLevel = logLevel;
		this.logsSince = logsSince;
		this.logsBefore = logsBefore;
		this.limit = limit;
		this.offset = offset;
	}

	public List<LogLevel> getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(List<LogLevel> logLevel) {
		this.logLevel = logLevel;
	}

	public ZonedDateTime getLogsSince() {
		return logsSince;
	}

	public void setLogsSince(ZonedDateTime logsSince) {
		this.logsSince = logsSince;
	}

	public ZonedDateTime getLogsBefore() {
		return logsBefore;
	}

	public void setLogsBefore(ZonedDateTime logsBefore) {
		this.logsBefore = logsBefore;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

}
