package org.cishell.cibridge.model;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public class LogFilter {
	public List<LogLevel> logLevel;
	public Date logsSince;
	public Date logsBefore;
	public int limit;
	public int offset;

	public LogFilter(List<LogLevel> logLevel, Date logsSince, Date logsBefore, int limit, int offset) {
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

	public Date getLogsSince() {
		return logsSince;
	}

	public void setLogsSince(Date logsSince) {
		this.logsSince = logsSince;
	}

	public Date getLogsBefore() {
		return logsBefore;
	}

	public void setLogsBefore(Date logsBefore) {
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
