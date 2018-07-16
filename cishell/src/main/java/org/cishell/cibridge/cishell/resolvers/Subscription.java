package org.cishell.cibridge.cishell.resolvers;

import com.coxautodev.graphql.tools.GraphQLSubscriptionResolver;
import org.cishell.cibridge.cishell.data.AlgorithmDefinationCIShell;
import org.cishell.cibridge.core.model.*;

import java.util.List;

public class Subscription implements GraphQLSubscriptionResolver{
    AlgorithmDefinationCIShell algorithmDefinationOSGICIShell;
    public Subscription(AlgorithmDefinationCIShell algorithmDefinationOSGICIShell) {
        this.algorithmDefinationOSGICIShell = algorithmDefinationOSGICIShell;
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
