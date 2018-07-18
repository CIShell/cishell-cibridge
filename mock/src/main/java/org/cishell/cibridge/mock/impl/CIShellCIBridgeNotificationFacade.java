package org.cishell.cibridge.mock.impl;

import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.model.NotificationFilter;
import org.cishell.cibridge.core.model.NotificationQueryResults;
import org.osgi.framework.BundleContext;

public class CIShellCIBridgeNotificationFacade implements CIBridge.NotificationFacade {
	
	private final BundleContext context;
	
	public CIShellCIBridgeNotificationFacade(BundleContext context) {
		this.context=context;
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

}
