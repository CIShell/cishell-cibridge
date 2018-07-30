package org.cishell.cibridge.core.model;

import java.util.List;

public class Notification {
	private final String id;
	private final NotificationType type;
	private final String title;
	private final String message;
	private final String detail;
	private final List<String> stackTrace;
	private final List<ParameterDefinition> formParameters;
	private  Boolean isClosed;
	private  List<PropertyInput> formResponse;
	private  Boolean questionResponse;
	private  Boolean confirmationResponse;

	public Notification(String id, NotificationType type, String title, String message, String detail,
			List<String> stackTrace, List<ParameterDefinition> formParameters, Boolean isClosed,
			List<PropertyInput> formResponse, Boolean questionResponse, Boolean confirmationResponse) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.type = type;
		this.title = title;
		this.message = message;
		this.detail = detail;
		this.stackTrace = stackTrace;
		this.formParameters = formParameters;
		this.isClosed = isClosed;
		this.formResponse = formResponse;
		this.questionResponse = questionResponse;
		this.confirmationResponse = confirmationResponse;
	}

	public String getId() {
		return id;
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

	public List<String> getStackTrace() {
		return stackTrace;
	}

	public List<ParameterDefinition> getFormParameters() {
		return formParameters;
	}

	public Boolean getIsClosed() {
		return isClosed;
	}

	public List<PropertyInput> getFormResponse() {
		return formResponse;
	}

	public Boolean getQuestionResponse() {
		return questionResponse;
	}

	public Boolean getConfirmationResponse() {
		return confirmationResponse;
	}
	public void setClosed(Boolean closed) {
	        isClosed = closed;
	}

	public void setFormResponse(List<PropertyInput> formResponse) {
	        this.formResponse = formResponse;
	}

	public void setQuestionResponse(Boolean questionResponse) {
	        this.questionResponse = questionResponse;
	}

	public void setConfirmationResponse(Boolean confirmationResponse) {
	        this.confirmationResponse = confirmationResponse;
	}

}
