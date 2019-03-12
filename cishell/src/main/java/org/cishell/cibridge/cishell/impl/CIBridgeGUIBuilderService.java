package org.cishell.cibridge.cishell.impl;

import io.reactivex.disposables.Disposable;
import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.cibridge.core.model.*;
import org.cishell.service.guibuilder.GUI;
import org.cishell.service.guibuilder.GUIBuilderService;
import org.cishell.service.guibuilder.SelectionListener;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.MetaTypeProvider;
import org.osgi.service.metatype.ObjectClassDefinition;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.*;

public class CIBridgeGUIBuilderService implements GUIBuilderService {

    private CIShellCIBridge cibridge;
    private CIShellCIBridgeNotificationFacade notificationFacade;
    private static final HashMap<Integer, AttributeType> attributeTypeMap = new HashMap<>();
    private static UUID counter;


    public CIBridgeGUIBuilderService(CIShellCIBridge cibridge) {
        this.cibridge = cibridge;
        notificationFacade = cibridge.cishellNotification;

        attributeTypeMap.put(1, AttributeType.STRING);
        attributeTypeMap.put(2, AttributeType.LONG);
        attributeTypeMap.put(3, AttributeType.INTEGER);
        attributeTypeMap.put(4, AttributeType.SHORT);
        attributeTypeMap.put(5, AttributeType.CHARACTER);
        attributeTypeMap.put(6, AttributeType.BYTE);
        attributeTypeMap.put(7, AttributeType.DOUBLE);
        attributeTypeMap.put(8, AttributeType.FLOAT);

        notificationFacade.getNotificationUpdatedObservable().filter(notification -> notification.getId()=="").subscribe(new io.reactivex.Observer<Notification>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Notification notification) {
                System.out.println("client subscriber");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        Disposable subscriber = notificationFacade.getNotificationUpdatedObservable().filter(notification -> notification.getId()=="").subscribe(notification -> System.out.println(notification));
        subscriber.dispose();
    }

    @Override
    public synchronized GUI createGUI(String id, MetaTypeProvider params) {


        HashMap<String, Notification> map = notificationFacade.getNotificationMap();

        ObjectClassDefinition objectClassDefinition = params.getObjectClassDefinition(id, null);
        List<ParameterDefinition> notificationParams = new ArrayList<>();

        for (AttributeDefinition attributes : objectClassDefinition.getAttributeDefinitions(ObjectClassDefinition.ALL)) {
            List<Property> properties = new ArrayList<>();
            for (int i = 0; i < attributes.getOptionLabels().length; i++) {
                Property property = new Property(attributes.getOptionLabels()[i], attributes.getOptionValues()[i]);
                properties.add(property);
            }
            ParameterDefinition parameterDefinition = new ParameterDefinition(attributes.getID(), attributes.getName(), attributes.getDescription(), attributeTypeMap.get(attributes.getType()), properties);
            notificationParams.add(parameterDefinition);
        }

        Notification notification = new Notification(id, NotificationType.FORM, objectClassDefinition.getID(), objectClassDefinition.getName(),
                objectClassDefinition.getDescription(), null, notificationParams, false,
                null, false, false);

        GUI gui = createAndGetGui(id, map, notification);

        gui.setSelectionListener(new SelectionListener() {
            @Override
            public void hitOk(Dictionary<String, Object> dictionary) {

                List<PropertyInput> formResponse = new ArrayList<>();
                Enumeration<String> dictionaryKeys = dictionary.keys();

                while (dictionaryKeys.hasMoreElements()) {
                    String key = dictionaryKeys.nextElement();
                    PropertyInput propertyInput = new PropertyInput(key, dictionary.get(key).toString());
                    formResponse.add(propertyInput);
                }

                NotificationResponse notificationResponse = new NotificationResponse(formResponse, false, false, false);
                notificationFacade.setNotificationResponse(id, notificationResponse);
            }

            @Override
            public void cancelled() {
                NotificationResponse notificationResponse = new NotificationResponse(null, false, false, true);
                notificationFacade.setNotificationResponse(id, notificationResponse);
            }
        });

        return gui;
    }

    @Override
    public Dictionary createGUIandWait(String id, MetaTypeProvider params) {

        HashMap<String, Notification> map = notificationFacade.getNotificationMap();

        ObjectClassDefinition objectClassDefinition = params.getObjectClassDefinition(id, null);
        List<ParameterDefinition> notificationParams = new ArrayList<>();

        for (AttributeDefinition attributes : objectClassDefinition.getAttributeDefinitions(ObjectClassDefinition.ALL)) {
            List<Property> properties = new ArrayList<>();
            for (int i = 0; i < attributes.getOptionLabels().length; i++) {
                Property property = new Property(attributes.getOptionLabels()[i], attributes.getOptionValues()[i]);
                properties.add(property);
            }
            ParameterDefinition parameterDefinition = new ParameterDefinition(attributes.getID(), attributes.getName(), attributes.getDescription(), attributeTypeMap.get(attributes.getType()), properties);
            notificationParams.add(parameterDefinition);
        }

        Notification notification = new Notification(id, NotificationType.FORM, objectClassDefinition.getID(), objectClassDefinition.getName(),
                objectClassDefinition.getDescription(), null, notificationParams, false,
                null, false, false);

        GUI gui = createAndGetGui(id, map, notification);

        gui.setSelectionListener(new SelectionListener() {
            @Override
            public void hitOk(Dictionary<String, Object> dictionary) {

                List<PropertyInput> formResponse = new ArrayList<>();
                Enumeration<String> dictionaryKeys = dictionary.keys();

                while (dictionaryKeys.hasMoreElements()) {
                    String key = dictionaryKeys.nextElement();
                    PropertyInput propertyInput = new PropertyInput(key, dictionary.get(key).toString());
                    formResponse.add(propertyInput);
                }

                NotificationResponse notificationResponse = new NotificationResponse(formResponse, false, false, false);
                notificationFacade.setNotificationResponse(id, notificationResponse);

            }

            @Override
            public void cancelled() {
                NotificationResponse notificationResponse = new NotificationResponse(null, false, false, true);
                notificationFacade.setNotificationResponse(id, notificationResponse);
            }
        });

        return gui.openAndWait();
    }

    @Override
    public boolean showConfirm(String title, String message, String detail) {

        HashMap<String, Notification> map = notificationFacade.getNotificationMap();

        String UUID = counter.randomUUID().toString();

        Notification notification = new Notification(UUID, NotificationType.CONFIRM, title, message,
                detail, null, null, false,
                null, false, false);

        createAndGetGui(UUID, map, notification);

        // TODO Implement busy wait or mutation and a Subscriber to Notification Updated Observable to check for the reponse on the current Notification UUID
//        gui.setSelectionListener(new SelectionListener() {
//            @Override
//            public void hitOk(Dictionary<String, Object> dictionary) {
//                notification.setConfirmationResponse(true);
//            }
//
//            @Override
//            public void cancelled() {
//                notification.setConfirmationResponse(false);
//            }
//        });

        notificationFacade.getNotificationAddedObservableEmitter().onNext(notification);
        return notification.getConfirmationResponse();
    }

    @Override
    public void showError(String title, String message, String detail) {

        HashMap<String, Notification> map = notificationFacade.getNotificationMap();

        String UUID = counter.randomUUID().toString();

        Notification notification = new Notification(UUID, NotificationType.ERROR, title, message,
                detail, null, null, false,
                null, false, false);

        map.put(UUID, notification);
        notificationFacade.getNotificationAddedObservableEmitter().onNext(notification);
    }

    @Override
    public void showError(String title, String message, Throwable error) {

        HashMap<String, Notification> map = notificationFacade.getNotificationMap();

        List<String> stackTrace = new ArrayList<>();
        for (StackTraceElement element : error.getStackTrace()) {
            stackTrace.add(element.toString());
        }

        String UUID = counter.randomUUID().toString();
        Notification notification = new Notification(UUID, NotificationType.ERROR, title, message,
                null, stackTrace, null, false,
                null, false, false);

        map.put(UUID, notification);
        notificationFacade.getNotificationAddedObservableEmitter().onNext(notification);

    }

    @Override
    public void showInformation(String title, String message, String detail) {

        HashMap<String, Notification> map = notificationFacade.getNotificationMap();

        String UUID = counter.randomUUID().toString();
        Notification notification = new Notification(UUID, NotificationType.INFORMATION, title, message,
                detail, null, null, false,
                null, false, false);

        map.put(UUID, notification);
        notificationFacade.getNotificationAddedObservableEmitter().onNext(notification);
    }

    @Override
    public boolean showQuestion(String title, String message, String detail) {

        HashMap<String, Notification> map = notificationFacade.getNotificationMap();

        String UUID = counter.randomUUID().toString();
        Notification notification = new Notification(UUID, NotificationType.QUESTION, title, message,
                detail, null, null, false,
                null, false, false);

        GUI gui = createAndGetGui(UUID, map, notification);

        gui.setSelectionListener(new SelectionListener() {
            @Override
            public void hitOk(Dictionary<String, Object> dictionary) {
                notification.setQuestionResponse(true);
            }

            @Override
            public void cancelled() {
                notification.setQuestionResponse(false);
            }
        });

        notificationFacade.getNotificationAddedObservableEmitter().onNext(notification);

        return notification.getQuestionResponse();
    }

    @Override
    public void showWarning(String title, String message, String detail) {

        HashMap<String, Notification> map = notificationFacade.getNotificationMap();

        String UUID = counter.randomUUID().toString();
        Notification notification = new Notification(UUID, NotificationType.WARNING, title, message,
                detail, null, null, false,
                null, false, false);

        map.put(UUID, notification);
        notificationFacade.getNotificationAddedObservableEmitter().onNext(notification);
    }

    private GUI createAndGetGui(String id, HashMap<String, Notification> map, Notification notification) {

        return new GUI() {
            @Override
            public Dictionary openAndWait() {
                // TODO Implement a publisher to push Notification Responses as Dictionary
                // TODO  Call Notification added subscriber
                notificationFacade.getNotificationAddedObservableEmitter().onNext(notification);
                return null;
            }

            @Override
            public void open() {
                // TODO  Call Notification added subscriber
                map.put(id, notification);
                notificationFacade.getNotificationAddedObservableEmitter().onNext(notification);
            }

            @Override
            public void close() {
                notificationFacade.closeNotification(id);
            }

            @Override
            public boolean isClosed() {
                return notification.getIsClosed();
            }

            @Override
            public void setSelectionListener(SelectionListener selectionListener) {

            }
        };

    }

}
