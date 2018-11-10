package org.cishell.cibridge.mock.impl;

import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.model.Log;
import org.cishell.cibridge.core.model.LogFilter;
import org.cishell.cibridge.core.model.LogLevel;
import org.cishell.cibridge.core.model.LogQueryResults;

import java.util.List;

public class CIShellCIBridgeLoggingFacade implements CIBridge.LoggingFacade {

    @Override
    public LogQueryResults getLogs(LogFilter filter) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Log logAdded(List<LogLevel> logLevels) {
        // TODO Auto-generated method stub
        return null;
    }

}
