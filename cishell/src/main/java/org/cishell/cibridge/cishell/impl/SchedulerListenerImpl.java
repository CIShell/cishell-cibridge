package org.cishell.cibridge.cishell.impl;

import org.cishell.app.service.scheduler.SchedulerListener;
import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.cibridge.core.api.ProgressTrackableAlgorithm;
import org.cishell.cibridge.core.model.AlgorithmInstance;
import org.cishell.cibridge.core.model.AlgorithmState;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;

public class SchedulerListenerImpl implements SchedulerListener {

    private CIShellCIBridge cibridge;

    public SchedulerListenerImpl(CIShellCIBridge cibridge) {
        this.cibridge = cibridge;
    }

    @Override
    public void algorithmScheduled(Algorithm algorithm, Calendar calendar) {
        setAlgorithmState(algorithm, AlgorithmState.SCHEDULED);
        setScheduledRunTime(algorithm, calendar);

    }

    @Override
    public void algorithmRescheduled(Algorithm algorithm, Calendar calendar) {
        setAlgorithmState(algorithm, AlgorithmState.SCHEDULED);
        setScheduledRunTime(algorithm, calendar);
    }

    @Override
    public void algorithmUnscheduled(Algorithm algorithm) {
        setAlgorithmState(algorithm, AlgorithmState.IDLE);
    }

    @Override
    public void algorithmStarted(Algorithm algorithm) {
        setAlgorithmState(algorithm, AlgorithmState.RUNNING);
    }

    @Override
    public void algorithmFinished(Algorithm algorithm, Data[] data) {
        setAlgorithmState(algorithm, AlgorithmState.FINISHED);
    }

    @Override
    public void algorithmError(Algorithm algorithm, Throwable throwable) {
        setAlgorithmState(algorithm, AlgorithmState.ERRORED);
        //TODO what to do with the throwable??
    }

    @Override
    public void schedulerRunStateChanged(boolean b) {
        //TODO nothing to update on cibridge side
    }

    @Override
    public void schedulerCleared() {
        //TODO nothing to update on cibridge side
    }

    private void setAlgorithmState(Algorithm algorithm, AlgorithmState algorithmState) {
        if (algorithm instanceof ProgressTrackableAlgorithm) {
            String algorithmInstanceId = ((ProgressTrackableAlgorithm) algorithm).getAlgorithmInstanceId();
            AlgorithmInstance algorithmInstance = cibridge.algorithmDataMap.get(algorithmInstanceId).getAlgorithmInstance();
            algorithmInstance.setState(algorithmState);
        }
    }

    private void setScheduledRunTime(Algorithm algorithm, Calendar calendar) {
        if (algorithm instanceof ProgressTrackableAlgorithm) {
            String algorithmInstanceId = ((ProgressTrackableAlgorithm) algorithm).getAlgorithmInstanceId();
            AlgorithmInstance algorithmInstance = cibridge.algorithmDataMap.get(algorithmInstanceId).getAlgorithmInstance();
            ZonedDateTime zonedDateTime = calendar.toInstant().atZone(ZoneId.systemDefault());
            algorithmInstance.setScheduledRunTime(zonedDateTime);
        }
    }
}
