package org.cishell.cibridge.cishell.impl;

import com.google.common.base.Preconditions;
import org.cishell.app.service.scheduler.SchedulerListener;
import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.cibridge.core.model.AlgorithmInstance;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.ProgressTrackable;
import org.cishell.framework.data.Data;

import java.time.ZoneId;
import java.util.Calendar;

import static org.cishell.cibridge.core.model.AlgorithmState.*;

public class SchedulerListenerImpl implements SchedulerListener {

    private CIShellCIBridge cibridge;

    public SchedulerListenerImpl() {
        System.out.println("Listener created");
    }

    public void setCIBridge(CIShellCIBridge cibridge) {
        Preconditions.checkNotNull(cibridge, "cibridge cannot be null");
        this.cibridge = cibridge;
        this.cibridge.getSchedulerService().addSchedulerListener(this);
    }

    @Override
    public void algorithmScheduled(Algorithm algorithm, Calendar calendar) {
        System.out.println("Algorithm scheduled");
        AlgorithmInstance algorithmInstance = getAlgorithmInstance(algorithm);
        algorithmInstance.setState(SCHEDULED);
        algorithmInstance.setScheduledRunTime(calendar.toInstant().atZone(ZoneId.systemDefault()));
        //todo call subscription method
    }

    @Override
    public void algorithmRescheduled(Algorithm algorithm, Calendar calendar) {
        algorithmScheduled(algorithm, calendar);
        //todo call subscription method
    }

    @Override
    public void algorithmUnscheduled(Algorithm algorithm) {
        System.out.println("Algorithm unscheduled");
        getAlgorithmInstance(algorithm).setState(IDLE);
        //todo call subscription method
    }

    @Override
    public void algorithmStarted(Algorithm algorithm) {
        System.out.println("Algorithm started running");
        getAlgorithmInstance(algorithm).setState(RUNNING);
        //todo call subscription method
    }

    @Override
    public void algorithmFinished(Algorithm algorithm, Data[] data) {
        AlgorithmInstance algorithmInstance = getAlgorithmInstance(algorithm);

        if (algorithmInstance.getState() == ERRORED) {
            return;
        }

        if (algorithm instanceof ProgressTrackable) {
            if (((ProgressTrackable) algorithm).getProgressMonitor().isCanceled()) {
                System.out.println("Algorithm cancelled");
                getAlgorithmInstance(algorithm).setState(CANCELLED);
                return;
            }
        }

        System.out.println("Algorithm finished");
        getAlgorithmInstance(algorithm).setState(FINISHED);

        //todo call subscription method
    }

    @Override
    public void algorithmError(Algorithm algorithm, Throwable throwable) {
        System.out.println("Algorithm errored");
        getAlgorithmInstance(algorithm).setState(ERRORED);
        throwable.printStackTrace();
        //todo call subscription method
    }

    @Override
    public void schedulerRunStateChanged(boolean b) {
        System.out.println("scheduler run state: " + b);
    }

    @Override
    public void schedulerCleared() {
        System.out.println("scheduler cleared");
    }

    private AlgorithmInstance getAlgorithmInstance(Algorithm algorithm) {
        return cibridge.cishellAlgorithm.getCIShellAlgorithmCIBridgeAlgorithmMap().get(algorithm);

    }
}
