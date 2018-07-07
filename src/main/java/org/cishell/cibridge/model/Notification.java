package org.cishell.cibridge.model;

public class Notification {
	private final String ID;
	private final NotificationType type;
	private final String title;
	private final String message;
	private final String detail;
	private final String stackTrace;
	private final ParameterDefinition formParameters;
	private final Boolean isClosed;
	private final Property formResponse;
	private final Boolean questionResponse;
	private final Boolean confirmatipnResponse;
    
    public Notification(String ID,NotificationType type,String title,String message,String detail,String stackTrace,ParameterDefinition formParameters,Boolean isClosed,Property formResponse,Boolean questionResponse,Boolean confirmatipnResponse) {
		// TODO Auto-generated constructor stub
    	this.ID=ID;
    	this.type=type;
    	this.title=title;
    	this.message=message;
    	this.detail=detail;
    	this.stackTrace=stackTrace;
    	this.formParameters=formParameters;
    	this.isClosed=isClosed;
    	this.formResponse=formResponse;
    	this.questionResponse=questionResponse;
    	this.confirmatipnResponse=confirmatipnResponse;
	}
    
    public Notification(NotificationType type,String title,String message,String detail,String stackTrace,ParameterDefinition formParameters,Boolean isClosed,Property formResponse,Boolean questionResponse,Boolean confirmatipnResponse) {
		// TODO Auto-generated constructor stub
    	this.ID=null;
    	this.type=type;
    	this.title=title;
    	this.message=message;
    	this.detail=detail;
    	this.stackTrace=stackTrace;
    	this.formParameters=formParameters;
    	this.isClosed=isClosed;
    	this.formResponse=formResponse;
    	this.questionResponse=questionResponse;
    	this.confirmatipnResponse=confirmatipnResponse;
	}

	public String getID() {
		return ID;
	}

	public NotificationType getType() {
		return type;
	}

	public String getTitle() {
		return title;
	}

	public String getMessage() {
		return message;
	}

	public String getDetail() {
		return detail;
	}

	public String getStackTrace() {
		return stackTrace;
	}

	public ParameterDefinition getFormParameters() {
		return formParameters;
	}

	public Boolean getIsClosed() {
		return isClosed;
	}

	public Property getFormResponse() {
		return formResponse;
	}

	public Boolean getQuestionResponse() {
		return questionResponse;
	}

	public Boolean getConfirmatipnResponse() {
		return confirmatipnResponse;
	}
    
}
