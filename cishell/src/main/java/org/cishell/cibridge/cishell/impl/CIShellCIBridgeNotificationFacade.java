package org.cishell.cibridge.cishell.impl;

import java.util.Hashtable;

import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.model.Notification;
import org.cishell.cibridge.core.model.NotificationFilter;
import org.cishell.cibridge.core.model.NotificationQueryResults;
import org.cishell.cibridge.core.model.NotificationResponse;
import org.cishell.service.guibuilder.GUIBuilderService;
import org.cishell.cibridge.cishell.impl.CIBridgeGUIBuilderService;

public class CIShellCIBridgeNotificationFacade implements CIBridge.NotificationFacade {
	private CIShellCIBridge cibridge;

	public void setCIBridge(CIShellCIBridge cibridge) {
		this.cibridge = cibridge;
	}
	private void registerGUIBuilderService() {
		this.cibridge.getBundleContext().registerService(GUIBuilderService.class.getName(), new CIBridgeGUIBuilderService(cibridge), new Hashtable<String,String>());

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


