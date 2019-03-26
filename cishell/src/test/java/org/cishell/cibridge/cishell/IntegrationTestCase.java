package org.cishell.cibridge.cishell;

import org.cishell.app.service.datamanager.DataManagerService;
import org.cishell.app.service.scheduler.SchedulerService;
import org.cishell.container.CIShellContainer;
import org.cishell.service.conversion.DataConversionService;
import org.cishell.service.guibuilder.GUIBuilderService;
import org.junit.AfterClass;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.MetaTypeService;

public abstract class IntegrationTestCase {

    private static final String PLUGINS_DIRECTORY_PATH = "../integration-tests-container/target/plugins";
    private static final CIShellContainer CISHELL_CONTAINER = CIShellContainer.getBuilder().pluginsDirectoryPath(PLUGINS_DIRECTORY_PATH).build();
    private static final CIShellCIBridge CISHELL_CIBRIDGE = new CIShellCIBridge(CISHELL_CONTAINER.getBundleContext());

    protected BundleContext getBundleContext() {
        return CISHELL_CONTAINER.getBundleContext();
    }

    protected CIShellCIBridge getCIShellCIBridge() {
        return CISHELL_CIBRIDGE;
    }

    protected GUIBuilderService getGUIBuilderService() {
        return CISHELL_CONTAINER.getGUIBuilderService();
    }

    protected DataConversionService getDataConversionService() {
        return CISHELL_CONTAINER.getDataConversionService();
    }

    protected SchedulerService getSchedulerService() {
        return CISHELL_CONTAINER.getSchedulerService();
    }

    protected DataManagerService getDataManagerService() {
        return CISHELL_CONTAINER.getDataManagerService();
    }

    protected LogService getLogService() {
        return CISHELL_CONTAINER.getLogService();
    }

    protected MetaTypeService getMetaTypeService() {
        return CISHELL_CONTAINER.getMetaTypeService();
    }

    protected <S> S getService(Class<S> clazz) {
        return CISHELL_CONTAINER.getService(clazz);
    }

    protected <S> S waitAndGetService(Class<S> clazz) throws InterruptedException {
        int ticks = 200;
        while (ticks-- > 0) {
            if (getService(clazz) != null) {
                return getService(clazz);
            }
            Thread.sleep(100);
        }
        return null;
    }
}
