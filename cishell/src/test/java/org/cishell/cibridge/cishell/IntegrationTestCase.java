package org.cishell.cibridge.cishell;

import org.cishell.app.service.datamanager.DataManagerService;
import org.cishell.app.service.scheduler.SchedulerService;
import org.cishell.container.CIShellContainer;
import org.cishell.service.conversion.DataConversionService;
import org.cishell.service.guibuilder.GUIBuilderService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.MetaTypeService;

public abstract class IntegrationTestCase {

    private static final String PLUGINS_DIRECTORY_PATH = "../integration-tests-container/target/plugins";
    private static final CIShellContainer CONTAINER = new CIShellContainer(PLUGINS_DIRECTORY_PATH, null);
    private static final CIShellCIBridge CISHELL_CIBRIDGE = new CIShellCIBridge(CONTAINER.getBundleContext());

    protected BundleContext getBundleContext() {
        return CONTAINER.getBundleContext();
    }

    protected CIShellCIBridge getCIShellCIBridge() {
        return CISHELL_CIBRIDGE;
    }

    protected GUIBuilderService getGUIBuilderService() {
        return (GUIBuilderService) this.getService(GUIBuilderService.class);
    }

    protected DataConversionService getDataConversionService() {
        return (DataConversionService) this.getService(DataConversionService.class);
    }

    protected SchedulerService getSchedulerService() {
        return (SchedulerService) this.getService(SchedulerService.class);
    }

    protected DataManagerService getDataManagerService() {
        return (DataManagerService) this.getService(DataManagerService.class);
    }

    protected LogService getLogService() {
        return (LogService) this.getService(LogService.class);
    }

    protected MetaTypeService getMetaTypeService() {
        return (MetaTypeService) this.getService(MetaTypeService.class);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected Object getService(Class c) {
        ServiceReference ref = getBundleContext().getServiceReference(c.getName());
        return ref != null ? getBundleContext().getService(ref) : null;
    }
}
