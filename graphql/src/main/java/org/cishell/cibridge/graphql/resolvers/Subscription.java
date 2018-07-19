package org.cishell.cibridge.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLSubscriptionResolver;
import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.model.*;

import java.util.List;

public class Subscription implements GraphQLSubscriptionResolver{
	private CIBridge cibridge;
	
	public Subscription(CIBridge cibridge){
		this.cibridge = cibridge;
		System.out.println("Subscription initialized");
	}
	
	public void setCIBridge(CIBridge cibridge) {
		this.cibridge = cibridge;
	}
	public CIBridge getCIBridge() {
		return this.cibridge;
	}
	
    AlgorithmDefinition algorithmDefinitionAdded(){
        return null;
    }
    AlgorithmDefinition algorithmDefinitionRemoved(){
        return null;
    }
    AlgorithmInstance algorithmInstanceUpdated(AlgorithmFilter filter){
        return null;
    }

    Notification notificationAdded(){
        return null;
    }
    Notification notificationUpdated(){
        return null;
    }
    Data dataAdded(){
        return null;
    }
    Data dataRemoved(){
        return null;
    }
    Data dataUpdated(){
        return null;
    }

    Boolean schedulerCleared(){
        return Boolean.TRUE;
    }
    Boolean schedulerRunningChanged(){
        return Boolean.TRUE;
    }
    Log logAdded(List<LogLevel> logLevels){
        return null;
    }
}
