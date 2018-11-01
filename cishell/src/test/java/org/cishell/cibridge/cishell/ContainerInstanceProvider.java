package org.cishell.cibridge.cishell;

import org.cishell.container.CIShellContainer;

public class ContainerInstanceProvider {

    private static final String pluginsDirectoryPath = "../integration-tests-container/target/plugins";

    private ContainerInstanceProvider() {
        //not allowed
    }

    private static class SingletonHelper {
        private static final CIShellContainer CONTAINER_INSTANCE = new CIShellContainer(pluginsDirectoryPath, null);
    }

    public static CIShellContainer getCIShellContainer() {
        return SingletonHelper.CONTAINER_INSTANCE;
    }
    
}