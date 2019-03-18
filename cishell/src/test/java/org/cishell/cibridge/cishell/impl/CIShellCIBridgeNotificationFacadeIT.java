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

public class CIShellCIBridgeNotificationFacadeIT extends IntegrationTestCase {

    private CIShellCIBridgeNotificationFacade ciShellCIBridgeNotificationFacade = getCIShellCIBridge().cishellNotification;
    private CIBridgeGUIBuilderService ciBridgeGUIBuilderService = (CIBridgeGUIBuilderService) getCIShellCIBridge().getGUIBuilderService();

    @Test
    public void validateGetNotificationsWithOutFilter() {

    }

    @Test
    public void validateisClosedNotification() {

    }

    @Test
    public void validateSetNotificationResponse() {

    }

    @Test
    public void validateCloseNotification() {

    }

    @Test
    public void validateNotificationAddedSubscriber() {

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
    public void validateNotificationUpdatedSubscriber() {

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

}
