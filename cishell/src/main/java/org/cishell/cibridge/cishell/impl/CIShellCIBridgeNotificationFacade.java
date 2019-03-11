package org.cishell.cibridge.cishell.impl;

import com.google.common.base.Preconditions;
import io.reactivex.Flowable;
import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.model.*;
import org.cishell.service.guibuilder.GUIBuilderService;
import org.reactivestreams.Publisher;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

public class CIShellCIBridgeNotificationFacade implements CIBridge.NotificationFacade {

    private CIShellCIBridge cibridge;
    private HashMap<String, Notification> notificationMap = new LinkedHashMap<>();

    public void setCIBridge(CIShellCIBridge cibridge) {
        Preconditions.checkNotNull(cibridge, "cibridge cannot be null");
        this.cibridge = cibridge;
        this.cibridge.getBundleContext().registerService(GUIBuilderService.class.getName(),
                new CIBridgeGUIBuilderService(cibridge), new Hashtable<String, String>());
    }


    @Override
    public NotificationQueryResults getNotifications(NotificationFilter filter) {
        List<Notification> notifications = new ArrayList<>();
//      BundleContext context = cibridge.getBundleContext();
        PageInfo pageInfo = new PageInfo(false, false);
        NotificationQueryResults queryResults = null;
        try {
            if (filter != null) {
                System.out.println("Filter :" + filter);
                if (filter.getID() != null) {
                    for (String pids : filter.getID()) {
                        if (notificationMap.containsKey(pids)) {
                            notifications.add(notificationMap.get(pids));
                        }
                    }
                }
            } else {
                System.out.println("Filter is empty!");
            }
            for (Entry<String, Notification> entry : notificationMap.entrySet()) {
                System.out.println("inside for e");
                System.out.println(entry.getKey());
            }
            queryResults = new NotificationQueryResults(notifications, pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return queryResults;
    }

    @Override
    public Boolean isClosed(String NotificationId) {
        if (notificationMap.containsKey(NotificationId)) {
            return notificationMap.get(NotificationId).getIsClosed();
        }
        return false;
    }

    public Boolean setNotificationResponse(String notificationId, NotificationResponse response) {
        if (notificationMap.containsKey(notificationId)) {
            Notification notification = notificationMap.get(notificationId);
            notification.setClosed(response.getCloseNotification());
            notification.setConfirmationResponse(response.getConfirmationResponse());

            List<Property> formResponse = new ArrayList<>();
            for (PropertyInput inputProperty : response.getFormResponse()) {
                formResponse.add(new Property(inputProperty.getKey(), inputProperty.getValue()));
            }
            notification.setFormResponse(formResponse);
            notification.setQuestionResponse(response.getQuestionResponse());
            notificationMap.put(notificationId, notification);
            return true;
        }
        return false;
    }

    public Boolean closeNotification(String notificationId) {
        if (notificationMap.containsKey(notificationId)) {
            Notification notification = notificationMap.get(notificationId);
            notification.setClosed(true);
            return true;
        }
        return false;
    }

    public HashMap<String, Notification> getNotificationMap() {
        return notificationMap;
    }


    // TODO Update the subscriptions with listener
    public Publisher<Notification> notificationAdded() {
        Notification notification = new Notification("1", NotificationType.INFORMATION, "Notification", "Test", null,
                null, null, null, null, true, true);
        List<Notification> results = new ArrayList<Notification>();
        results.add(notification);
        return Flowable.fromIterable(results).delay(2, TimeUnit.SECONDS);
    }

    // TODO Update the subscriptions with listener
    public Publisher<Notification> notificationUpdated() {
        Notification notification = new Notification("1", NotificationType.INFORMATION, "Notification", "Test", null,
                null, null, null, null, true, true);
        List<Notification> results = new ArrayList<>();
        results.add(notification);
        return Flowable.fromIterable(results).delay(2, TimeUnit.SECONDS);
    }

}
