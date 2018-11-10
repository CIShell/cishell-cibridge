package org.cishell.cibridge.core.model;

import java.util.List;


public class NotificationResponse {
    protected List<PropertyInput> formResponse;
    private Boolean questionResponse;
    private Boolean confirmationResponse;
    private Boolean closeNotification;

    public NotificationResponse(List<PropertyInput> formResponse, Boolean questionResponse,
                                Boolean confirmationResponse, Boolean closeNotification) {
        // TODO Auto-generated constructor stub
        this.formResponse = formResponse;
        this.questionResponse = questionResponse;
        this.confirmationResponse = confirmationResponse;
        this.closeNotification = closeNotification;
    }

    public List<PropertyInput> getFormResponse() {
        return formResponse;
    }

    public void setFormResponse(List<PropertyInput> formResponse) {
        this.formResponse = formResponse;
    }

    public Boolean getQuestionResponse() {
        return questionResponse;
    }

    public void setQuestionResponse(Boolean questionResponse) {
        this.questionResponse = questionResponse;
    }

    public Boolean getConfirmationResponse() {
        return confirmationResponse;
    }

    public void setConfirmationResponse(Boolean confirmationResponse) {
        this.confirmationResponse = confirmationResponse;
    }

    public Boolean getCloseNotification() {
        return closeNotification;
    }

    public void setCloseNotification(Boolean closeNotification) {
        this.closeNotification = closeNotification;
    }

}
