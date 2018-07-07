package org.cishell.cibridge.model;

import java.time.LocalTime;

public class LogFilter{
	public final LogLevel logLevel;
	public final LocalTime logsSince;
	public final LocalTime logsBefore;
	public final int limit;
	public final int offset;
    
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
    
}
