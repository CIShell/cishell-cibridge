package org.cishell.cibridge.cishell.impl;

import org.cishell.cibridge.core.model.Notification;
import org.cishell.cibridge.core.model.NotificationFilter;
import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.model.NotificationQueryResults;
import org.cishell.cibridge.core.model.NotificationResponse;
import org.osgi.framework.BundleContext;

public class CIShellCIBridgeNotificationFacade implements CIBridge.NotificationFacade {
	private final BundleContext context;

	public CIShellCIBridgeNotificationFacade(BundleContext context) {
		this.context = context;
	}

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
	public Boolean setAlgorithmCancelled(String algorithmInstanceId, Boolean isCancelled) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean setAlgorithmPaused(String algorithmInstanceId, Boolean isPaused) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean removeAlgorithm(String algorithmInstanceId) {
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
