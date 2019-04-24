package org.cishell.cibridge.cishell.impl;

import io.reactivex.subscribers.TestSubscriber;
import org.cishell.cibridge.cishell.model.CIShellCIBridgeAlgorithmInstance;
import org.cishell.cibridge.core.model.*;
import org.cishell.service.guibuilder.GUI;
import org.cishell.service.guibuilder.SelectionListener;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class CIBridgeGUIBuilderServiceIT extends CIShellCIBridgeBaseIT {

    @Test
    public void validateGUIParams() {
        TestSubscriber<Notification> testSubscriber = new TestSubscriber<>();
        notificationFacade.notificationAdded().subscribe(testSubscriber);

        String pid = "org.cishell.tests.algorithm.UserInputAlgorithm";
        CIShellCIBridgeAlgorithmInstance algorithmInstance = (CIShellCIBridgeAlgorithmInstance) getCIShellCIBridge().cishellAlgorithm.createAlgorithm(pid, null, null);
        getCIShellCIBridge().cishellScheduler.runAlgorithmNow(algorithmInstance.getId());

        testSubscriber.awaitCount(1);
        List<Notification> notificationList = testSubscriber.values();
        assertTrue(1 <= notificationList.size());
        Notification notification = notificationList.get(0);

        // Verify if the notification object getting created is having desired values in the fields
        assertEquals(NotificationType.FORM, notification.getType());
        assertEquals("org.cishell.tests.algorithm.UserInputAlgorithm.OCD", notification.getTitle());
        assertEquals("A Random Number", notification.getMessage());
        assertEquals("isn't name is enough???!!!", notification.getDetail());

        List<ParameterDefinition> formParameters = notification.getFormParameters();
        assertNotNull(formParameters);
        assertEquals(1, formParameters.size());
        ParameterDefinition formParameter = formParameters.get(0);
        assertEquals("ARandomNumber", formParameter.getId());
        assertEquals("A Random Number", formParameter.getName());
        assertEquals("Enter any positive number you like", formParameter.getDescription());
        assertEquals(AttributeType.INTEGER, formParameter.getType());
        assertEquals(0, formParameter.getCardinality());
        assertEquals("1", formParameter.getDefaultValues().get(0));

        notificationFacade.removeNotification(notification.getId());
    }

    @Test
    public void validateCreateGUIOpenWithParams() throws InterruptedException {
        TestSubscriber<Notification> testSubscriber = new TestSubscriber<>();
        notificationFacade.notificationAdded().subscribe(testSubscriber);

        String pid = "org.cishell.tests.algorithm.UserInputAlgorithm";
        CIShellCIBridgeAlgorithmInstance algorithmInstance = (CIShellCIBridgeAlgorithmInstance) getCIShellCIBridge().cishellAlgorithm.createAlgorithm(pid, null, null);
        getCIShellCIBridge().cishellScheduler.runAlgorithmNow(algorithmInstance.getId());

        testSubscriber.awaitCount(1);
        List<Notification> notificationList = testSubscriber.values();
        assertTrue(1 <= notificationList.size());
        Notification notification = notificationList.get(0);

        List<PropertyInput> expectedFormResponse = new ArrayList<>();
        expectedFormResponse.add(new PropertyInput("ARandomNumber", "4"));

        NotificationResponse notificationResponse = new NotificationResponse(expectedFormResponse, false, false, false);
        notificationFacade.setNotificationResponse(notification.getId(), notificationResponse);

        NotificationResponse closeNotificationResponse = new NotificationResponse(null, false, false, true);
        notificationFacade.setNotificationResponse(notification.getId(), closeNotificationResponse);

        Thread.sleep(1000);
        notificationFacade.removeNotification(notification.getId());
    }

    @Test
    public void validateCreateGUIOpenAndWaitWithoutParams() throws InterruptedException {
        TestSubscriber<Notification> testSubscriber = new TestSubscriber<>();
        notificationFacade.notificationAdded().subscribe(testSubscriber);

        String id = "NotificationOpenAndWaitWithoutParamsID";
        GUI gui = guiBuilderService.createGUI(id, null);

        HashSet<String> propertyKeys = new HashSet<>(Arrays.asList("key1", "key2", "key3"));
        HashSet<String> propertyValues = new HashSet<>(Arrays.asList("value1", "value2", "value3"));

        PropertyInput property = new PropertyInput("key1", "value1");
        PropertyInput property1 = new PropertyInput("key2", "value2");
        PropertyInput property2 = new PropertyInput("key3", "value3");

        Thread notificationCreateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // Should create a notification and add it to map and wait for response.
                gui.openAndWait();

            }
        });

        notificationCreateThread.start();

        Thread notificationSetResponseThread = new Thread(new Runnable() {
            @Override
            public void run() {
                testSubscriber.awaitCount(1);
                List<Notification> notificationList = testSubscriber.values();
                Notification expectedNotification = notificationList.get(0);

                List<PropertyInput> formResponse = new ArrayList<>();
                formResponse.add(property);
                formResponse.add(property1);
                formResponse.add(property2);

                NotificationResponse notificationResponse = new NotificationResponse(formResponse, false, false, false);
                notificationFacade.setNotificationResponse(expectedNotification.getId(), notificationResponse);
            }
        });

        notificationSetResponseThread.start();

        Thread.sleep(50);

        List<Notification> notificationList = testSubscriber.values();
        Notification expectedNotification = notificationList.get(0);
        NotificationFilter notificationFilter = new NotificationFilter();
        List<String> notificationId = new ArrayList<>(Arrays.asList(expectedNotification.getId()));
        notificationFilter.setID(notificationId);

        Notification notification = notificationFacade.getNotifications(notificationFilter).getResults().get(0);
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


        notificationFacade.removeNotification(expectedNotification.getId());
    }

    @Test
    public void validateCreateGUIOpenAndWaitWithParams() throws InterruptedException {

        TestSubscriber<Notification> testSubscriber = new TestSubscriber<>();
        notificationFacade.notificationAdded().subscribe(testSubscriber);

        String pid = "org.cishell.tests.algorithm.WaitForUserInputAlgorithm";
        CIShellCIBridgeAlgorithmInstance algorithmInstance = (CIShellCIBridgeAlgorithmInstance) getCIShellCIBridge().cishellAlgorithm.createAlgorithm(pid, null, null);
        getCIShellCIBridge().cishellScheduler.runAlgorithmNow(algorithmInstance.getId());

        testSubscriber.awaitCount(1);
        List<Notification> notificationList = testSubscriber.values();

        Notification notification = notificationList.get(0);
        List<PropertyInput> expectedFormResponse = new ArrayList<>();

        PropertyInput property = new PropertyInput("ARandomNumber", "16");

        expectedFormResponse.add(property);
        NotificationResponse notificationResponse = new NotificationResponse(expectedFormResponse, false, false, false);

        Thread.sleep(2000);
        notificationFacade.setNotificationResponse(notification.getId(), notificationResponse);

        notificationFacade.removeNotification(notification.getId());
    }

    @Test
    public void validateshowConfirmGUI() {
        TestSubscriber<Notification> testSubscriber = new TestSubscriber<>();
        notificationFacade.notificationAdded().subscribe(testSubscriber);

        String expectedTitle = "Dummy Notification Title";
        String expectedMessage = "Dummy Notification Message";
        String expectedDetail = "Dummy Notification Details";
        Boolean expectedConfirmationRespone = true;

        Thread createNotificationThread = new Thread(new Runnable() {
            @Override
            public void run() {
                guiBuilderService.showConfirm(expectedTitle, expectedMessage, expectedDetail);
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
        notificationFacade.setNotificationResponse(expectedNotification.getId(), notificationResponse);

        NotificationFilter notificationFilter = new NotificationFilter();
        List<String> notificationId = new ArrayList<>(Arrays.asList(expectedNotification.getId()));
        notificationFilter.setID(notificationId);

        Notification actualNotification = notificationFacade.getNotifications(notificationFilter).getResults().get(0);
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

        notificationFacade.removeNotification(actualNotification.getId());
    }

    @Test
    public void validateshowErrorGUI() {

        TestSubscriber<Notification> testSubscriber = new TestSubscriber<>();
        notificationFacade.notificationAdded().subscribe(testSubscriber);

        String expectedTitle = "Dummy Notification Title";
        String expectedMessage = "Dummy Notification Message";
        String expectedDetail = "Dummy Notification Details";

        guiBuilderService.showError(expectedTitle, expectedMessage, expectedDetail);

        testSubscriber.awaitCount(1);
        List<Notification> notificationList = testSubscriber.values();
        Notification expectedNotification = notificationList.get(0);

        NotificationFilter notificationFilter = new NotificationFilter();
        List<String> notificationId = new ArrayList<>(Arrays.asList(expectedNotification.getId()));
        notificationFilter.setID(notificationId);

        Notification actualNotification = notificationFacade.getNotifications(notificationFilter).getResults().get(0);
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

        notificationFacade.removeNotification(actualNotification.getId());
    }

    @Test
    public void validateshowErrorWithThrowableGUI() {

        TestSubscriber<Notification> testSubscriber = new TestSubscriber<>();
        notificationFacade.notificationAdded().subscribe(testSubscriber);

        String expectedTitle = "Test Error Notification";
        String expectedMessage = "Error Type of notification";
        Exception expectedException = new ArrayIndexOutOfBoundsException();

        guiBuilderService.showError(expectedTitle, expectedMessage, expectedException);

        testSubscriber.awaitCount(1);
        List<Notification> notificationList = testSubscriber.values();
        Notification expectedNotification = notificationList.get(0);

        NotificationFilter notificationFilter = new NotificationFilter();
        List<String> notificationId = new ArrayList<>(Arrays.asList(expectedNotification.getId()));
        notificationFilter.setID(notificationId);

        Notification actualNotification = notificationFacade.getNotifications(notificationFilter).getResults().get(0);
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

        notificationFacade.removeNotification(actualNotification.getId());
    }

    @Test
    public void validateshowInformationGUI() {

        TestSubscriber<Notification> testSubscriber = new TestSubscriber<>();
        notificationFacade.notificationAdded().subscribe(testSubscriber);

        String expectedTitle = "Dummy Notification Title";
        String expectedMessage = "Dummy Notification Message";
        String expectedDetail = "Dummy Notification Details";

        guiBuilderService.showInformation(expectedTitle, expectedMessage, expectedDetail);

        testSubscriber.awaitCount(1);
        List<Notification> notificationList = testSubscriber.values();
        Notification expectedNotification = notificationList.get(0);

        NotificationFilter notificationFilter = new NotificationFilter();
        List<String> notificationId = new ArrayList<>(Arrays.asList(expectedNotification.getId()));
        notificationFilter.setID(notificationId);

        Notification actualNotification = notificationFacade.getNotifications(notificationFilter).getResults().get(0);
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

        notificationFacade.removeNotification(actualNotification.getId());
    }

    @Test
    public void validateshowQuestionGUI() {

        TestSubscriber<Notification> testSubscriber = new TestSubscriber<>();
        notificationFacade.notificationAdded().subscribe(testSubscriber);

        String expectedTitle = "Dummy Question Title";
        String expectedMessage = "Dummy Question Message";
        String expectedDetail = "Dummy Question Details";
        Boolean expectedQuestionRespone = true;

        Thread createNotificationThread = new Thread(new Runnable() {
            @Override
            public void run() {
                guiBuilderService.showQuestion(expectedTitle, expectedMessage, expectedDetail);
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
        notificationFacade.setNotificationResponse(expectedNotification.getId(), notificationResponse);

        NotificationFilter notificationFilter = new NotificationFilter();
        List<String> notificationId = new ArrayList<>(Arrays.asList(expectedNotification.getId()));
        notificationFilter.setID(notificationId);

        Notification actualNotification = notificationFacade.getNotifications(notificationFilter).getResults().get(0);
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

        notificationFacade.removeNotification(actualNotification.getId());
    }

    @Test
    public void validateshowWarningGUI() {

        TestSubscriber<Notification> testSubscriber = new TestSubscriber<>();
        notificationFacade.notificationAdded().subscribe(testSubscriber);

        String expectedTitle = "Dummy Warning Title";
        String expectedMessage = "Dummy Warning Message";
        String expectedDetail = "Dummy Warning Details";

        guiBuilderService.showWarning(expectedTitle, expectedMessage, expectedDetail);

        testSubscriber.awaitCount(1);
        List<Notification> notificationList = testSubscriber.values();
        Notification expectedNotification = notificationList.get(0);

        NotificationFilter notificationFilter = new NotificationFilter();
        List<String> notificationId = new ArrayList<>(Arrays.asList(expectedNotification.getId()));
        notificationFilter.setID(notificationId);

        Notification actualNotification = notificationFacade.getNotifications(notificationFilter).getResults().get(0);
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

        notificationFacade.removeNotification(actualNotification.getId());
    }

    @Test
    public void validateSelectionListenerHitOk() {
        TestSubscriber<Notification> testNotiAddedSubscriber = new TestSubscriber<>();
        notificationFacade.notificationAdded().subscribe(testNotiAddedSubscriber);

        SelectionListener selectionListener = new SelectionListener() {
            @Override
            public void hitOk(Dictionary<String, Object> dictionary) {
                assertNotNull(dictionary.get("key"));
                assertSame("value", dictionary.get("key"));
            }

            @Override
            public void cancelled() {
                assertTrue("Should not be called", false);
            }
        };

        GUI gui = guiBuilderService.createGUI("RandomId", null);
        gui.setSelectionListener(selectionListener);

        gui.open();

        testNotiAddedSubscriber.awaitCount(1);
        List<Notification> notificationList = testNotiAddedSubscriber.values();
        Notification expectedNotification = notificationList.get(0);

        List<PropertyInput> formResponse = new ArrayList<>();
        formResponse.add(new PropertyInput("key", "value"));
        NotificationResponse notificationResponse = new NotificationResponse(formResponse, null, null, null);

        notificationFacade.setNotificationResponse(expectedNotification.getId(), notificationResponse);
        notificationFacade.removeNotification(expectedNotification.getId());

    }

    @Test
    public void validateSelectionListenerCancelled() {
        TestSubscriber<Notification> testNotiAddedSubscriber = new TestSubscriber<>();
        notificationFacade.notificationAdded().subscribe(testNotiAddedSubscriber);

        TestSubscriber<Notification> testNotiUpdatedSubscriber = new TestSubscriber<>();
        notificationFacade.notificationUpdated().subscribe(testNotiUpdatedSubscriber);

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

        GUI gui = guiBuilderService.createGUI("Random Id", null);
        gui.setSelectionListener(selectionListener);

        gui.open();

        testNotiAddedSubscriber.awaitCount(1);
        List<Notification> notificationList = testNotiAddedSubscriber.values();
        Notification expectedNotification = notificationList.get(0);

        List<PropertyInput> formResponse = new ArrayList<>();
        formResponse.add(new PropertyInput("key", "value"));
        NotificationResponse notificationResponse = new NotificationResponse(null, null, null, true);

        notificationFacade.setNotificationResponse(expectedNotification.getId(), notificationResponse);
        notificationFacade.removeNotification(expectedNotification.getId());
    }

}
