package org.cishell.cibridge.cishell.impl;

import com.google.common.base.Preconditions;
import io.reactivex.Flowable;
import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.model.AlgorithmInstance;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.ProgressTrackable;
import org.osgi.framework.ServiceReference;
import org.reactivestreams.Publisher;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
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

    @Override
    public Integer getSchedulerQueueWaiting() {
        int count = 0;

        for (AlgorithmInstance algorithmInstance : cibridge.cishellAlgorithm.getAlgorithmInstanceMap().values()) {
            switch (algorithmInstance.getState()) {
                case IDLE:
                case PAUSED:
                case WAITING:
                    ++count;
            }
        }

        return count;
    }

    /* Mutations */

    @Override
    public Boolean setAlgorithmCancelled(String algorithmInstanceId, Boolean cancel) {
        CIShellCIBridgeAlgorithmInstance algorithmInstance = getAlgorithmInstance(algorithmInstanceId);

        if (!(algorithmInstance.getState() == RUNNING ||
                algorithmInstance.getState() == PAUSED ||
                algorithmInstance.getState() == WAITING)) {
            return false;
        }

        if (setProgressMonitorImpl(algorithmInstance)) {
            if (cancel) {
                ProgressTrackable progressTrackableAlgorithm = (ProgressTrackable) algorithmInstance.getAlgorithm();
                progressTrackableAlgorithm.getProgressMonitor().setCanceled(true);
                return algorithmInstance.getState().equals(CANCELLED);
            }
        }

        return false;
    }


    @Override
    public Boolean setAlgorithmPaused(String algorithmInstanceId, Boolean pause) {
        CIShellCIBridgeAlgorithmInstance algorithmInstance = getAlgorithmInstance(algorithmInstanceId);

        if (setProgressMonitorImpl(algorithmInstance)) {
            ProgressTrackable progressTrackableAlgorithm = (ProgressTrackable) algorithmInstance.getAlgorithm();
            if (pause && algorithmInstance.getState() == RUNNING) {
                progressTrackableAlgorithm.getProgressMonitor().setPaused(true);
            } else if (!pause && algorithmInstance.getState() == PAUSED) {
                progressTrackableAlgorithm.getProgressMonitor().setPaused(false);
            } else {
                return false;
            }

            return algorithmInstance.getState().equals(PAUSED) == pause;
        }

        return false;
    }

    @Override
    public Boolean removeAlgorithm(String algorithmInstanceId) {
        CIShellCIBridgeAlgorithmInstance algorithmInstance = getAlgorithmInstance(algorithmInstanceId);
        Algorithm algorithm = algorithmInstance.getAlgorithm();

        //unschedule the algorithm
        cibridge.getSchedulerService().unschedule(algorithm);

        //set cancelled
        if (setProgressMonitorImpl(algorithmInstance)) {
            ProgressTrackable progressTrackableAlgorithm = (ProgressTrackable) algorithmInstance.getAlgorithm();
            progressTrackableAlgorithm.getProgressMonitor().setCanceled(true);
        }

        //remove the algorithm from cibridge cache
        cibridge.cishellAlgorithm.getCIShellAlgorithmCIBridgeAlgorithmMap().remove(algorithm);
        cibridge.cishellAlgorithm.getAlgorithmInstanceMap().remove(algorithmInstanceId);

        return true;
    }

    @Override
    public Boolean runAlgorithmNow(String algorithmInstanceId) {
        CIShellCIBridgeAlgorithmInstance algorithmInstance = getAlgorithmInstance(algorithmInstanceId);
        setProgressMonitorImpl(algorithmInstance);

        if (algorithmInstance.getState() == SCHEDULED) {
            unscheduleAlgorithm(algorithmInstanceId);
        }else if(algorithmInstance.getState() != IDLE){
            return false;
        }

        cibridge.getSchedulerService().runNow(algorithmInstance.getAlgorithm(), getServiceReference(algorithmInstanceId));
        return true;
    }

    @Override
    public Boolean scheduleAlgorithm(String algorithmInstanceId, ZonedDateTime date) {
        CIShellCIBridgeAlgorithmInstance algorithmInstance = getAlgorithmInstance(algorithmInstanceId);
        setProgressMonitorImpl(algorithmInstance);

        if(algorithmInstance.getState() == IDLE){
            cibridge.getSchedulerService().schedule(algorithmInstance.getAlgorithm(), getServiceReference(algorithmInstanceId), GregorianCalendar.from(date));
        }else if(algorithmInstance.getState() == SCHEDULED){
            rescheduleAlgorithm(algorithmInstanceId, date);
        }else{
            return false;
        }

        algorithmInstance.setScheduledRunTime(date);
        algorithmInstance.setState(SCHEDULED);
        return true;
    }

    @Override
    public Boolean rescheduleAlgorithm(String algorithmInstanceId, ZonedDateTime date) {
        CIShellCIBridgeAlgorithmInstance algorithmInstance = getAlgorithmInstance(algorithmInstanceId);
        setProgressMonitorImpl(algorithmInstance);

        if (algorithmInstance.getState() != SCHEDULED) {
            return false;
        }

        boolean isAlreadyScheduled = cibridge.getSchedulerService().reschedule(algorithmInstance.getAlgorithm(), GregorianCalendar.from(date));
        if (!isAlreadyScheduled) {
            System.out.println("came here");
            return false;
        }
        algorithmInstance.setScheduledRunTime(date);
        algorithmInstance.setState(SCHEDULED);
        return true;
    }

    @Override
    public Boolean unscheduleAlgorithm(String algorithmInstanceId) {
        CIShellCIBridgeAlgorithmInstance algorithmInstance = getAlgorithmInstance(algorithmInstanceId);
        Algorithm algorithm = algorithmInstance.getAlgorithm();

        if(algorithmInstance.getState() != SCHEDULED){
            return false;
        }

        boolean unscheduled = cibridge.getSchedulerService().unschedule(algorithm);
        if (!unscheduled) {
            return false;
        }

        algorithmInstance.setState(IDLE);
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

    private CIShellCIBridgeAlgorithmInstance getAlgorithmInstance(String algorithmInstanceId) {
        Preconditions.checkArgument(cibridge.cishellAlgorithm.getAlgorithmInstanceMap().containsKey(algorithmInstanceId), "No algorithm found with id '%s'", algorithmInstanceId);
        return cibridge.cishellAlgorithm.getAlgorithmInstanceMap().get(algorithmInstanceId);
    }

    private ServiceReference getServiceReference(String algorithmInstanceId) {
        return cibridge.getSchedulerService().getServiceReference(getAlgorithmInstance(algorithmInstanceId).getAlgorithm());
    }

    private boolean setProgressMonitorImpl(CIShellCIBridgeAlgorithmInstance algorithmInstance) {
        //if ProgressTrackable, supply cibridge implementation of progress monitor
        if (algorithmInstance.getAlgorithm() instanceof ProgressTrackable) {
            ProgressTrackable progressTrackableAlgorithm = (ProgressTrackable) algorithmInstance.getAlgorithm();
            if (!(progressTrackableAlgorithm.getProgressMonitor() instanceof ProgressMonitorImpl)) {
                progressTrackableAlgorithm.setProgressMonitor(new ProgressMonitorImpl(cibridge, algorithmInstance));
            }
            return true;
        } else {
            return false;
        }
    }

    private void log(int logLevel, String message) {
        cibridge.getLogService().log(logLevel, message);
    }
}
