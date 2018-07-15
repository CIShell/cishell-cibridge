package org.cishell.cibridge.mock.resolvers;


public class CIShellCIBridge extends CIBridge {
	private BundleContext context;

	public CIShellCIBridge(BundleContext context) {
		this.context = context;
		super(new CIShellCIBridgeAlgorithmFacade(context), new CIShellCIBridgeDataFacade(context));
	}
}

public class CIShellCIBridgeAlgorithmFacade implements CIBridge.AlgorithmFacade {

}
