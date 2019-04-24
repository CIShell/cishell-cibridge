package org.cishell.cibridge.cishell.impl;

import io.reactivex.subscribers.TestSubscriber;
import org.cishell.cibridge.core.model.*;
import org.cishell.service.guibuilder.GUI;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

public class CIShellCIBridgeNotificationFacadeIT extends CIShellCIBridgeBaseIT {

    @Test
    public void validateGetNotificationsWithOutFilter() {

        TestSubscriber<Notification> testSubscriber = new TestSubscriber<>();
        notificationFacade.notificationAdded().subscribe(testSubscriber);

        NotificationFilter notificationFilter = new NotificationFilter();

        String id1 = "RandomFormTypeNotfication1";
        GUI gui1 = guiBuilderService.createGUI(id1, null);
        gui1.open();


        String id2 = "RandomFormTypeNotfication2";
        GUI gui2 = guiBuilderService.createGUI(id2, null);
        gui2.open();

        String id3 = "RandomFormTypeNotfication3";
        GUI gui3 = guiBuilderService.createGUI(id3, null);
        gui3.open();

        testSubscriber.awaitCount(3);
        List<Notification> notificationList = testSubscriber.values();

        List<String> listOfIds = new ArrayList<>();

        for (Notification n : testSubscriber.values()) {
            listOfIds.add(n.getId());
        }

        NotificationQueryResults actualResults = notificationFacade.getNotifications(new NotificationFilter());
        List<Notification> actualNotifications = actualResults.getResults();

        for (Notification notification : actualNotifications) {
            assertTrue(listOfIds.contains(notification.getId()));
        }

        notificationFacade.removeNotification(notificationList.get(0).getId());
        notificationFacade.removeNotification(notificationList.get(1).getId());
        notificationFacade.removeNotification(notificationList.get(2).getId());
    }

    @Test
    public void validateGetNotificationsWithFilter() {
        TestSubscriber<Notification> testSubscriber = new TestSubscriber<>();
        notificationFacade.notificationAdded().subscribe(testSubscriber);

        NotificationFilter notificationFilter = new NotificationFilter();

        String id1 = "RandomFormTypeNotfication1";
        GUI gui1 = guiBuilderService.createGUI(id1, null);
        gui1.open();

        String id2 = "RandomFormTypeNotfication2";
        GUI gui2 = guiBuilderService.createGUI(id2, null);
        gui2.open();

        String id3 = "RandomFormTypeNotfication3";
        GUI gui3 = guiBuilderService.createGUI(id3, null);
        gui3.open();

        testSubscriber.awaitCount(3);
        List<Notification> notificationList = testSubscriber.values();

        //Closing the id1 notification
        notificationFacade.closeNotification(notificationList.get(0).getId());

        //Setting the notification filter to return only closed notifications
        notificationFilter.setIsClosed(true);

        //Fetching results
        NotificationQueryResults actualResults = notificationFacade.getNotifications(notificationFilter);
        List<Notification> actualNotifications = actualResults.getResults();

        // Only notification with id1 is expected as it is the only closed notification
        assertEquals(1, actualResults.getResults().size());
        assertEquals(true, actualNotifications.get(0).getIsClosed());

        // Closing the notification of id2
        notificationFacade.closeNotification(id2);

        // Adding ID's of notification id1 and id3 to the filter
        notificationFilter.setID(Arrays.asList(notificationList.get(0).getId(), notificationList.get(2).getId()));

        // Expected is ony notification of id1 because there are two filters closed and List of ID's and only notification of id1 satisifies both properties
        actualResults = notificationFacade.getNotifications(notificationFilter);

        // Since we have two ids closed to true and id's
        assertEquals(1, actualResults.getResults().size());

        assertEquals(notificationList.get(0).getId(), actualNotifications.get(0).getId());

        // Removing notifications
        notificationFacade.removeNotification(notificationList.get(0).getId());
        notificationFacade.removeNotification(notificationList.get(1).getId());
        notificationFacade.removeNotification(notificationList.get(2).getId());
    }

    @Test
    public void validateisClosedNotification() {
        TestSubscriber<Notification> testSubscriber = new TestSubscriber<>();
        notificationFacade.notificationAdded().subscribe(testSubscriber);

        String id = "RandomFormTypeNotfication";
        GUI gui = guiBuilderService.createGUI(id, null);
        gui.open();

        testSubscriber.awaitCount(1);
        List<Notification> notificationList = testSubscriber.values();
        Notification expectedNotification = notificationList.get(0);

        NotificationFilter notificationFilter = new NotificationFilter();
        List<String> notificationId = new ArrayList<>(Arrays.asList(expectedNotification.getId()));
        notificationFilter.setID(notificationId);

        Notification exNotification = notificationFacade.getNotifications(notificationFilter).getResults().get(0);
        assertFalse(exNotification.getIsClosed());

        notificationFacade.closeNotification(expectedNotification.getId());
        assertTrue(exNotification.getIsClosed());

        notificationFacade.removeNotification(expectedNotification.getId());
    }

    @Test
    public void validateSetNotificationResponse() {
        TestSubscriber<Notification> testSubscriber = new TestSubscriber<>();
        notificationFacade.notificationAdded().subscribe(testSubscriber);

        String id = "RandomFormTypeNotfication";
        GUI gui = guiBuilderService.createGUI(id, null);
        gui.open();

        testSubscriber.awaitCount(1);
        List<Notification> notificationList = testSubscriber.values();
        Notification expectedNotification = notificationList.get(0);

        NotificationFilter notificationFilter = new NotificationFilter();
        List<String> notificationId = new ArrayList<>(Arrays.asList(expectedNotification.getId()));
        notificationFilter.setID(notificationId);

        Notification exNotification = notificationFacade.getNotifications(notificationFilter).getResults().get(0);
        assertFalse(exNotification.getIsClosed());

        HashSet<String> propertyKeys = new HashSet<>(Arrays.asList("key1", "key2", "key3"));
        HashSet<String> propertyValues = new HashSet<>(Arrays.asList("value1", "value2", "value3"));

        PropertyInput property = new PropertyInput("key1", "value1");
        PropertyInput property1 = new PropertyInput("key2", "value2");
        PropertyInput property2 = new PropertyInput("key3", "value3");

        List<PropertyInput> formResponse = new ArrayList<>();
        formResponse.add(property);
        formResponse.add(property1);
        formResponse.add(property2);
        NotificationResponse notificationResponse = new NotificationResponse(formResponse, false, false, false);
        notificationFacade.setNotificationResponse(expectedNotification.getId(), notificationResponse);


        List<Property> actualResponse = exNotification.getFormResponse();
        assertEquals(propertyKeys.size(), formResponse.size());
        for (Property p : actualResponse) {
            assertTrue(propertyKeys.contains(p.getKey()));
            assertTrue(propertyValues.contains(p.getValue()));
        }

        notificationFacade.removeNotification(expectedNotification.getId());

    }

    @Test
    public void validateCloseNotification() {
        TestSubscriber<Notification> testSubscriber = new TestSubscriber<>();
        notificationFacade.notificationAdded().subscribe(testSubscriber);

        String id = "RandomFormTypeNotfication";
        GUI gui = guiBuilderService.createGUI(id, null);
        gui.open();

        testSubscriber.awaitCount(1);
        List<Notification> notificationList = testSubscriber.values();
        Notification expectedNotification = notificationList.get(0);

        NotificationFilter notificationFilter = new NotificationFilter();
        List<String> notificationId = new ArrayList<>(Arrays.asList(expectedNotification.getId()));
        notificationFilter.setID(notificationId);

        Notification exNotification = notificationFacade.getNotifications(notificationFilter).getResults().get(0);
        assertFalse(exNotification.getIsClosed());

        notificationFacade.closeNotification(expectedNotification.getId());
        assertTrue(exNotification.getIsClosed());

        notificationFacade.removeNotification(expectedNotification.getId());
    }

    @Test
    public void validateNotificationAddedSubscriber() {

        TestSubscriber<Notification> testSubscriber = new TestSubscriber<>();
        notificationFacade.notificationAdded().subscribe(testSubscriber);

        String expectedTitle = "Test Information Notification";
        String expectedMessage = "Information Type of notification";
        String expectedDetail = "Detailed option for information message";

        guiBuilderService.showInformation(expectedTitle, expectedMessage, expectedDetail);

        testSubscriber.awaitCount(1);
        testSubscriber.assertNoErrors();

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

        notificationFacade.removeNotification(expectedNotification.getId());
    }

    @Test
    public void validateNotificationUpdatedSubscriber() {

        TestSubscriber<Notification> testNotiAddedSubscriber = new TestSubscriber<>();
        notificationFacade.notificationAdded().subscribe(testNotiAddedSubscriber);

        String expectedTitle = "Test Info Notification";
        String expectedMessage = "Info Type of notification";
        String expectedDetail = "Detailed option for info message";
        boolean expectedCloseNotification = true;

        guiBuilderService.showInformation(expectedTitle, expectedMessage, expectedDetail);

        testNotiAddedSubscriber.awaitCount(1);
        testNotiAddedSubscriber.assertNoErrors();

        List<Notification> notificationList = testNotiAddedSubscriber.values();
        Notification expectedNotification = notificationList.get(0);

        NotificationFilter notificationFilter = new NotificationFilter();
        List<String> notificationId = new ArrayList<>(Arrays.asList(expectedNotification.getId()));
        notificationFilter.setID(notificationId);

        Notification actualNotification = notificationFacade.getNotifications(notificationFilter).getResults().get(0);
        assertNotNull(actualNotification);

        NotificationResponse notificationResponse = new NotificationResponse(null, false, false, expectedCloseNotification);

        TestSubscriber<Notification> testNotiUpdatedSubscriber = new TestSubscriber<>();
        notificationFacade.notificationUpdated().subscribe(testNotiUpdatedSubscriber);
        notificationFacade.setNotificationResponse(notificationList.get(0).getId(), notificationResponse);

        testNotiUpdatedSubscriber.awaitCount(1);
        testNotiUpdatedSubscriber.assertNoErrors();

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

        // Expected True because we closed the notification
        assertEquals(expectedCloseNotification, actualNotification.getIsClosed());

        notificationFacade.removeNotification(expectedNotification.getId());
    }

}
