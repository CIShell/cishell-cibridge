package org.cishell.cibridge.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLSubscriptionResolver;
import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.model.*;

import java.util.List;

public class Subscription implements GraphQLSubscriptionResolver {
    private CIBridge cibridge;

    public Subscription(CIBridge cibridge) {
        this.cibridge = cibridge;
        System.out.println("Subscription initialized");
    }

    public void setCIBridge(CIBridge cibridge) {
        this.cibridge = cibridge;
    }

    public CIBridge getCIBridge() {
        return this.cibridge;
    }

    public AlgorithmDefinition algorithmDefinitionAdded() {
        return null;
    }

    public AlgorithmDefinition algorithmDefinitionRemoved() {
        return null;
    }

    public AlgorithmInstance algorithmInstanceUpdated(AlgorithmFilter filter) {
        return null;
    }

    public Notification notificationAdded() {
        return null;
    }

    public Notification notificationUpdated() {
        return null;
    }

    public Data dataAdded() {
        return null;
    }

    public Data dataRemoved() {
        return null;
    }

    public Data dataUpdated() {
        return null;
    }

    public Boolean schedulerCleared() {
        return Boolean.TRUE;
    }

    public Boolean schedulerRunningChanged() {
        return Boolean.TRUE;
    }

    public Log logAdded(List<LogLevel> logLevels) {
        return null;
    }
}
