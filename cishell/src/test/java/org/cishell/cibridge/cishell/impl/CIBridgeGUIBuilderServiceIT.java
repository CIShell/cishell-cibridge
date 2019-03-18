package org.cishell.cibridge.cishell.impl;

import io.reactivex.observers.BaseTestConsumer;
import io.reactivex.subscribers.TestSubscriber;
import org.cishell.cibridge.cishell.IntegrationTestCase;
import org.cishell.cibridge.core.model.*;
import org.cishell.service.guibuilder.GUI;
import org.junit.Test;

import java.security.acl.NotOwnerException;
import java.util.*;

import static org.junit.Assert.*;

public class CIBridgeGUIBuilderServiceIT extends IntegrationTestCase {

    private CIShellCIBridgeNotificationFacade ciShellCIBridgeNotificationFacade = getCIShellCIBridge().cishellNotification;
    private CIBridgeGUIBuilderService ciBridgeGUIBuilderService = (CIBridgeGUIBuilderService) getCIShellCIBridge().getGUIBuilderService();

    @Test
    public void validateCreateGUIOpenWithoutParams() {

        Map<String, Notification> map = ciShellCIBridgeNotificationFacade.getNotificationMap();
        String id = "NotificationOpenWithoutParamsID";
        GUI gui = ciBridgeGUIBuilderService.createGUI(id, null);
        // Should create a notification and add it to map.
        gui.open();
        assertTrue(map.containsKey(id));

        Notification notification = map.get(id);

        // Verify if the notification object getting created is having desired values in the fields
        assertEquals(id, notification.getId());
        assertEquals(NotificationType.FORM, notification.getType());
        assertNull(notification.getFormResponse());
        assertFalse(notification.getConfirmationResponse());
        assertFalse(notification.getQuestionResponse());
        assertNull(notification.getStackTrace());
        assertNull(notification.getTitle());
        assertNull(notification.getMessage());
        assertNull(notification.getDetail());
        assertNull(notification.getFormParameters());
    }

    @Test
    public void validateCreateGUIOpenWithParams() {

        // FIXME Not sure how to pass params. Pass params and assert the values of params passed are being filled in the notification form
        Map<String, Notification> map = ciShellCIBridgeNotificationFacade.getNotificationMap();
        String id = "NotificationOpenWithParamsID";
        GUI gui = ciBridgeGUIBuilderService.createGUI(id, null);
        // Should create a notification and add it to map.
        gui.open();
        assertTrue(map.containsKey(id));

        Notification notification = map.get(id);

        // Verify if the notification object getting created is having desired values in the fields
        assertEquals(id, notification.getId());
        assertEquals(NotificationType.FORM, notification.getType());
        assertNull(notification.getFormResponse());
        assertFalse(notification.getConfirmationResponse());
        assertFalse(notification.getQuestionResponse());
        assertNull(notification.getStackTrace());
        assertNull(notification.getTitle());
        assertNull(notification.getMessage());
        assertNull(notification.getDetail());

        //TODO Fetch Form parameters and compare parameters of each entry


    }

    @Test
    public void validateCreateGUIOpenAndWaitWithoutParams() {

        Map<String, Notification> map = ciShellCIBridgeNotificationFacade.getNotificationMap();
        String id = "NotificationOpenAndWaitWithoutParamsID";
        GUI gui = ciBridgeGUIBuilderService.createGUI(id, null);

        HashSet<String> propertyKeys = new HashSet<>();
        HashSet<String> propertyValues = new HashSet<>();
        propertyKeys.addAll(Arrays.asList("key1", "key2", "key3"));
        propertyValues.addAll(Arrays.asList("value1", "value2", "value3"));

        PropertyInput property = new PropertyInput("key1", "value1");
        PropertyInput property1 = new PropertyInput("key2", "value2");
        PropertyInput property2 = new PropertyInput("key3", "value3");

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // Should create a notification and add it to map and wait for response.
                gui.openAndWait();

                assertTrue(map.containsKey(id));
                Notification notification = map.get(id);
                // Verify if the notification object getting created is having desired values in the fields
                assertEquals(id, notification.getId());
                assertEquals(NotificationType.FORM, notification.getType());
                assertNotNull(notification.getFormResponse());
                assertFalse(notification.getConfirmationResponse());
                assertFalse(notification.getQuestionResponse());
                assertNull(notification.getStackTrace());
                assertNull(notification.getTitle());
                assertNull(notification.getMessage());
                assertNull(notification.getDetail());
                assertNull(notification.getFormParameters());

                //Comparing Form response
                List<Property> formResponse = notification.getFormResponse();
                assertEquals(propertyKeys.size(), formResponse.size());
                for (Property p : formResponse) {
                    assertTrue(propertyKeys.contains(p.getKey()));
                    assertTrue(propertyValues.contains(p.getValue()));
                }

            }
        });

        thread.start();

        List<PropertyInput> formResponse = new ArrayList<>();
        formResponse.add(property);
        formResponse.add(property1);
        formResponse.add(property2);

        NotificationResponse notificationResponse = new NotificationResponse(formResponse, false, false, false);

        ciShellCIBridgeNotificationFacade.setNotificationResponse(id, notificationResponse);

    }

    @Test
    public void validateCreateGUIOpenAndWaitWithParams() {

        Map<String, Notification> map = ciShellCIBridgeNotificationFacade.getNotificationMap();
        String id = "NotificationOpenAndWaitWithoutParamsID";

        // FIXME Not sure how to pass params. Pass params and assert the values of params passed are being filled in the notification form
        GUI gui = ciBridgeGUIBuilderService.createGUI(id, null);

        HashSet<String> propertyKeys = new HashSet<>();
        HashSet<String> propertyValues = new HashSet<>();
        propertyKeys.addAll(Arrays.asList("key1", "key2", "key3"));
        propertyValues.addAll(Arrays.asList("value1", "value2", "value3"));

        PropertyInput property = new PropertyInput("key1", "value1");
        PropertyInput property1 = new PropertyInput("key2", "value2");
        PropertyInput property2 = new PropertyInput("key3", "value3");

        Thread createNotificationThread = new Thread(new Runnable() {
            @Override
            public void run() {
                gui.openAndWait();
            }
        });

        Thread notificationResponseThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // Should create a notification and add it to map and wait for response.
                List<PropertyInput> formResponse = new ArrayList<>();
                formResponse.add(property);
                formResponse.add(property1);
                formResponse.add(property2);
                NotificationResponse notificationResponse = new NotificationResponse(formResponse, false, false, false);
                ciShellCIBridgeNotificationFacade.setNotificationResponse(id, notificationResponse);

            }
        });

        createNotificationThread.start();
        notificationResponseThread.start();

        try {
            Thread.sleep(50);
        } catch (Exception e){
            e.printStackTrace();
        }

        assertTrue(map.containsKey(id));
        Notification notification = map.get(id);

        // Verify if the notification object getting created is having desired values in the fields
        assertEquals(id, notification.getId());
        assertEquals(NotificationType.FORM, notification.getType());
        assertNotNull(notification.getFormResponse());
        assertFalse(notification.getConfirmationResponse());
        assertFalse(notification.getQuestionResponse());
        assertNull(notification.getStackTrace());
        assertNull(notification.getTitle());
        assertNull(notification.getMessage());
        assertNull(notification.getDetail());
        assertNull(notification.getFormParameters());

        //Comparing Form response
        List<Property> formResponse = notification.getFormResponse();
        assertEquals(propertyKeys.size(), formResponse.size());
        for (Property p : formResponse) {
            assertTrue(propertyKeys.contains(p.getKey()));
            assertTrue(propertyValues.contains(p.getValue()));
        }

    }

    @Test
    public void validateshowConfirmGUI() {

        Map<String, Notification> map = ciShellCIBridgeNotificationFacade.getNotificationMap();
        TestSubscriber<Notification> testSubscriber = new TestSubscriber<>();
        ciShellCIBridgeNotificationFacade.notificationAdded().subscribe(testSubscriber);

        String expectedTitle = "Dummy Notification Title";
        String expectedMessage = "Dummy Notification Message";
        String expectedDetail = "Dummy Notification Details";
        Boolean expectedConfirmationRespone = true;

        Thread createNotificationThread = new Thread(new Runnable() {
            @Override
            public void run() {
                ciBridgeGUIBuilderService.showConfirm(expectedTitle, expectedMessage, expectedDetail);
            }
        });

        createNotificationThread.start();

        try {
            Thread.sleep(50);
        } catch (Exception e){
            e.printStackTrace();
        }

        testSubscriber.awaitCount(1);
        List<Notification> notificationList = testSubscriber.values();
        Notification expectedNotification = notificationList.get(0);

        NotificationResponse notificationResponse = new NotificationResponse(null, false, expectedConfirmationRespone, false);
        ciShellCIBridgeNotificationFacade.setNotificationResponse(expectedNotification.getId(), notificationResponse);

        assertTrue(map.containsKey(expectedNotification.getId()));

        Notification actualNotification = map.get(expectedNotification.getId());

        // Verify if the notification object getting created is having desired values in the fields
        assertEquals(expectedNotification.getId(), actualNotification.getId());
        assertEquals(NotificationType.CONFIRM, actualNotification.getType());
        assertNull(actualNotification.getFormResponse());
        assertFalse(actualNotification.getQuestionResponse());
        assertNull(actualNotification.getStackTrace());
        assertEquals(expectedMessage, actualNotification.getMessage());
        assertEquals(expectedTitle, actualNotification.getTitle());
        assertEquals(expectedDetail, actualNotification.getDetail());
        assertNull(actualNotification.getFormParameters());

        //Comparing Form response
        assertEquals(expectedConfirmationRespone, actualNotification.getConfirmationResponse());

    }

    @Test
    public void validateshowErrorGUI() {

        Map<String, Notification> map = ciShellCIBridgeNotificationFacade.getNotificationMap();
        TestSubscriber<Notification> testSubscriber = new TestSubscriber<>();
        ciShellCIBridgeNotificationFacade.notificationAdded().subscribe(testSubscriber);

        String expectedTitle = "Dummy Notification Title";
        String expectedMessage = "Dummy Notification Message";
        String expectedDetail = "Dummy Notification Details";

        ciBridgeGUIBuilderService.showError(expectedTitle, expectedMessage, expectedDetail);

        testSubscriber.awaitCount(1);
        List<Notification> notificationList = testSubscriber.values();
        Notification expectedNotification = notificationList.get(0);

        assertTrue(map.containsKey(expectedNotification.getId()));

        Notification actualNotification = map.get(expectedNotification.getId());

        // Verify if the notification object getting created is having desired values in the fields
        assertEquals(expectedNotification.getId(), actualNotification.getId());
        assertEquals(NotificationType.ERROR, actualNotification.getType());
        assertNull(actualNotification.getFormResponse());
        assertFalse(actualNotification.getQuestionResponse());
        assertNull(actualNotification.getStackTrace());
        assertEquals(expectedMessage, actualNotification.getMessage());
        assertEquals(expectedTitle, actualNotification.getTitle());
        assertEquals(expectedDetail, actualNotification.getDetail());
        assertNull(actualNotification.getFormParameters());
        assertFalse(actualNotification.getConfirmationResponse());

    }

    @Test
    public void validateshowErrorWithThrowableGUI() {

        Map<String, Notification> map = ciShellCIBridgeNotificationFacade.getNotificationMap();
        TestSubscriber<Notification> testSubscriber = new TestSubscriber<>();
        ciShellCIBridgeNotificationFacade.notificationAdded().subscribe(testSubscriber);

        String expectedTitle = "Test Error Notification";
        String expectedMessage = "Error Type of notification";
        Exception expectedException = new ArrayIndexOutOfBoundsException();

        ciBridgeGUIBuilderService.showError(expectedTitle, expectedMessage, expectedException);

        testSubscriber.awaitCount(1);
        List<Notification> notificationList = testSubscriber.values();
        Notification expectedNotification = notificationList.get(0);

        assertTrue(map.containsKey(expectedNotification.getId()));

        Notification actualNotification = map.get(expectedNotification.getId());

        // Verify if the notification object getting created is having desired values in the fields
        assertEquals(expectedNotification.getId(), actualNotification.getId());
        assertEquals(NotificationType.ERROR, actualNotification.getType());
        assertNull(actualNotification.getFormResponse());
        assertFalse(actualNotification.getQuestionResponse());
        assertNotNull(actualNotification.getStackTrace());
        assertEquals(expectedMessage, actualNotification.getMessage());
        assertEquals(expectedTitle, actualNotification.getTitle());
        assertNull(actualNotification.getDetail());
        assertNull(actualNotification.getFormParameters());
        assertFalse(actualNotification.getConfirmationResponse());

        List<String> actualStacktrace = actualNotification.getStackTrace();
        StackTraceElement[] stackTraceElements = expectedException.getStackTrace();
        for(int i=0; i<stackTraceElements.length; i++){
            assertEquals(stackTraceElements[i].toString(), actualStacktrace.get(i));
        }
    }

    @Test
    public void validateshowInformationGUI() {

        Map<String, Notification> map = ciShellCIBridgeNotificationFacade.getNotificationMap();
        TestSubscriber<Notification> testSubscriber = new TestSubscriber<>();
        ciShellCIBridgeNotificationFacade.notificationAdded().subscribe(testSubscriber);

        String expectedTitle = "Dummy Notification Title";
        String expectedMessage = "Dummy Notification Message";
        String expectedDetail = "Dummy Notification Details";

        ciBridgeGUIBuilderService.showInformation(expectedTitle, expectedMessage, expectedDetail);

        testSubscriber.awaitCount(1);
        List<Notification> notificationList = testSubscriber.values();
        Notification expectedNotification = notificationList.get(0);

        assertTrue(map.containsKey(expectedNotification.getId()));

        Notification actualNotification = map.get(expectedNotification.getId());

        // Verify if the notification object getting created is having desired values in the fields
        assertEquals(expectedNotification.getId(), actualNotification.getId());
        assertEquals(NotificationType.INFORMATION, actualNotification.getType());
        assertNull(actualNotification.getFormResponse());
        assertFalse(actualNotification.getQuestionResponse());
        assertNull(actualNotification.getStackTrace());
        assertEquals(expectedMessage, actualNotification.getMessage());
        assertEquals(expectedTitle, actualNotification.getTitle());
        assertEquals(expectedDetail, actualNotification.getDetail());
        assertNull(actualNotification.getFormParameters());
        assertFalse(actualNotification.getConfirmationResponse());
    }

    @Test
    public void validateshowQuestionGUI() {
        Map<String, Notification> map = ciShellCIBridgeNotificationFacade.getNotificationMap();
        TestSubscriber<Notification> testSubscriber = new TestSubscriber<>();
        ciShellCIBridgeNotificationFacade.notificationAdded().subscribe(testSubscriber);

        String expectedTitle = "Dummy Question Title";
        String expectedMessage = "Dummy Question Message";
        String expectedDetail = "Dummy Question Details";
        Boolean expectedQuestionRespone = true;

        Thread createNotificationThread = new Thread(new Runnable() {
            @Override
            public void run() {
                ciBridgeGUIBuilderService.showQuestion(expectedTitle, expectedMessage, expectedDetail);
            }
        });

        createNotificationThread.start();

        try {
            Thread.sleep(50);
        } catch (Exception e){
            e.printStackTrace();
        }

        testSubscriber.awaitCount(1);
        List<Notification> notificationList = testSubscriber.values();
        Notification expectedNotification = notificationList.get(0);

        NotificationResponse notificationResponse = new NotificationResponse(null, expectedQuestionRespone, false, false);
        ciShellCIBridgeNotificationFacade.setNotificationResponse(expectedNotification.getId(), notificationResponse);

        assertTrue(map.containsKey(expectedNotification.getId()));

        Notification actualNotification = map.get(expectedNotification.getId());

        // Verify if the notification object getting created is having desired values in the fields
        assertEquals(expectedNotification.getId(), actualNotification.getId());
        assertEquals(NotificationType.QUESTION, actualNotification.getType());
        assertNull(actualNotification.getFormResponse());
        assertFalse(actualNotification.getConfirmationResponse());
        assertNull(actualNotification.getStackTrace());
        assertEquals(expectedMessage, actualNotification.getMessage());
        assertEquals(expectedTitle, actualNotification.getTitle());
        assertEquals(expectedDetail, actualNotification.getDetail());
        assertNull(actualNotification.getFormParameters());

        //Comparing Form response
        assertEquals(expectedQuestionRespone, actualNotification.getQuestionResponse());
    }

    @Test
    public void validateshowWarningGUI() {

        Map<String, Notification> map = ciShellCIBridgeNotificationFacade.getNotificationMap();
        TestSubscriber<Notification> testSubscriber = new TestSubscriber<>();
        ciShellCIBridgeNotificationFacade.notificationAdded().subscribe(testSubscriber);

        String expectedTitle = "Dummy Warning Title";
        String expectedMessage = "Dummy Warning Message";
        String expectedDetail = "Dummy Warning Details";

        ciBridgeGUIBuilderService.showWarning(expectedTitle, expectedMessage, expectedDetail);

        testSubscriber.awaitCount(1);
        List<Notification> notificationList = testSubscriber.values();
        Notification expectedNotification = notificationList.get(0);

        assertTrue(map.containsKey(expectedNotification.getId()));

        Notification actualNotification = map.get(expectedNotification.getId());

        // Verify if the notification object getting created is having desired values in the fields
        assertEquals(expectedNotification.getId(), actualNotification.getId());
        assertEquals(NotificationType.WARNING, actualNotification.getType());
        assertNull(actualNotification.getFormResponse());
        assertFalse(actualNotification.getQuestionResponse());
        assertNull(actualNotification.getStackTrace());
        assertEquals(expectedMessage, actualNotification.getMessage());
        assertEquals(expectedTitle, actualNotification.getTitle());
        assertEquals(expectedDetail, actualNotification.getDetail());
        assertNull(actualNotification.getFormParameters());
        assertFalse(actualNotification.getConfirmationResponse());

    }

}
