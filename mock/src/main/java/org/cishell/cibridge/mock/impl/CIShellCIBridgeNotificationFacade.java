package org.cishell.cibridge.mock.impl;

import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.model.Notification;
import org.cishell.cibridge.core.model.NotificationFilter;
import org.cishell.cibridge.core.model.NotificationQueryResults;
import org.cishell.cibridge.core.model.NotificationResponse;

public class CIShellCIBridgeNotificationFacade implements CIBridge.NotificationFacade {

	@Override
	public NotificationQueryResults getNotifications(NotificationFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isClosed(String NotificationId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean setNotificationResponse(String notificationId, NotificationResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean closeNotification(String notificationId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Notification notificationAdded() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Notification notificationUpdated() {
		// TODO Auto-generated method stub
		return null;
	}

}
