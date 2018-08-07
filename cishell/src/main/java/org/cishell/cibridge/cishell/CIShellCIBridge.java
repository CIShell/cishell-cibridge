package org.cishell.cibridge.cishell;

import org.cishell.app.service.datamanager.DataManagerService;
import org.cishell.app.service.scheduler.SchedulerService;
import org.cishell.cibridge.cishell.impl.*;
import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.model.AlgorithmDataObject;
import org.cishell.cibridge.core.model.AlgorithmFactoryDataObject;
import org.cishell.cibridge.core.model.PageInfo;
import org.cishell.cibridge.core.model.interfaces.QueryResults;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.service.conversion.DataConversionService;
import org.cishell.service.guibuilder.GUIBuilderService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.MetaTypeService;

import java.util.HashMap;
import java.util.List;

public class CIShellCIBridge extends CIBridge {
    private BundleContext context;

    public final CIShellCIBridgeAlgorithmFacade cishellAlgorithm;
    public final CIShellCIBridgeDataFacade cishellData;
    public final CIShellCIBridgeNotificationFacade cishellNotification;
    public final CIShellCIBridgeSchedulerFacade cishellScheduler;
    public final CIShellCIBridgeLoggingFacade cishellLogging;
    public final HashMap<String, AlgorithmDataObject> algorithmDataMap = new HashMap<>();
    public final HashMap<String, AlgorithmFactoryDataObject> algorithmFactoryDataMap = new HashMap<>();

    public CIShellCIBridge(BundleContext context) {
        super(new CIShellCIBridgeAlgorithmFacade(), new CIShellCIBridgeDataFacade(),
                new CIShellCIBridgeNotificationFacade(), new CIShellCIBridgeSchedulerFacade(),
                new CIShellCIBridgeLoggingFacade());
        this.context = context;

        this.cishellAlgorithm = (CIShellCIBridgeAlgorithmFacade) this.algorithm;
        this.cishellData = (CIShellCIBridgeDataFacade) this.data;
        this.cishellNotification = (CIShellCIBridgeNotificationFacade) this.notification;
        this.cishellScheduler = (CIShellCIBridgeSchedulerFacade) this.scheduler;
        this.cishellLogging = (CIShellCIBridgeLoggingFacade) this.logging;

        cishellAlgorithm.setCIBridge(this);
        cishellData.setCIBridge(this);
        cishellNotification.setCIBridge(this);
        cishellScheduler.setCIBridge(this);
        cishellLogging.setCIBridge(this);

        this.cishellAlgorithm.cacheData();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public AlgorithmFactory getAlgorithmFactory(String pid) {
        try {
            ServiceReference[] refs = context.getServiceReferences(AlgorithmFactory.class.getName(),
                    "(&(" + Constants.SERVICE_PID + "=" + pid + "))");
            if (refs != null && refs.length > 0) {
                return (AlgorithmFactory) context.getService(refs[0]);
            } else {
                return null;
            }
        } catch (InvalidSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public GUIBuilderService getGUIBuilderService() {
        return (GUIBuilderService) this.getService(GUIBuilderService.class);
    }

    public DataConversionService getDataConversionService() {
        return (DataConversionService) this.getService(DataConversionService.class);
    }

    public SchedulerService getSchedulerService() {
        return (SchedulerService) this.getService(SchedulerService.class);
    }

    public DataManagerService getDataManagerService() {
        return (DataManagerService) this.getService(DataManagerService.class);
    }

    public LogService getLogService() {
        return (LogService) this.getService(LogService.class);
    }

    public MetaTypeService getMetaTypeService() {
        return (MetaTypeService) this.getService(MetaTypeService.class);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Object getService(Class c) {
        ServiceReference ref = context.getServiceReference(c.getName());
        return ref != null ? context.getService(ref) : null;
    }

    public BundleContext getBundleContext() {
        return context;
    }

    public <T> QueryResults<T> getPaginatedQueryResults(QueryResults<T> queryResults, int limit, int offset) {

        int listSize = queryResults.getResults().size();
        boolean hasNext = false;
        boolean hasPrevious = false;

        List<T> queryList;
        if (offset > 0) {
            if (offset >= listSize) {
                queryList = queryResults.getResults().subList(0, 0);
            } else if (limit > 0) {
                queryList = queryResults.getResults().subList(offset, Math.min(offset + limit, listSize));
                hasPrevious = true;
                if ((offset + limit) < listSize) {
                    hasNext = true;
                }
            } else {
                queryList = queryResults.getResults().subList(offset, listSize);
                hasPrevious = true;
            }
        } else if (limit > 0) {
            queryList = queryResults.getResults().subList(0, Math.min(limit, listSize));
            if (limit < listSize) {
                hasNext = true;
            }
        } else {
            queryList = queryResults.getResults().subList(0, listSize);
        }

        PageInfo pageInfo = new PageInfo(hasNext, hasPrevious);

        return queryResults.getQueryResults(queryList, pageInfo);
    }

}
