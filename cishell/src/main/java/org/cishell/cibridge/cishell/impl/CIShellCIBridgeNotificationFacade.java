package org.cishell.cibridge.cishell.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Hashtable;

import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.model.Notification;
import org.cishell.cibridge.core.model.NotificationFilter;
import org.cishell.cibridge.core.model.NotificationQueryResults;
import org.cishell.cibridge.core.model.NotificationResponse;
import org.cishell.cibridge.core.model.PageInfo;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.service.guibuilder.GUIBuilderService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.cishell.cibridge.cishell.impl.CIBridgeGUIBuilderService;
import org.osgi.framework.Constants;

public class CIShellCIBridgeNotificationFacade implements CIBridge.NotificationFacade {
	private CIShellCIBridge cibridge;
	private HashMap<String,Notification> notificationMap = new HashMap<String,Notification>();

	public void setCIBridge(CIShellCIBridge cibridge) {
		this.cibridge = cibridge;
	}
	//FIXME (Check all the implementation of methods and test it).
	private void registerGUIBuilderService() {
		//this.cibridge.getBundleContext().registerService(GUIBuilderService.class.getName(), new CIBridgeGUIBuilderService(cibridge), new Hashtable<String,String>());

	}

	@Override
	public NotificationQueryResults getNotifications(NotificationFilter filter) {
		// TODO Auto-generated method stub
	/*List<Notification> notifications = new ArrayList<>();
	BundleContext context = cibridge.getBundleContext();
	PageInfo pageInfo = new PageInfo(false,false); 
	NotificationQueryResults queryResults = null;
	try {
		if(filter != null) {
			System.out.println("Filter :"+filter);
			if(filter.getID() != null) {
			for(String pids: filter.getID()) {
				if(notificationMap.containsKey(pids)){
					notifications.add(notificationMap.get(pids));
				}
			}
		}		
		}else {
			System.out.println("Filter is empty!");	
			
	}
		queryResults = new NotificationQueryResults(notifications, pageInfo);	
	}
	catch(Exception e) {
		e.printStackTrace();
	}
*/	return null;
}

@Override
public Boolean isClosed(String NotificationId) {
	// TODO Auto-generated method stub
	
	/*if(notificationMap.containsKey(NotificationId))
		return notificationMap.get(NotificationId).getIsClosed();
	*/
	return null;
}


public Boolean setNotificationResponse(String notificationId, NotificationResponse response) {
	// TODO Auto-generated method stub
	/*if(notificationMap.containsKey(notificationId)) {
		Notification temp = notificationMap.get(notificationId);
		temp.setClosed( response.getCloseNotification());
		temp.setConfirmationResponse(response.getConfirmationResponse());
		temp.setFormResponse(response.getFormResponse());
		temp.setQuestionResponse(response.getQuestionResponse());
		notificationMap.put(notificationId,temp);
		return true;
	}*/
		return false;
}



public Boolean closeNotification(String notificationId) {
	/*if(notificationMap.containsKey(notificationId)) {
		Notification temp = notificationMap.get(notificationId);
		if(temp.getIsClosed() == true) {
				System.out.println("The notification is already closed");
		} 
		else{
		temp.setClosed(true);
		notificationMap.put(notificationId,temp);
		return true;
		}
	}	*/
	return false;
}

//TODO Subscription methods below
public Notification notificationAdded() {
	//TODO Auto-generated method stub
	return null;
}


public Notification notificationUpdated() {
	//TODO Auto-generated method stub
		return null;
	}

}


