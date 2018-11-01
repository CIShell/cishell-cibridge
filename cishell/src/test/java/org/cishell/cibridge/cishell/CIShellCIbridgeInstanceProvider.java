package org.cishell.cibridge.cishell;

public class CIShellCIbridgeInstanceProvider {

    private CIShellCIbridgeInstanceProvider() {
        //not allowed
    }

    private static class SingletonHelper {
        private static final CIShellCIBridge CISHELL_CIBRIDGE_INSTANCE = new CIShellCIBridge(ContainerInstanceProvider.getCIShellContainer().getBundleContext());
    }

    public static CIShellCIBridge getCIShellCIBridge() {
        return SingletonHelper.CISHELL_CIBRIDGE_INSTANCE;
    }
}