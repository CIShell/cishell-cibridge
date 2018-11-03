package org.cishell.cibridge.cishell;

import org.cishell.container.CIShellContainer;
import org.osgi.framework.BundleContext;

public abstract class IntegrationTestCase {

    private static final String PLUGINS_DIRECTORY_PATH = "../integration-tests-container/target/plugins";
    private static final CIShellContainer CONTAINER = new CIShellContainer(PLUGINS_DIRECTORY_PATH, null);
    private static final CIShellCIBridge CISHELL_CIBRIDGE = new CIShellCIBridge(CONTAINER.getBundleContext());

    public CIShellContainer getContainer() {
        return CONTAINER;
    }

    public BundleContext getBundleContext() {
        return CONTAINER.getBundleContext();
    }

    public CIShellCIBridge getCIShellCIBridge() {
        return CISHELL_CIBRIDGE;
    }
}
