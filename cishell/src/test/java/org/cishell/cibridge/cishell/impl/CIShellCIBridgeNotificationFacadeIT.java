package org.cishell.cibridge.cishell.impl;

import io.reactivex.subscribers.TestSubscriber;
import org.cishell.cibridge.cishell.IntegrationTestCase;
import org.cishell.cibridge.core.model.*;
import org.cishell.service.guibuilder.GUI;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class CIShellCIBridgeNotificationFacadeIT extends IntegrationTestCase {

    private CIShellCIBridgeNotificationFacade ciShellCIBridgeNotificationFacade = getCIShellCIBridge().cishellNotification;
    private CIBridgeGUIBuilderService ciBridgeGUIBuilderService = (CIBridgeGUIBuilderService) getCIShellCIBridge().getGUIBuilderService();

    @Test
    public void validateGetNotificationsWithOutFilter() {

        NotificationFilter notificationFilter = new NotificationFilter();

        String id1 = "RandomFormTypeNotfication1";
        GUI gui1 = ciBridgeGUIBuilderService.createGUI(id1, null);
        gui1.open();

        String id2 = "RandomFormTypeNotfication2";
        GUI gui2 = ciBridgeGUIBuilderService.createGUI(id2, null);
        gui2.open();

        String id3 = "RandomFormTypeNotfication3";
        GUI gui3 = ciBridgeGUIBuilderService.createGUI(id3, null);
        gui3.open();

        NotificationQueryResults actualResults = ciShellCIBridgeNotificationFacade.getNotifications(notificationFilter);
        List<Notification> actualNotifications = actualResults.getResults();

        assertEquals(3, actualResults.getResults().size());

        ciShellCIBridgeNotificationFacade.getNotificationMap().remove(id1);
        ciShellCIBridgeNotificationFacade.getNotificationMap().remove(id2);
        ciShellCIBridgeNotificationFacade.getNotificationMap().remove(id3);
    }

    @Test
    public void validateGetNotificationsWithFilter() {

        NotificationFilter notificationFilter = new NotificationFilter();

        String id1 = "RandomFormTypeNotfication1";
        GUI gui1 = ciBridgeGUIBuilderService.createGUI(id1, null);
        gui1.open();

        String id2 = "RandomFormTypeNotfication2";
        GUI gui2 = ciBridgeGUIBuilderService.createGUI(id2, null);
        gui2.open();

        String id3 = "RandomFormTypeNotfication3";
        GUI gui3 = ciBridgeGUIBuilderService.createGUI(id3, null);
        gui3.open();

        //Closing the id1 notification
        ciShellCIBridgeNotificationFacade.getNotificationMap().get(id1).setClosed(true);

        //Setting the notification filter to return only closed notifications
        notificationFilter.setIsClosed(true);

        //Fetching results
        NotificationQueryResults actualResults = ciShellCIBridgeNotificationFacade.getNotifications(notificationFilter);
        List<Notification> actualNotifications = actualResults.getResults();

        // Only notification with id1 is expected as it is the only closed notification
        assertEquals(1, actualResults.getResults().size());
        assertEquals(true, actualNotifications.get(0).getIsClosed());

        // Closing the notification of id2
        ciShellCIBridgeNotificationFacade.getNotificationMap().get(id2).setClosed(true);

        // Adding ID's of notification id1 and id3 to the filter
        notificationFilter.setID(Arrays.asList(id1, id3));

        // Expected is ony notification of id1 because there are two filters closed and List of ID's and only notification of id1 satisifies both properties
        actualResults = ciShellCIBridgeNotificationFacade.getNotifications(notificationFilter);

        // Since we have two ids closed to true and id's
        assertEquals(1, actualResults.getResults().size());

        assertEquals(id1, actualNotifications.get(0).getId());
        // Removing notifications
        ciShellCIBridgeNotificationFacade.getNotificationMap().remove(id1);
        ciShellCIBridgeNotificationFacade.getNotificationMap().remove(id2);
        ciShellCIBridgeNotificationFacade.getNotificationMap().remove(id3);

    }

    @Test
    public void validateisClosedNotification() {

        String id = "RandomFormTypeNotfication";
        GUI gui = ciBridgeGUIBuilderService.createGUI(id, null);
        gui.open();

        TestSubscriber<Notification> testSubscriber = new TestSubscriber<>();
        ciShellCIBridgeNotificationFacade.notificationAdded().subscribe(testSubscriber);

        Notification exNotification = ciShellCIBridgeNotificationFacade.getNotificationMap().get(id);
        assertFalse(exNotification.getIsClosed());

        ciShellCIBridgeNotificationFacade.closeNotification(id);
        assertTrue(exNotification.getIsClosed());

        ciShellCIBridgeNotificationFacade.getNotificationMap().remove(id);
    }

    @Test
    public void validateSetNotificationResponse() {

        String id = "RandomFormTypeNotfication";
        GUI gui = ciBridgeGUIBuilderService.createGUI(id, null);
        gui.open();

        TestSubscriber<Notification> testSubscriber = new TestSubscriber<>();
        ciShellCIBridgeNotificationFacade.notificationAdded().subscribe(testSubscriber);

        Notification exNotification = ciShellCIBridgeNotificationFacade.getNotificationMap().get(id);
        assertFalse(exNotification.getIsClosed());

        HashSet<String> propertyKeys = new HashSet<>();
        HashSet<String> propertyValues = new HashSet<>();
        propertyKeys.addAll(Arrays.asList("key1", "key2", "key3"));
        propertyValues.addAll(Arrays.asList("value1", "value2", "value3"));

        PropertyInput property = new PropertyInput("key1", "value1");
        PropertyInput property1 = new PropertyInput("key2", "value2");
        PropertyInput property2 = new PropertyInput("key3", "value3");

        List<PropertyInput> formResponse = new ArrayList<>();
        formResponse.add(property);
        formResponse.add(property1);
        formResponse.add(property2);
        NotificationResponse notificationResponse = new NotificationResponse(formResponse, false, false, false);
        ciShellCIBridgeNotificationFacade.setNotificationResponse(id, notificationResponse);


        List<Property> actualResponse = exNotification.getFormResponse();
        assertEquals(propertyKeys.size(), formResponse.size());
        for (Property p : actualResponse) {
            assertTrue(propertyKeys.contains(p.getKey()));
            assertTrue(propertyValues.contains(p.getValue()));
        }

        ciShellCIBridgeNotificationFacade.getNotificationMap().remove(id);

    }

    @Test
    public void validateCloseNotification() {

        String id = "RandomFormTypeNotfication";
        GUI gui = ciBridgeGUIBuilderService.createGUI(id, null);
        gui.open();

        TestSubscriber<Notification> testSubscriber = new TestSubscriber<>();
        ciShellCIBridgeNotificationFacade.notificationAdded().subscribe(testSubscriber);

        Notification exNotification = ciShellCIBridgeNotificationFacade.getNotificationMap().get(id);
        assertFalse(exNotification.getIsClosed());

        ciShellCIBridgeNotificationFacade.closeNotification(id);
        assertTrue(exNotification.getIsClosed());

        ciShellCIBridgeNotificationFacade.getNotificationMap().remove(id);
    }

    @Test
    public void validateNotificationAddedSubscriber() {

        Map<String, Notification> map = ciShellCIBridgeNotificationFacade.getNotificationMap();
        TestSubscriber<Notification> testSubscriber = new TestSubscriber<>();
        ciShellCIBridgeNotificationFacade.notificationAdded().subscribe(testSubscriber);

        String expectedTitle = "Test Information Notification";
        String expectedMessage = "Information Type of notification";
        String expectedDetail = "Detailed option for information message";

        ciBridgeGUIBuilderService.showInformation(expectedTitle, expectedMessage, expectedDetail);

        testSubscriber.awaitCount(1);
        testSubscriber.assertNoErrors();

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

        ciShellCIBridgeNotificationFacade.getNotificationMap().remove(expectedNotification.getId());
    }

    @Test
    public void validateNotificationUpdatedSubscriber() {

        Map<String, Notification> map = ciShellCIBridgeNotificationFacade.getNotificationMap();
        TestSubscriber<Notification> testNotiAddedSubscriber = new TestSubscriber<>();
        ciShellCIBridgeNotificationFacade.notificationAdded().subscribe(testNotiAddedSubscriber);

        String expectedTitle = "Test Info Notification";
        String expectedMessage = "Info Type of notification";
        String expectedDetail = "Detailed option for info message";
        boolean expectedCloseNotification = true;

        ciBridgeGUIBuilderService.showInformation(expectedTitle, expectedMessage, expectedDetail);

        testNotiAddedSubscriber.awaitCount(1);
        testNotiAddedSubscriber.assertNoErrors();

        List<Notification> notificationList = testNotiAddedSubscriber.values();
        Notification expectedNotification = notificationList.get(0);
        Notification actualNotification = map.get(expectedNotification.getId());

        NotificationResponse notificationResponse = new NotificationResponse(null, false, false, expectedCloseNotification);

        TestSubscriber<Notification> testNotiUpdatedSubscriber = new TestSubscriber<>();
        ciShellCIBridgeNotificationFacade.notificationUpdated().subscribe(testNotiUpdatedSubscriber);
        ciShellCIBridgeNotificationFacade.setNotificationResponse(notificationList.get(0).getId(), notificationResponse);

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

        ciShellCIBridgeNotificationFacade.getNotificationMap().remove(expectedNotification.getId());

    }

}
