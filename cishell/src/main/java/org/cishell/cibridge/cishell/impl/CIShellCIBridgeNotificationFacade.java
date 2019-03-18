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

public class CIShellCIBridgeNotificationFacade implements CIBridge.NotificationFacade {

    private CIShellCIBridge cibridge;
    private HashMap<String, Notification> notificationMap = new LinkedHashMap<>();
    private ConnectableObservable<Notification> notificationAddedObservable;
    private ObservableEmitter<Notification> notificationAddedObservableEmitter;
    private ConnectableObservable<Notification> notificationUpdatedObservable;
    private ObservableEmitter<Notification> notificationUpdatedObservableEmitter;

    public CIShellCIBridgeNotificationFacade() {

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

        List<Notification> notifications = new ArrayList<>();
        PageInfo pageInfo = new PageInfo(false, false);
        NotificationQueryResults queryResults = null;
        try {
            if (filter != null) {
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
    public Boolean isClosed(String notificationId) {

        if (notificationMap.containsKey(notificationId)) {
            return notificationMap.get(notificationId).getIsClosed();
        }
        return false;
    }

    public Boolean setNotificationResponse(String notificationId, NotificationResponse response) {


        if (notificationMap.containsKey(notificationId)) {
            Notification notification = notificationMap.get(notificationId);
            notification.setClosed(response.getCloseNotification());
            notification.setConfirmationResponse(response.getConfirmationResponse());

            List<Property> formResponse = null;
            if (response.getFormResponse() != null) {
                formResponse = new ArrayList<>();
                for (PropertyInput inputProperty : response.getFormResponse()) {
                    formResponse.add(new Property(inputProperty.getKey(), inputProperty.getValue()));
                }
            }
            notification.setFormResponse(formResponse);
            notification.setQuestionResponse(response.getQuestionResponse());
            notificationMap.put(notificationId, notification);
            synchronized (notification) {
                notification.notify();
            }
            notificationUpdatedObservableEmitter.onNext(notification);
            return true;
        }
        return false;
    }

    public Boolean closeNotification(String notificationId) {

        try {
            if (notificationMap.containsKey(notificationId)) {
                Notification notification = notificationMap.get(notificationId);
                notification.setClosed(true);
                synchronized (notification) {
                    notification.notify();
                }
                notificationUpdatedObservableEmitter.onNext(notification);
                return true;
            }
            return false;
        }catch (Exception e){
            e.printStackTrace();
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

    public Publisher<Notification> notificationAdded() {
        Flowable<Notification> publisher;
        ConnectableObservable<Notification> connectableObservable = notificationAddedObservable;
        publisher = connectableObservable.toFlowable(BackpressureStrategy.BUFFER);
        return publisher;
    }

    public Publisher<Notification> notificationUpdated() {
        Flowable<Notification> publisher;
        ConnectableObservable<Notification> connectableObservable = notificationUpdatedObservable;
        publisher = connectableObservable.toFlowable(BackpressureStrategy.BUFFER);
        return publisher;
    }

}
