package org.cishell.cibridge.model;

import java.time.LocalTime;

public class LogFilter{
    LogLevel logLevel;
    LocalTime logsSince;
    LocalTime logsBefore;
    int limit;
    int offset;
}
