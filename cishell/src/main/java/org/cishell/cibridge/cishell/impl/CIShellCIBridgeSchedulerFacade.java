package org.cishell.cibridge.cishell.impl;

import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.api.ProgressTrackableAlgorithm;
import org.cishell.cibridge.core.model.AlgorithmDataObject;
import org.cishell.cibridge.core.model.AlgorithmInstance;
import org.cishell.framework.algorithm.Algorithm;
import org.osgi.framework.ServiceReference;

import java.time.ZonedDateTime;
import java.util.GregorianCalendar;
import java.util.Map;

import static org.cishell.cibridge.core.model.AlgorithmState.*;

public class CIShellCIBridgeSchedulerFacade implements CIBridge.SchedulerFacade {
    private CIShellCIBridge cibridge;

    public void setCIBridge(CIShellCIBridge cibridge) {
        this.cibridge = cibridge;
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
        for (Map.Entry<String, AlgorithmDataObject> entry : cibridge.algorithmDataMap.entrySet()) {

            AlgorithmInstance algorithmInstace = entry.getValue().getAlgorithmInstance();
            if (algorithmInstace.getState() == IDLE ||
                    algorithmInstace.getState() == PAUSED ||
                    algorithmInstace.getState() == RUNNING ||
                    algorithmInstace.getState() == WAITING) {
                count++;
            }
        }
        return count;
        //TODO another crude of doing this is below. But I think the above code is more reliable if cibridge always stays in sync with cishell
        //TODO read the documentation here: http://cishell.org/dev/docs/spec/api-1.0/org/cishell/app/service/scheduler/SchedulerService.html#getScheduledAlgorithms()
        //return scheduler.getScheduledAlgorithms().length;
    }

    /* Mutations */

    //TODO what do we return here?? was the operation success or not?
    @Override
    public Boolean setAlgorithmCancelled(String algorithmInstanceId, Boolean isCancelled) {
        Algorithm algorithm = getAlgorithm(algorithmInstanceId);
        if (algorithm instanceof ProgressTrackableAlgorithm) {
            ProgressTrackableAlgorithm progressTrackableAlgorithm = (ProgressTrackableAlgorithm) algorithm;
            progressTrackableAlgorithm.getProgressMonitor().setCanceled(isCancelled);
            return true;
        }
        return false;
    }

    @Override
    public Boolean setAlgorithmPaused(String algorithmInstanceId, Boolean isPaused) {
        Algorithm algorithm = getAlgorithm(algorithmInstanceId);
        if (algorithm instanceof ProgressTrackableAlgorithm) {
            ProgressTrackableAlgorithm progressTrackableAlgorithm = (ProgressTrackableAlgorithm) algorithm;
            progressTrackableAlgorithm.getProgressMonitor().setPaused(isPaused);
            return true;
        }
        return false;
    }

    @Override
    public Boolean removeAlgorithm(String algorithmInstanceId) {
        AlgorithmDataObject algoData = cibridge.algorithmDataMap.remove(algorithmInstanceId);
        if (algoData == null) {
            System.out.println("Algorithm not present");
            return false;
        }

        //TODO no way of removing algorithms from CIShell??
        return true;
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
            System.out.println("algorithm not scheduled before");
            return false;
        }

        AlgorithmInstance algorithmInstance = getAlgorithmInstance(algorithmInstanceId);
        algorithmInstance.setScheduledRunTime(date);
        algorithmInstance.setState(SCHEDULED);
        return true;
    }

    @Override
    public Boolean unscheduleAlgorithm(String algorithmInstanceId) {
        boolean isUnscheduled = cibridge.getSchedulerService().unschedule(getAlgorithm(algorithmInstanceId));
        if (isUnscheduled) {
            getAlgorithmInstance(algorithmInstanceId).setState(IDLE);
            return true;
        }
        return false;
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

    //TODO implement subscriptions methods below
    @Override
    public Boolean schedulerCleared() {
        return null;
    }

    @Override
    public Boolean schedulerRunningChanged() {
        return null;
    }

    private Algorithm getAlgorithm(String algorithmInstanceId) {
        return cibridge.algorithmDataMap.get(algorithmInstanceId).getAlgorithm();
    }

    private AlgorithmInstance getAlgorithmInstance(String algorithmInstanceId) {
        return cibridge.algorithmDataMap.get(algorithmInstanceId).getAlgorithmInstance();
    }

    private ServiceReference getServiceReference(String algorithmInstanceId) {
        return cibridge.getSchedulerService().getServiceReference(getAlgorithm(algorithmInstanceId));
    }

}
