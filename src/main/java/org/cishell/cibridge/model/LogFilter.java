package org.cishell.cibridge.model;

import java.time.LocalTime;

public class LogFilter{
	public LogLevel logLevel;
	public LocalTime logsSince;
	public LocalTime logsBefore;
	public int limit;
	public int offset;
    
    public LogFilter(LogLevel logLevel,LocalTime logsSince,LocalTime logsBefore,int limit,int offset) {
		// TODO Auto-generated constructor stub
    	this.logLevel=logLevel;
    	this.logsSince=logsSince;
    	this.logsBefore=logsBefore;
    	this.limit=limit;
    	this.offset=offset;
	}

	public LogLevel getLogLevel() {
		return logLevel;
	}

	public LocalTime getLogsSince() {
		return logsSince;
	}

	public LocalTime getLogsBefore() {
		return logsBefore;
	}

	public int getLimit() {
		return limit;
	}

	public int getOffset() {
		return offset;
	}

	public void setLogLevel(LogLevel logLevel) {
		this.logLevel = logLevel;
	}

	public void setLogsSince(LocalTime logsSince) {
		this.logsSince = logsSince;
	}

	public void setLogsBefore(LocalTime logsBefore) {
		this.logsBefore = logsBefore;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}
	
    
}
