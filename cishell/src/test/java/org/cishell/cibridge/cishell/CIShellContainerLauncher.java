package org.cishell.cibridge.cishell;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class CIShellContainerLauncher extends IntegrationTestCase {
    @Test
    public void launchCIShellContainerIT() {
        assertNotNull("Failed to start CIShellContainer", getBundleContext());
        System.out.println("CIShellContainer Launched : OK" + System.lineSeparator());
    }
}
