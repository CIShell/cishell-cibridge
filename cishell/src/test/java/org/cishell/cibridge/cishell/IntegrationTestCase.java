package org.cishell.cibridge.cishell;

import org.cishell.container.CIShellContainer;
import org.osgi.framework.BundleContext;

public class IntegrationTestCase {

    public CIShellContainer getContainer() {
        return ContainerInstanceProvider.getCIShellContainer();
    }

    public BundleContext getBundleContext() {
        return ContainerInstanceProvider.getCIShellContainer().getBundleContext();
    }

    public CIShellCIBridge getCIShellCIBridge() {
        return CIShellCIbridgeInstanceProvider.getCIShellCIBridge();
    }
}


