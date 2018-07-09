package org.cishell.cibridge.model;

public class NotificationResponse {
	private final PropertyInput formResponse;
	private final Boolean questionResponse;
	private final Boolean confirmationResponse;
	private final Boolean closeNotification;

	public NotificationResponse(PropertyInput formResponse, Boolean questionResponse, Boolean confirmationResponse,
			Boolean closeNotification) {
		// TODO Auto-generated constructor stub
		this.formResponse = formResponse;
		this.questionResponse = questionResponse;
		this.confirmationResponse = confirmationResponse;
		this.closeNotification = closeNotification;
	}

	public PropertyInput getFormResponse() {
		return formResponse;
	}

	public Boolean getQuestionResponse() {
		return questionResponse;
	}

	public Boolean getConfirmationResponse() {
		return confirmationResponse;
	}

	public Boolean getCloseNotification() {
		return closeNotification;
	}

}
