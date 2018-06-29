package org.cishell.cibridge.model;

public class Notification {
    int ID;
    NotificationType type;
    String title;
    String message;
    String detail;
    String stackTrace;
    ParameterDefinition formParameters;
    Boolean isClosed;
    Property formResponse;
    Boolean questionResponse;
    Boolean confirmatipnResponse;
}
