package org.cishell.cibridge.cishell.impl;

import com.google.common.base.Preconditions;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.observables.ConnectableObservable;
import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.model.*;
import org.cishell.service.guibuilder.GUIBuilderService;
import org.reactivestreams.Publisher;

import java.util.*;
import java.util.Map.Entry;

public class CIShellCIBridgeNotificationFacade implements CIBridge.NotificationFacade {

    private CIShellCIBridge cibridge;
    private HashMap<String, Notification> notificationMap = new LinkedHashMap<>();
    private ConnectableObservable<Notification> notificationAddedObservable;
    private ObservableEmitter<Notification> notificationAddedObservableEmitter;
    private ConnectableObservable<Notification> notificationUpdatedObservable;
    private ObservableEmitter<Notification> notificationUpdatedObservableEmitter;

    public CIShellCIBridgeNotificationFacade() {

        System.out.println("Notification Facade constructor");

        Observable<Notification> notificationAddedObservable = Observable.create(emitter -> {
            notificationAddedObservableEmitter = emitter;

        });
        this.notificationAddedObservable = notificationAddedObservable.share().publish();
        this.notificationAddedObservable.connect();

        Observable<Notification> notificationUpdatedObservable = Observable.create(emitter -> {
            notificationUpdatedObservableEmitter = emitter;

        });
        this.notificationUpdatedObservable = notificationUpdatedObservable.share().publish();
        this.notificationUpdatedObservable.connect();
    }

    public void setCIBridge(CIShellCIBridge cibridge) {
        Preconditions.checkNotNull(cibridge, "cibridge cannot be null");
        this.cibridge = cibridge;
        this.cibridge.getBundleContext().registerService(GUIBuilderService.class.getName(),
                new CIBridgeGUIBuilderService(cibridge), new Hashtable<String, String>());

    }


    @Override
    public NotificationQueryResults getNotifications(NotificationFilter filter) {

        //TODO remove later not necessary
        System.out.println("GET Notification called");
        this.cibridge.getGUIBuilderService().createGUI("1234546", null);
        List<Notification> notifications = new ArrayList<>();
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
            if (response.getFormResponse() != null) {
                for (PropertyInput inputProperty : response.getFormResponse()) {
                    formResponse.add(new Property(inputProperty.getKey(), inputProperty.getValue()));
                }
            }
            notification.setFormResponse(formResponse);
            notification.setQuestionResponse(response.getQuestionResponse());
            notificationMap.put(notificationId, notification);

            notificationUpdatedObservableEmitter.onNext(notification);
            return true;
        }
        return false;
    }

    public Boolean closeNotification(String notificationId) {
        if (notificationMap.containsKey(notificationId)) {
            Notification notification = notificationMap.get(notificationId);
            notification.setClosed(true);
            notificationUpdatedObservableEmitter.onNext(notification);
            return true;
        }
        return false;
    }

    public HashMap<String, Notification> getNotificationMap() {
        return notificationMap;
    }

    public ObservableEmitter<Notification> getNotificationAddedObservableEmitter() {
        return notificationAddedObservableEmitter;
    }

    public ObservableEmitter<Notification> getNotificationUpdatedObservableEmitter() {
        return notificationUpdatedObservableEmitter;
    }

    public ConnectableObservable<Notification> getNotificationAddedObservable() {
        return notificationAddedObservable;
    }

    public ConnectableObservable<Notification> getNotificationUpdatedObservable() {
        return notificationUpdatedObservable;
    }

    // TODO Test Pending
    public Publisher<Notification> notificationAdded() {
        Flowable<Notification> publisher;
        ConnectableObservable<Notification> connectableObservable = notificationAddedObservable;
        publisher = connectableObservable.toFlowable(BackpressureStrategy.BUFFER);
        return publisher;
    }

    // TODO Test Pending
    public Publisher<Notification> notificationUpdated() {
        Flowable<Notification> publisher;
        ConnectableObservable<Notification> connectableObservable = notificationUpdatedObservable;
        publisher = connectableObservable.toFlowable(BackpressureStrategy.BUFFER);
        return publisher;
    }

}
