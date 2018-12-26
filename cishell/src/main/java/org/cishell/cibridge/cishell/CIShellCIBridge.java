package org.cishell.cibridge.cishell;

import org.cishell.app.service.datamanager.DataManagerService;
import org.cishell.app.service.scheduler.SchedulerService;
import org.cishell.cibridge.cishell.impl.*;
import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.model.AlgorithmDataObject;
import org.cishell.cibridge.core.model.AlgorithmFactoryDataObject;
import org.cishell.framework.CIShellContext;
import org.cishell.framework.LocalCIShellContext;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.service.conversion.DataConversionService;
import org.cishell.service.guibuilder.GUIBuilderService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.MetaTypeService;

import java.util.HashMap;

public class CIShellCIBridge extends CIBridge {
    private BundleContext context;
    private CIShellContext ciShellContext;

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
        this.ciShellContext = new LocalCIShellContext(context);

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

        getBundleContext().addServiceListener(event -> {
            if (event.getType() == ServiceEvent.REGISTERED) {
                if (event.getServiceReference().toString().equals("[" + AlgorithmFactory.class.getName() + "]")) {
                    ServiceReference ref = event.getServiceReference();
                    cishellAlgorithm.cacheAlgorithm(ref);
                }
            } else if (event.getType() == ServiceEvent.UNREGISTERING) {
                if (event.getServiceReference().toString().equals("[" + AlgorithmFactory.class.getName() + "]")) {
                    ServiceReference ref = event.getServiceReference();
                    cishellAlgorithm.uncacheAlgorithm(ref);
                }
            } else if (event.getType() == ServiceEvent.MODIFIED) {
                //TODO
            }
        });

        this.cishellAlgorithm.cacheAlgorithmData();
        if (getSchedulerService() != null) {
            getSchedulerService().addSchedulerListener(new SchedulerListenerImpl(this));
        }

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

    public CIShellContext getCIShellContext() {
        return ciShellContext;
    }
}
