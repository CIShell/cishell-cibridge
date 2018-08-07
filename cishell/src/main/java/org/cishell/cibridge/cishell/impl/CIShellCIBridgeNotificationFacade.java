package org.cishell.cibridge.cishell.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Hashtable;

import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.model.*;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.service.guibuilder.GUIBuilderService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.cishell.cibridge.cishell.impl.CIBridgeGUIBuilderService;
import org.osgi.framework.Constants;

public class CIShellCIBridgeNotificationFacade implements CIBridge.NotificationFacade {
    private CIShellCIBridge cibridge;
    private HashMap<String, Notification> notificationMap = new HashMap<String, Notification>();

    public void setCIBridge(CIShellCIBridge cibridge) {
        this.cibridge = cibridge;
    }

    //FIXME (Check all the implementation of methods and test it).
    private void registerGUIBuilderService() {
        this.cibridge.getBundleContext().registerService(GUIBuilderService.class.getName(), new CIBridgeGUIBuilderService(cibridge), new Hashtable<String, String>());

    }

    @Override
    public NotificationQueryResults getNotifications(NotificationFilter filter) {
        // TODO Auto-generated method stub
        List<Notification> notifications = new ArrayList<>();
        BundleContext context = cibridge.getBundleContext();
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
        return null;
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

    //TODO Subscription methods below
    public Notification notificationAdded() {
        // TODO Auto-generated method stub
        return null;
    }


    public Notification notificationUpdated() {
        // TODO Auto-generated method stub
        return null;
    }

}


