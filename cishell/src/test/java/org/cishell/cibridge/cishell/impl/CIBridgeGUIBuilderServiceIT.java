package org.cishell.cibridge.cishell.impl;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.subscribers.TestSubscriber;
import org.cishell.cibridge.cishell.IntegrationTestCase;
import org.cishell.cibridge.core.model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class CIBridgeGUIBuilderServiceIT extends IntegrationTestCase {

    private CIShellCIBridgeNotificationFacade ciShellCIBridgeNotificationFacade = getCIShellCIBridge().cishellNotification;
    private CIBridgeGUIBuilderService ciBridgeGUIBuilderService = (CIBridgeGUIBuilderService) getCIShellCIBridge().getGUIBuilderService();

    //TODO add more tests for checking mutation

    @Test
    public void validateNotifictaionAddedforAddingConfirmMessage() {

        TestSubscriber<Notification> testSubscriber = new TestSubscriber<>();
        ciShellCIBridgeNotificationFacade.notificationAdded().subscribe(testSubscriber);

        String title = "Test Confirm Notification";
        String message = "Confirm Type of notification";
        String detail = "Yes or no option";

        ciBridgeGUIBuilderService.showConfirm(title, message, detail);

        testSubscriber.awaitCount(1);
        testSubscriber.assertNoErrors();

        List<Notification> notificationList = testSubscriber.values();
        System.out.println(notificationList.size());
        for (Notification notification: notificationList) {
            System.out.println(notification);
        }

    }

    @Test
    public void validateNotifictaionAddedforAddingErrorMessage() {

        TestSubscriber<Notification> testNotiAddedSubscriber = new TestSubscriber<>();
        ciShellCIBridgeNotificationFacade.notificationAdded().subscribe(testNotiAddedSubscriber);

        String title = "Test Error Notification";
        String message = "Error Type of notification";
        String detail = "Detailed option for error message";

        ciBridgeGUIBuilderService.showError(title, message, detail);

        testNotiAddedSubscriber.awaitCount(1);
        testNotiAddedSubscriber.assertNoErrors();

        List<Notification> notificationList = testNotiAddedSubscriber.values();
        System.out.println(notificationList.size());
        for (Notification notification : notificationList) {
            System.out.println(notification);
        }

        NotificationResponse notificationResponse = new NotificationResponse(null, false, false, true);

        TestSubscriber<Notification> testNotiUpdatedSubscriber = new TestSubscriber<>();
        ciShellCIBridgeNotificationFacade.notificationUpdated().subscribe(testNotiUpdatedSubscriber);
        ciShellCIBridgeNotificationFacade.setNotificationResponse(notificationList.get(0).getId(), notificationResponse);

        testNotiUpdatedSubscriber.awaitCount(1);
        testNotiUpdatedSubscriber.assertNoErrors();

    }

    @Test
    public void validateNotifictaionAddedforAddingInformationMessage() {

        TestSubscriber<Notification> testSubscriber = new TestSubscriber<>();
        ciShellCIBridgeNotificationFacade.notificationAdded().subscribe(testSubscriber);

        String title = "Test Information Notification";
        String message = "Information Type of notification";
        String detail = "Detailed option for information message";

        ciBridgeGUIBuilderService.showInformation(title, message, detail);

        testSubscriber.awaitCount(1);
        testSubscriber.assertNoErrors();

        List<Notification> notificationList = testSubscriber.values();
        System.out.println(notificationList.size());
        for (Notification notification: notificationList) {
            System.out.println(notification);
        }

    }

    @Test
    public void validateNotifictaionAddedforAddingQuestionMessage() {

        TestSubscriber<Notification> testSubscriber = new TestSubscriber<>();
        ciShellCIBridgeNotificationFacade.notificationAdded().subscribe(testSubscriber);

        String title = "Test Question Notification";
        String message = "Question Type of notification";
        String detail = "Detailed option for question message";

        ciBridgeGUIBuilderService.showQuestion(title, message, detail);

        testSubscriber.awaitCount(1);
        testSubscriber.assertNoErrors();

        List<Notification> notificationList = testSubscriber.values();
        System.out.println(notificationList.size());
        for (Notification notification: notificationList) {
            System.out.println(notification);
        }

    }

    @Test
    public void validateNotifictaionAddedforAddingWarningMessage() {
        TestSubscriber<Notification> testSubscriber = new TestSubscriber<>();
        ciShellCIBridgeNotificationFacade.notificationAdded().subscribe(testSubscriber);

        String title = "Test Warning Notification";
        String message = "Warning Type of notification";
        String detail = "Detailed option for warning message";

        ciBridgeGUIBuilderService.showWarning(title, message, detail);

        testSubscriber.awaitCount(1);
        testSubscriber.assertNoErrors();

        List<Notification> notificationList = testSubscriber.values();
        System.out.println(notificationList.size());
        for (Notification notification: notificationList) {
            System.out.println(notification);
        }
    }

    @Test
    public void validateOtherPropertiesOfAlgorithmDefinition() {

    }

    @Test
    public void getAlgorithmDefinitionsWithEmptyFilter() {

    }

    @Test
    public void getAlgorithmDefinitionsWithSpecifiedAlgorithmDefinitionIDs() {

    }

}
