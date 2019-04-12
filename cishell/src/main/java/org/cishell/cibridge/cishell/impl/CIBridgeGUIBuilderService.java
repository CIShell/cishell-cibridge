package org.cishell.cibridge.cishell.impl;

import io.reactivex.disposables.Disposable;
import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.cibridge.cishell.util.Util;
import org.cishell.cibridge.core.model.Notification;
import org.cishell.cibridge.core.model.NotificationType;
import org.cishell.cibridge.core.model.ParameterDefinition;
import org.cishell.cibridge.core.model.Property;
import org.cishell.service.guibuilder.GUI;
import org.cishell.service.guibuilder.GUIBuilderService;
import org.cishell.service.guibuilder.SelectionListener;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.MetaTypeProvider;
import org.osgi.service.metatype.ObjectClassDefinition;

import java.util.*;

public class CIBridgeGUIBuilderService implements GUIBuilderService {

    private CIShellCIBridge cibridge;
    private CIShellCIBridgeNotificationFacade notificationFacade;

    public CIBridgeGUIBuilderService(CIShellCIBridge cibridge) {
        this.cibridge = cibridge;
        notificationFacade = cibridge.cishellNotification;
    }

    @Override
    public synchronized GUI createGUI(String id, MetaTypeProvider params) {
        GUI gui = null;
        Notification notification = null;
        String UUID = java.util.UUID.randomUUID().toString();
        try {
            if (params != null) {
                ObjectClassDefinition objectClassDefinition = params.getObjectClassDefinition(id, null);
                List<ParameterDefinition> notificationParams = new ArrayList<>();
                for (AttributeDefinition attributes : objectClassDefinition.getAttributeDefinitions(ObjectClassDefinition.ALL)) {
                    List<Property> properties = new ArrayList<>();
                    for (int i = 0; i < attributes.getOptionLabels().length; i++) {
                        Property property = new Property(attributes.getOptionLabels()[i], attributes.getOptionValues()[i]);
                        properties.add(property);
                    }
                    ParameterDefinition parameterDefinition = new ParameterDefinition(attributes.getID());
                    parameterDefinition.setCardinality(attributes.getCardinality());
                    parameterDefinition.setDefaultValues(Arrays.asList(attributes.getDefaultValue()));
                    parameterDefinition.setDescription(attributes.getDescription());
                    parameterDefinition.setName(attributes.getName());
                    parameterDefinition.setOptions(properties);
                    parameterDefinition.setType(Util.getAttributeTypeFromInteger(attributes.getType()));

                    notificationParams.add(parameterDefinition);
                }
                notification = new Notification(UUID, NotificationType.FORM, objectClassDefinition.getID(), objectClassDefinition.getName(),
                        objectClassDefinition.getDescription(), null, notificationParams, false,
                        null, false, false);
            } else {
                notification = new Notification(UUID, NotificationType.FORM, null, null,
                        null, null, null, false,
                        null, false, false);
            }
            gui = createAndGetGui(notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gui;
    }

    @Override
    public Dictionary createGUIandWait(String id, MetaTypeProvider params) {
        String UUID = java.util.UUID.randomUUID().toString();
        ObjectClassDefinition objectClassDefinition = params.getObjectClassDefinition(id, null);
        List<ParameterDefinition> notificationParams = new ArrayList<>();
        List<Property> properties = null;
        for (AttributeDefinition attributes : objectClassDefinition.getAttributeDefinitions(ObjectClassDefinition.ALL)) {
            properties = new ArrayList<>();
            for (int i = 0; i < attributes.getOptionLabels().length; i++) {
                Property property = new Property(attributes.getOptionLabels()[i], attributes.getOptionValues()[i]);
                properties.add(property);
            }
            ParameterDefinition parameterDefinition = new ParameterDefinition(attributes.getID());
            parameterDefinition.setCardinality(attributes.getCardinality());
            parameterDefinition.setDefaultValues(Arrays.asList(attributes.getDefaultValue()));
            parameterDefinition.setDescription(attributes.getDescription());
            parameterDefinition.setName(attributes.getName());
            parameterDefinition.setOptions(properties);
            parameterDefinition.setType(Util.getAttributeTypeFromInteger(attributes.getType()));

            notificationParams.add(parameterDefinition);
        }
        Notification notification = new Notification(UUID, NotificationType.FORM, objectClassDefinition.getID(), objectClassDefinition.getName(),
                objectClassDefinition.getDescription(), null, notificationParams, false,
                null, false, false);
        GUI gui = createAndGetGui(notification);
        return gui.openAndWait();
    }

    @Override
    public boolean showConfirm(String title, String message, String detail) {
        String UUID = java.util.UUID.randomUUID().toString();
        Notification notification = new Notification(UUID, NotificationType.CONFIRM, title, message,
                detail, null, null, false,
                null, false, false);
        GUI gui = createAndGetGui(notification);
        gui.openAndWait();
        return notification.getConfirmationResponse();
    }

    @Override
    public void showError(String title, String message, String detail) {
        String UUID = java.util.UUID.randomUUID().toString();
        Notification notification = new Notification(UUID, NotificationType.ERROR, title, message,
                detail, null, null, false,
                null, false, false);
        notificationFacade.addNotification(notification);
    }

    @Override
    public void showError(String title, String message, Throwable error) {
        List<String> stackTrace = new ArrayList<>();
        for (StackTraceElement element : error.getStackTrace()) {
            stackTrace.add(element.toString());
        }
        String UUID = java.util.UUID.randomUUID().toString();
        Notification notification = new Notification(UUID, NotificationType.ERROR, title, message,
                null, stackTrace, null, false,
                null, false, false);
        notificationFacade.addNotification(notification);
    }

    @Override
    public void showInformation(String title, String message, String detail) {
        String UUID = java.util.UUID.randomUUID().toString();
        Notification notification = new Notification(UUID, NotificationType.INFORMATION, title, message,
                detail, null, null, false,
                null, false, false);
        notificationFacade.addNotification(notification);
    }

    @Override
    public boolean showQuestion(String title, String message, String detail) {
        String UUID = java.util.UUID.randomUUID().toString();
        Notification notification = new Notification(UUID, NotificationType.QUESTION, title, message,
                detail, null, null, false,
                null, false, false);
        GUI gui = createAndGetGui(notification);
        gui.openAndWait();
        return notification.getQuestionResponse();
    }

    @Override
    public void showWarning(String title, String message, String detail) {
        String UUID = java.util.UUID.randomUUID().toString();
        Notification notification = new Notification(UUID, NotificationType.WARNING, title, message,
                detail, null, null, false,
                null, false, false);
        notificationFacade.addNotification(notification);
    }

    private GUI createAndGetGui(Notification notification) {
        return new GUI() {
            @Override
            public Dictionary openAndWait() {
                notificationFacade.addNotification(notification);
                try {
                    synchronized (notification) {
                        notification.wait();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (notification.getFormResponse() != null) {
                    List<Property> formResponse = notification.getFormResponse();
                    Dictionary<String, String> dictionary = new Hashtable<>();
                    for (Property property : formResponse) {
                        dictionary.put(property.getKey(), property.getValue());
                    }

                    return dictionary;
                }
                return null;
            }

            @Override
            public void open() {
                notificationFacade.addNotification(notification);
            }

            @Override
            public void close() {
                notificationFacade.closeNotification(notification.getId());
                //Removing the notification as the algorithm has decided to close
                notificationFacade.removeNotification(notification.getId());
            }

            @Override
            public boolean isClosed() {
                return notification.getIsClosed();
            }

            @Override
            public void setSelectionListener(SelectionListener selectionListener) {
                if (selectionListener != null) {
                    notificationFacade.getNotificationUpdatedObservable().filter(notification1 -> notification1.getId().equals(notification.getId())).subscribe(new io.reactivex.Observer<Notification>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }
                        @Override
                        public void onNext(Notification notification) {
                            if ((notification.getType().equals(NotificationType.FORM) && notification.getFormResponse() != null)) {
                                Dictionary<String, Object> dictionary = null;
                                List<Property> responses = notification.getFormResponse();
                                if (responses != null) {
                                    dictionary = new Hashtable<>();
                                }
                                for (Property response : responses) {
                                    dictionary.put(response.getKey(), response.getValue());
                                }
                                selectionListener.hitOk(dictionary);
                            } else {
                                selectionListener.cancelled();
                            }
                        }
                        @Override
                        public void onError(Throwable e) {
                        }
                        @Override
                        public void onComplete() {
                        }
                    });
                }
            }
        };
    }
}