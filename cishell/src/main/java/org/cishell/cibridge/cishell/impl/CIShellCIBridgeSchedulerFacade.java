package org.cishell.cibridge.cishell.impl;

import com.google.common.base.Preconditions;
import io.reactivex.Flowable;
import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.model.AlgorithmInstance;
import org.cishell.framework.algorithm.Algorithm;
import org.osgi.framework.ServiceReference;
import org.reactivestreams.Publisher;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.cishell.cibridge.core.model.AlgorithmState.*;

public class CIShellCIBridgeSchedulerFacade implements CIBridge.SchedulerFacade {
    private CIShellCIBridge cibridge;
    private SchedulerListenerImpl schedulerListener = new SchedulerListenerImpl();

    public void setCIBridge(CIShellCIBridge cibridge) {
        Preconditions.checkNotNull(cibridge, "cibridge cannot be null");
        this.cibridge = cibridge;
        this.schedulerListener.setCIBridge(cibridge);
    }

    /* Queries */
    @Override
    public Boolean isSchedulerEmpty() {
        return cibridge.getSchedulerService().isEmpty();
    }

    @Override
    public Boolean isSchedulerRunning() {
        return cibridge.getSchedulerService().isRunning();
    }


    //todo what is the definition of queue waiting?
    @Override
    public Integer getSchedulerQueueWaiting() {
        int count = 0;
        for (AlgorithmInstance algorithmInstance : getAlgorithmInstanceMap().values()) {
            if (algorithmInstance.getState() == IDLE ||
                    algorithmInstance.getState() == PAUSED ||
                    algorithmInstance.getState() == RUNNING ||
                    algorithmInstance.getState() == WAITING) {
                count++;
            }
        }
        return count;
    }

    /* Mutations */

    @Override
    public Boolean setAlgorithmCancelled(String algorithmInstanceId, Boolean isCancelled) {
        CIShellCIBridgeAlgorithmInstance algorithmInstance = getAlgorithmInstanceMap().get(algorithmInstanceId);
        return false;
    }

    @Override
    public Boolean setAlgorithmPaused(String algorithmInstanceId, Boolean isPaused) {
        return false;
    }

    @Override
    public Boolean removeAlgorithm(String algorithmInstanceId) {
        CIShellCIBridgeAlgorithmInstance algorithmInstance = getAlgorithmInstanceMap().remove(algorithmInstanceId);
        return algorithmInstance != null;
    }

    @Override
    public Boolean runAlgorithmNow(String algorithmInstanceId) {
        cibridge.getSchedulerService().runNow(getAlgorithm(algorithmInstanceId), getServiceReference(algorithmInstanceId));
        return true;
    }

    @Override
    public Boolean scheduleAlgorithm(String algorithmInstanceId, ZonedDateTime date) {
        cibridge.getSchedulerService().schedule(getAlgorithm(algorithmInstanceId), getServiceReference(algorithmInstanceId), GregorianCalendar.from(date));
        AlgorithmInstance algorithmInstance = getAlgorithmInstance(algorithmInstanceId);
        algorithmInstance.setScheduledRunTime(date);
        algorithmInstance.setState(SCHEDULED);
        return true;
    }

    @Override
    public Boolean rescheduleAlgorithm(String algorithmInstanceId, ZonedDateTime date) {
        boolean isAlreadyScheduled = cibridge.getSchedulerService().reschedule(getAlgorithm(algorithmInstanceId), GregorianCalendar.from(date));
        if (!isAlreadyScheduled) {
            return false;
        }

        AlgorithmInstance algorithmInstance = getAlgorithmInstance(algorithmInstanceId);
        algorithmInstance.setScheduledRunTime(date);
        algorithmInstance.setState(SCHEDULED);
        return true;
    }

    @Override
    public Boolean unscheduleAlgorithm(String algorithmInstanceId) {
        boolean unscheduled = cibridge.getSchedulerService().unschedule(getAlgorithm(algorithmInstanceId));
        if (!unscheduled) {
            return false;
        }

        getAlgorithmInstance(algorithmInstanceId).setState(IDLE);
        return true;
    }

    @Override
    public Integer clearScheduler() {
        cibridge.getSchedulerService().clearSchedule();
        return cibridge.getSchedulerService().getScheduledAlgorithms().length;
    }

    @Override
    public Boolean setSchedulerRunning(Boolean running) {
        cibridge.getSchedulerService().setRunning(running);
        return cibridge.getSchedulerService().isRunning();
    }

    // TODO Update the subscriptions implementation with listeners
    @Override
    public Publisher<Boolean> schedulerCleared() {
        Boolean boolean1 = true;
        List<Boolean> results = new ArrayList<>();
        results.add(boolean1);
        return Flowable.fromIterable(results).delay(2, TimeUnit.SECONDS);
        // return Flowable.just(boolean1).;
    }

    // TODO Update the subscriptions implementation with listeners
    @Override
    public Publisher<Boolean> schedulerRunningChanged() {
        Boolean boolean1 = true;
        List<Boolean> results = new ArrayList<>();
        results.add(boolean1);
        return Flowable.fromIterable(results).delay(2, TimeUnit.SECONDS);
        // return Flowable.just(boolean1).;
    }

    private Map<String, CIShellCIBridgeAlgorithmInstance> getAlgorithmInstanceMap() {
        return cibridge.cishellAlgorithm.getAlgorithmInstanceMap();
    }

    private Algorithm getAlgorithm(String algorithmInstanceId) {
        return cibridge.cishellAlgorithm.getAlgorithmInstanceMap().get(algorithmInstanceId).getAlgorithm();
    }

    private AlgorithmInstance getAlgorithmInstance(String algorithmInstanceId) {
        return cibridge.cishellAlgorithm.getAlgorithmInstanceMap().get(algorithmInstanceId);
    }

    private ServiceReference getServiceReference(String algorithmInstanceId) {
        return cibridge.getSchedulerService().getServiceReference(getAlgorithm(algorithmInstanceId));
    }

}
