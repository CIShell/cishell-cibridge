package org.cishell.cibridge.core.model;

import java.time.ZonedDateTime;
import java.util.List;

public class Log {
    private LogLevel logLevel;
    private String message;
    private List<String> stackTrace;
    private ZonedDateTime timestamp;

    public Log() {
    }

    public Log(LogLevel logLevel, String message, List<String> stackTrace, ZonedDateTime timestamp) {
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

    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStackTrace(List<String> stackTrace) {
        this.stackTrace = stackTrace;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

}
