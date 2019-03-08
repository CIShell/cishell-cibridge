package org.cishell.cibridge.cishell.impl;

import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.model.AlgorithmInstance;
import org.cishell.framework.algorithm.ProgressMonitor;

import static org.cishell.cibridge.core.model.AlgorithmState.*;

public class ProgressMonitorImpl implements ProgressMonitor {

    private CIBridge cibridge;
    private AlgorithmInstance algorithmInstance;
    private int capabilities = 0;
    private double totalWorkUnits = 0;
    private double worked = 0;
    private boolean paused;
    private boolean canceled;

    public ProgressMonitorImpl(CIBridge cibridge, AlgorithmInstance algorithmInstance) {
        this.cibridge = cibridge;
        this.algorithmInstance = algorithmInstance;
    }

    @Override
    public void start(int capabilities, int totalWorkUnits) {
        this.capabilities = capabilities;
        this.totalWorkUnits = totalWorkUnits;
        this.algorithmInstance.setState(RUNNING);
        //todo call subscription method
    }

    @Override
    public void start(int capabilities, double totalWorkUnits) {
        this.capabilities = capabilities;
        this.totalWorkUnits = totalWorkUnits;
        this.algorithmInstance.setState(RUNNING);
        //todo call subscription method
    }

    @Override
    public void worked(int work) {
        if ((capabilities & WORK_TRACKABLE) > 0) {
            this.worked = work;
            algorithmInstance.setProgress((int) (work * 100.0 / totalWorkUnits));
            //todo call subscription method
        }
    }

    @Override
    public void worked(double work) {
        if ((capabilities & WORK_TRACKABLE) > 0) {
            this.worked = work;
            algorithmInstance.setProgress((int) (work * 100.0 / totalWorkUnits));
            //todo call subscription method
        }
    }

    @Override
    public void done() {
        algorithmInstance.setState(FINISHED);
        //todo call subscription method
    }

    @Override
    public void setCanceled(boolean canceled) {
        if ((capabilities & CANCELLABLE) > 0) {
            this.canceled = canceled;
            algorithmInstance.setState(CANCELLED);
            //todo call subscription method
        }
    }

    @Override
    public boolean isCanceled() {
        return this.canceled;
    }

    @Override
    public void setPaused(boolean paused) {
        if ((capabilities & PAUSEABLE) > 0) {
            this.paused = paused;
            algorithmInstance.setState(PAUSED);
            //todo call subscription method
        }
    }

    @Override
    public boolean isPaused() {
        return this.paused;
    }

    @Override
    public void describeWork(String s) {

    }

}
