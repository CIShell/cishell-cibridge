package org.cishell.cibridge.cishell.impl;

import com.google.common.base.Preconditions;
import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.model.AlgorithmInstance;
import org.cishell.cibridge.core.model.AlgorithmState;
import org.cishell.framework.algorithm.ProgressMonitor;

import static org.cishell.cibridge.core.model.AlgorithmState.*;

public class ProgressMonitorImpl implements ProgressMonitor {

    private CIBridge cibridge;
    private AlgorithmInstance algorithmInstance;
    private int capabilities = 0;
    private double totalWorkUnits = 0;
    private AlgorithmState previousState;
    private boolean paused = false;
    private boolean canceled = false;

    public ProgressMonitorImpl(CIBridge cibridge, AlgorithmInstance algorithmInstance) {
        this.cibridge = cibridge;
        this.algorithmInstance = algorithmInstance;
    }

    @Override
    public void start(int capabilities, int totalWorkUnits) {
        start(capabilities, (double) totalWorkUnits);
        //todo call subscription method
    }

    @Override
    public void start(int capabilities, double totalWorkUnits) {
        Preconditions.checkArgument(totalWorkUnits > 0, "total work units should be a positive value");
        this.capabilities = capabilities;
        this.totalWorkUnits = totalWorkUnits;
        algorithmInstance.setProgress(0);
        algorithmInstance.setState(RUNNING);
        //todo call subscription method
    }

    @Override
    public void worked(int work) {
        worked((double) work);
    }

    @Override
    public void worked(double work) {
        if ((capabilities & WORK_TRACKABLE) > 0) {
            algorithmInstance.setProgress((int) (work * 100 / totalWorkUnits));
            System.out.println("updating progress: " + algorithmInstance.getProgress());
            //todo call subscription method
        }
    }

    @Override
    public void done() {
        algorithmInstance.setProgress(100);
        algorithmInstance.setState(FINISHED);
        //todo call subscription method
    }

    @Override
    public void setCanceled(boolean canceled) {
        if ((capabilities & CANCELLABLE) > 0) {
            //cancel the algorithm. currently you cant uncancel an already canceled algorithm
            if (canceled) {
                this.canceled = true;
                algorithmInstance.setState(CANCELLED);
                //todo call subscription method
            }
        }
    }

    @Override
    public boolean isCanceled() {
        return this.canceled;
    }

    @Override
    public void setPaused(boolean paused) {
        if ((capabilities & PAUSEABLE) > 0) {
            //pause the algorithm
            if (paused) {
                this.paused = true;
                previousState = algorithmInstance.getState();
                algorithmInstance.setState(PAUSED);
            }
            //resume the algorithm
            else {
                this.paused = false;
                algorithmInstance.setState(previousState);
                synchronized (this) {
                    this.notify();
                }
            }

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
