package org.cishell.cibridge.cishell.impl;

import com.google.common.base.Preconditions;
import org.cishell.app.service.scheduler.SchedulerListener;
import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.cibridge.core.model.AlgorithmInstance;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;

import static org.cishell.cibridge.core.model.AlgorithmState.*;

public class SchedulerListenerImpl implements SchedulerListener {

    private CIShellCIBridge cibridge;

    public void setCIBridge(CIShellCIBridge cibridge) {
        Preconditions.checkNotNull(cibridge, "cibridge cannot be null");
        this.cibridge = cibridge;
        this.cibridge.getSchedulerService().addSchedulerListener(this);
    }

    @Override
    public void algorithmScheduled(Algorithm algorithm, Calendar calendar) {
        System.out.println("Algorithm Scheduled");
        AlgorithmInstance algorithmInstance = getAlgorithmInstance(algorithm);
        algorithmInstance.setState(SCHEDULED);
        setScheduledRunTime(algorithmInstance, calendar);
        //todo call subscription method
    }

    @Override
    public void algorithmRescheduled(Algorithm algorithm, Calendar calendar) {
        System.out.println("Algorithm Re-scheduled");
        algorithmScheduled(algorithm, calendar);
        //todo call subscription method
    }

    @Override
    public void algorithmUnscheduled(Algorithm algorithm) {
        getAlgorithmInstance(algorithm).setState(IDLE);
        //todo call subscription method
    }

    @Override
    public void algorithmStarted(Algorithm algorithm) {
        getAlgorithmInstance(algorithm).setState(RUNNING);
        //todo call subscription method
    }

    @Override
    public void algorithmFinished(Algorithm algorithm, Data[] data) {
        getAlgorithmInstance(algorithm).setState(FINISHED);
        //todo call subscription method
    }

    @Override
    public void algorithmError(Algorithm algorithm, Throwable throwable) {
        getAlgorithmInstance(algorithm).setState(ERRORED);
        //todo call subscription method
    }

    @Override
    public void schedulerRunStateChanged(boolean b) {
    }

    @Override
    public void schedulerCleared() {
        //TODO nothing to update on cibridge side
    }

    private AlgorithmInstance getAlgorithmInstance(Algorithm algorithm) {
        return cibridge.cishellAlgorithm.getCishellAlgorithmCIBridgeAlgorithmMap().get(algorithm);

    }

    private void setScheduledRunTime(AlgorithmInstance algorithmInstance, Calendar calendar) {
        ZonedDateTime zonedDateTime = calendar.toInstant().atZone(ZoneId.systemDefault());
        algorithmInstance.setScheduledRunTime(zonedDateTime);
    }
}
