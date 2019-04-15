package org.cishell.cibridge.cishell.impl;

import com.google.common.base.Preconditions;
import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.cibridge.core.model.AlgorithmInstance;
import org.cishell.framework.algorithm.ProgressMonitor;

import static org.cishell.cibridge.core.model.AlgorithmState.*;

public class ProgressMonitorImpl implements ProgressMonitor {

    private CIShellCIBridge cibridge;
    private AlgorithmInstance algorithmInstance;
    private int capabilities = 0;
    private double totalWorkUnits = 0;
    private boolean paused = false;
    private boolean canceled = false;

    public ProgressMonitorImpl(CIShellCIBridge cibridge, AlgorithmInstance algorithmInstance) {
        this.cibridge = cibridge;
        this.algorithmInstance = algorithmInstance;
    }

    @Override
    public void start(int capabilities, int totalWorkUnits) {
        start(capabilities, (double) totalWorkUnits);
    }

    @Override
    public void start(int capabilities, double totalWorkUnits) {
        Preconditions.checkArgument(totalWorkUnits > 0, "total work units should be a positive value");
        this.capabilities = capabilities;
        this.totalWorkUnits = totalWorkUnits;
        algorithmInstance.setProgress(0);
        algorithmInstance.setState(RUNNING);
        cibridge.cishellAlgorithm.getAlgorithmInstanceUpdatedObservableEmitter().onNext(algorithmInstance);
    }

    @Override
    public void worked(int work) {
        worked((double) work);
    }

    @Override
    public void worked(double work) {
        if ((capabilities & WORK_TRACKABLE) > 0) {
            algorithmInstance.setProgress((int) (work * 100 / totalWorkUnits));
            cibridge.cishellAlgorithm.getAlgorithmInstanceUpdatedObservableEmitter().onNext(algorithmInstance);
        }
    }

    @Override
    public void done() {
        algorithmInstance.setProgress(100);
        algorithmInstance.setState(FINISHED);
        cibridge.cishellAlgorithm.getAlgorithmInstanceUpdatedObservableEmitter().onNext(algorithmInstance);
    }

    @Override
    public void setCanceled(boolean canceled) {
        if ((capabilities & CANCELLABLE) > 0) {
            //cancel the algorithm. currently you cant uncancel an already canceled algorithm
            if (canceled) {
                this.canceled = true;
                cibridge.cishellAlgorithm.getAlgorithmInstanceUpdatedObservableEmitter().onNext(algorithmInstance);
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
                algorithmInstance.setState(PAUSED);
            }
            //resume the algorithm
            else {
                this.paused = false;
                synchronized (this) {
                    this.notify();
                }
                algorithmInstance.setState(RUNNING);
            }
            cibridge.cishellAlgorithm.getAlgorithmInstanceUpdatedObservableEmitter().onNext(algorithmInstance);
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
