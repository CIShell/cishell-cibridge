package org.cishell.cibridge.cishell.impl;

import io.reactivex.subscribers.TestSubscriber;
import org.cishell.cibridge.cishell.IntegrationTestCase;
import org.cishell.cibridge.core.model.*;
import org.cishell.service.guibuilder.GUI;
import org.cishell.service.guibuilder.SelectionListener;
import org.junit.Test;
import org.osgi.service.metatype.MetaTypeProvider;
import org.osgi.service.metatype.ObjectClassDefinition;

import java.util.*;

import static org.junit.Assert.*;

public class CIBridgeGUIBuilderServiceIT extends IntegrationTestCase {

    private CIShellCIBridgeNotificationFacade ciShellCIBridgeNotificationFacade = getCIShellCIBridge().cishellNotification;
    private CIBridgeGUIBuilderService ciBridgeGUIBuilderService = (CIBridgeGUIBuilderService) getCIShellCIBridge().getGUIBuilderService();

    @Test
    public void validateCreateGUIOpenWithoutParams() {

        TestSubscriber<Notification> testSubscriber = new TestSubscriber<>();
        ciShellCIBridgeNotificationFacade.notificationAdded().subscribe(testSubscriber);

        String id = "NotificationOpenWithoutParamsID";
        GUI gui = ciBridgeGUIBuilderService.createGUI(id, null);
        // Should create a notification and add it to map.
        gui.open();

        testSubscriber.awaitCount(1);
        List<Notification> notificationList = testSubscriber.values();
        Notification expectedNotification = notificationList.get(0);

        NotificationFilter notificationFilter = new NotificationFilter();
        List<String> notificationId = new ArrayList<>(Arrays.asList(expectedNotification.getId()));
        notificationFilter.setID(notificationId);

        Notification notification = ciShellCIBridgeNotificationFacade.getNotifications(notificationFilter).getResults().get(0);
        assertNotNull(notification);

        // Verify if the notification object getting created is having desired values in the fields
        assertEquals(expectedNotification.getId(), notification.getId());
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

    // FIXME Not sure how to pass params. Pass params and assert the values of params passed are being filled in the notification form
    @Test
    public void validateCreateGUIOpenWithParams() {

        TestSubscriber<Notification> testSubscriber = new TestSubscriber<>();
        ciShellCIBridgeNotificationFacade.notificationAdded().subscribe(testSubscriber);

        MetaTypeProvider metaTypeProvider = new MetaTypeProvider() {
            @Override
            public ObjectClassDefinition getObjectClassDefinition(String s, String s1) {
                return null;
            }

            @Override
            public String[] getLocales() {
                return new String[0];
            }
        };



        String id = "NotificationOpenWithParamsID";
        GUI gui = ciBridgeGUIBuilderService.createGUI(id, null);
        // Should create a notification and add it to map.
        gui.open();
        testSubscriber.awaitCount(1);
        List<Notification> notificationList = testSubscriber.values();
        Notification expectedNotification = notificationList.get(0);


        NotificationFilter notificationFilter = new NotificationFilter();
        List<String> notificationId = new ArrayList<>(Arrays.asList(expectedNotification.getId()));
        notificationFilter.setID(notificationId);

        Notification notification = ciShellCIBridgeNotificationFacade.getNotifications(notificationFilter).getResults().get(0);
        assertNotNull(notification);

        // Verify if the notification object getting created is having desired values in the fields
        assertEquals(expectedNotification.getId(), notification.getId());
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
        TestSubscriber<Notification> testSubscriber = new TestSubscriber<>();
        ciShellCIBridgeNotificationFacade.notificationAdded().subscribe(testSubscriber);

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
                testSubscriber.awaitCount(1);
                List<Notification> notificationList = testSubscriber.values();
                Notification expectedNotification = notificationList.get(0);

                NotificationFilter notificationFilter = new NotificationFilter();
                List<String> notificationId = new ArrayList<>(Arrays.asList(expectedNotification.getId()));
                notificationFilter.setID(notificationId);

                Notification notification = ciShellCIBridgeNotificationFacade.getNotifications(notificationFilter).getResults().get(0);
                assertNotNull(notification);

                // Verify if the notification object getting created is having desired values in the fields
                assertEquals(expectedNotification.getId(), notification.getId());
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

    // FIXME Not sure how to pass params. Pass params and assert the values of params passed are being filled in the notification form
    @Test
    public void validateCreateGUIOpenAndWaitWithParams() {

        TestSubscriber<Notification> testSubscriber = new TestSubscriber<>();
        ciShellCIBridgeNotificationFacade.notificationAdded().subscribe(testSubscriber);
        String id = "NotificationOpenAndWaitWithoutParamsID";

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
                testSubscriber.awaitCount(1);
            }
        });

        createNotificationThread.start();
        testSubscriber.awaitCount(1);
        List<Notification> notificationList = testSubscriber.values();
        Notification expectedNotification = notificationList.get(0);
        List<PropertyInput> expectedFormResponse = new ArrayList<>();
        expectedFormResponse.add(property);
        expectedFormResponse.add(property1);
        expectedFormResponse.add(property2);
        NotificationResponse notificationResponse = new NotificationResponse(expectedFormResponse, false, false, false);

        notificationList = testSubscriber.values();
        Notification tempExpectedNotification = notificationList.get(0);
        System.out.println(tempExpectedNotification);
        ciShellCIBridgeNotificationFacade.setNotificationResponse(tempExpectedNotification.getId(), notificationResponse);

        NotificationFilter notificationFilter = new NotificationFilter();
        List<String> notificationId = new ArrayList<>(Arrays.asList(expectedNotification.getId()));
        notificationFilter.setID(notificationId);

        Notification notification = ciShellCIBridgeNotificationFacade.getNotifications(notificationFilter).getResults().get(0);
        assertNotNull(notification);

        // Verify if the notification object getting created is having desired values in the fields
        assertEquals(expectedNotification.getId(), notification.getId());
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
        List<Property> actualFormResponse = notification.getFormResponse();
        assertEquals(propertyKeys.size(), actualFormResponse.size());
        for (Property p : actualFormResponse) {
            assertTrue(propertyKeys.contains(p.getKey()));
            assertTrue(propertyValues.contains(p.getValue()));
        }

    }

    @Test
    public void validateshowConfirmGUI() {

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
        } catch (Exception e) {
            e.printStackTrace();
        }

        testSubscriber.awaitCount(1);
        List<Notification> notificationList = testSubscriber.values();
        Notification expectedNotification = notificationList.get(0);

        NotificationResponse notificationResponse = new NotificationResponse(null, false, expectedConfirmationRespone, false);
        ciShellCIBridgeNotificationFacade.setNotificationResponse(expectedNotification.getId(), notificationResponse);

        NotificationFilter notificationFilter = new NotificationFilter();
        List<String> notificationId = new ArrayList<>(Arrays.asList(expectedNotification.getId()));
        notificationFilter.setID(notificationId);

        Notification actualNotification = ciShellCIBridgeNotificationFacade.getNotifications(notificationFilter).getResults().get(0);
        assertNotNull(actualNotification);

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

        TestSubscriber<Notification> testSubscriber = new TestSubscriber<>();
        ciShellCIBridgeNotificationFacade.notificationAdded().subscribe(testSubscriber);

        String expectedTitle = "Dummy Notification Title";
        String expectedMessage = "Dummy Notification Message";
        String expectedDetail = "Dummy Notification Details";

        ciBridgeGUIBuilderService.showError(expectedTitle, expectedMessage, expectedDetail);

        testSubscriber.awaitCount(1);
        List<Notification> notificationList = testSubscriber.values();
        Notification expectedNotification = notificationList.get(0);

        NotificationFilter notificationFilter = new NotificationFilter();
        List<String> notificationId = new ArrayList<>(Arrays.asList(expectedNotification.getId()));
        notificationFilter.setID(notificationId);

        Notification actualNotification = ciShellCIBridgeNotificationFacade.getNotifications(notificationFilter).getResults().get(0);
        assertNotNull(actualNotification);

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

        TestSubscriber<Notification> testSubscriber = new TestSubscriber<>();
        ciShellCIBridgeNotificationFacade.notificationAdded().subscribe(testSubscriber);

        String expectedTitle = "Test Error Notification";
        String expectedMessage = "Error Type of notification";
        Exception expectedException = new ArrayIndexOutOfBoundsException();

        ciBridgeGUIBuilderService.showError(expectedTitle, expectedMessage, expectedException);

        testSubscriber.awaitCount(1);
        List<Notification> notificationList = testSubscriber.values();
        Notification expectedNotification = notificationList.get(0);

        NotificationFilter notificationFilter = new NotificationFilter();
        List<String> notificationId = new ArrayList<>(Arrays.asList(expectedNotification.getId()));
        notificationFilter.setID(notificationId);

        Notification actualNotification = ciShellCIBridgeNotificationFacade.getNotifications(notificationFilter).getResults().get(0);
        assertNotNull(actualNotification);

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
        for (int i = 0; i < stackTraceElements.length; i++) {
            assertEquals(stackTraceElements[i].toString(), actualStacktrace.get(i));
        }
    }

    @Test
    public void validateshowInformationGUI() {

        TestSubscriber<Notification> testSubscriber = new TestSubscriber<>();
        ciShellCIBridgeNotificationFacade.notificationAdded().subscribe(testSubscriber);

        String expectedTitle = "Dummy Notification Title";
        String expectedMessage = "Dummy Notification Message";
        String expectedDetail = "Dummy Notification Details";

        ciBridgeGUIBuilderService.showInformation(expectedTitle, expectedMessage, expectedDetail);

        testSubscriber.awaitCount(1);
        List<Notification> notificationList = testSubscriber.values();
        Notification expectedNotification = notificationList.get(0);

        NotificationFilter notificationFilter = new NotificationFilter();
        List<String> notificationId = new ArrayList<>(Arrays.asList(expectedNotification.getId()));
        notificationFilter.setID(notificationId);

        Notification actualNotification = ciShellCIBridgeNotificationFacade.getNotifications(notificationFilter).getResults().get(0);
        assertNotNull(actualNotification);

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
        } catch (Exception e) {
            e.printStackTrace();
        }

        testSubscriber.awaitCount(1);
        List<Notification> notificationList = testSubscriber.values();
        Notification expectedNotification = notificationList.get(0);

        NotificationResponse notificationResponse = new NotificationResponse(null, expectedQuestionRespone, false, false);
        ciShellCIBridgeNotificationFacade.setNotificationResponse(expectedNotification.getId(), notificationResponse);

        NotificationFilter notificationFilter = new NotificationFilter();
        List<String> notificationId = new ArrayList<>(Arrays.asList(expectedNotification.getId()));
        notificationFilter.setID(notificationId);

        Notification actualNotification = ciShellCIBridgeNotificationFacade.getNotifications(notificationFilter).getResults().get(0);
        assertNotNull(actualNotification);

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

        TestSubscriber<Notification> testSubscriber = new TestSubscriber<>();
        ciShellCIBridgeNotificationFacade.notificationAdded().subscribe(testSubscriber);

        String expectedTitle = "Dummy Warning Title";
        String expectedMessage = "Dummy Warning Message";
        String expectedDetail = "Dummy Warning Details";

        ciBridgeGUIBuilderService.showWarning(expectedTitle, expectedMessage, expectedDetail);

        testSubscriber.awaitCount(1);
        List<Notification> notificationList = testSubscriber.values();
        Notification expectedNotification = notificationList.get(0);

        NotificationFilter notificationFilter = new NotificationFilter();
        List<String> notificationId = new ArrayList<>(Arrays.asList(expectedNotification.getId()));
        notificationFilter.setID(notificationId);

        Notification actualNotification = ciShellCIBridgeNotificationFacade.getNotifications(notificationFilter).getResults().get(0);
        assertNotNull(actualNotification);

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

    @Test
    public void validateSelectionListenerHitOk() {
        TestSubscriber<Notification> testNotiAddedSubscriber = new TestSubscriber<>();
        ciShellCIBridgeNotificationFacade.notificationAdded().subscribe(testNotiAddedSubscriber);

        SelectionListener selectionListener = new SelectionListener() {
            @Override
            public void hitOk(Dictionary<String, Object> dictionary) {
                System.out.println(dictionary);
                assertNotNull(dictionary.get("key"));
                assertSame("value", dictionary.get("key"));
            }

            @Override
            public void cancelled() {
                System.out.println("cancelled");
            }
        };

        GUI gui = ciBridgeGUIBuilderService.createGUI("RandomId", null);
        gui.setSelectionListener(selectionListener);

        gui.open();

        testNotiAddedSubscriber.awaitCount(1);
        List<Notification> notificationList = testNotiAddedSubscriber.values();
        Notification expectedNotification = notificationList.get(0);

        List<PropertyInput> formResponse = new ArrayList<>();
        formResponse.add(new PropertyInput("key", "value"));
        NotificationResponse notificationResponse = new NotificationResponse(formResponse, null, null, null);

        ciShellCIBridgeNotificationFacade.setNotificationResponse(expectedNotification.getId(), notificationResponse);

        ciShellCIBridgeNotificationFacade.removeNotification(expectedNotification.getId());
    }

    // TODO Complete This tests to check selection listener
    @Test
    public void validateSelectionListenerCancelled() {
        TestSubscriber<Notification> testNotiAddedSubscriber = new TestSubscriber<>();
        ciShellCIBridgeNotificationFacade.notificationAdded().subscribe(testNotiAddedSubscriber);

        TestSubscriber<Notification> testNotiUpdatedSubscriber = new TestSubscriber<>();
        ciShellCIBridgeNotificationFacade.notificationUpdated().subscribe(testNotiUpdatedSubscriber);

        SelectionListener selectionListener = new SelectionListener() {
            @Override
            public void hitOk(Dictionary<String, Object> dictionary) {
                assertFalse(false);
            }

            @Override
            public void cancelled() {
                //This function has to be called
                assertTrue(true);
            }
        };

        GUI gui = ciBridgeGUIBuilderService.createGUI("Random Id", null);
        gui.setSelectionListener(selectionListener);

        gui.open();

        testNotiAddedSubscriber.awaitCount(1);
        List<Notification> notificationList = testNotiAddedSubscriber.values();
        Notification expectedNotification = notificationList.get(0);

        List<PropertyInput> formResponse = new ArrayList<>();
        formResponse.add(new PropertyInput("key", "value"));
        NotificationResponse notificationResponse = new NotificationResponse(null, null, null, true);

        ciShellCIBridgeNotificationFacade.setNotificationResponse(expectedNotification.getId(), notificationResponse);
        ciShellCIBridgeNotificationFacade.removeNotification(expectedNotification.getId());
    }

}
