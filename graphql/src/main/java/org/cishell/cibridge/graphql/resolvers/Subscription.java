package org.cishell.cibridge.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLSubscriptionResolver;
import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.model.*;
import org.reactivestreams.Publisher;

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

	public Publisher<AlgorithmDefinition> algorithmDefinitionAdded() {
		return this.cibridge.algorithm.algorithmDefinitionAdded();
	}

	public Publisher<AlgorithmDefinition> algorithmDefinitionRemoved() {
		return this.cibridge.algorithm.algorithmDefinitionRemoved();
	}

	public Publisher<AlgorithmInstance> algorithmInstanceUpdated(AlgorithmFilter filter) {
		return this.cibridge.algorithm.algorithmInstanceUpdated(filter);
	}

	public Publisher<Notification> notificationAdded() {
		return this.cibridge.notification.notificationAdded();
	}

	public Publisher<Notification> notificationUpdated() {
		return this.cibridge.notification.notificationUpdated();
	}

	public Publisher<Data> dataAdded() {
		return this.cibridge.data.dataAdded();
	}

	public Publisher<Data> dataRemoved() {
		return this.cibridge.data.dataRemoved();
	}

	public Publisher<Data> dataUpdated() {
		return this.cibridge.data.dataUpdated();
	}

	public Boolean schedulerCleared() {
		return this.cibridge.scheduler.schedulerCleared();
	}

	public Boolean schedulerRunningChanged() {
		return this.cibridge.scheduler.schedulerRunningChanged();
	}

	public Publisher<Log> logAdded(List<LogLevel> logLevels) {
		return this.cibridge.logging.logAdded(logLevels);
	}
}
