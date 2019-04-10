package org.cishell.cibridge.cishell.impl;

import com.google.common.base.Preconditions;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.observables.ConnectableObservable;
import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.cibridge.cishell.util.PaginationUtil;
import org.cishell.cibridge.cishell.util.Util;
import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.model.*;
import org.cishell.service.guibuilder.GUIBuilderService;
import org.reactivestreams.Publisher;

import java.util.*;
import java.util.function.Predicate;

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
        List<Predicate<Notification>> criteria = new ArrayList<>();
        criteria.add(data -> {
            if (data == null)
                return false;
            if (filter.getID() == null) {
                return true;
            } else {
                return filter.getID().contains(data.getId());
            }

        });
        criteria.add(data -> {
            if (data == null)
                return true;
            if (filter.getIsClosed() == null) {
                return true;
            } else {
                return filter.getIsClosed() == data.getIsClosed();
            }

        });

        List<Notification> notificationList = new ArrayList(notificationMap.values());

        QueryResults<Notification> paginatedQueryResults = PaginationUtil.getPaginatedResults(notificationList,
                criteria, filter.getOffset(), filter.getLimit());

        return new NotificationQueryResults(paginatedQueryResults.getResults(), paginatedQueryResults.getPageInfo());
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addNotification(Notification notification) {
        notificationMap.put(notification.getId(), notification);
        notificationAddedObservableEmitter.onNext(notification);
    }

    public void removeNotification(String notificationId) {
        notificationMap.remove(notificationId);
    }

    public ConnectableObservable<Notification> getNotificationUpdatedObservable() {
        return notificationUpdatedObservable;
    }

    public Publisher<Notification> notificationAdded() {
        return Util.asPublisher(notificationAddedObservable);
    }

    public Publisher<Notification> notificationUpdated() {
        return Util.asPublisher(notificationUpdatedObservable);
    }
}
