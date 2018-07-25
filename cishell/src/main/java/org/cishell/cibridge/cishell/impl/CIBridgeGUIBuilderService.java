package org.cishell.cibridge.cishell.impl;

import java.util.Dictionary;

import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.service.guibuilder.GUI;
import org.cishell.service.guibuilder.GUIBuilderService;
import org.osgi.service.metatype.MetaTypeProvider;

public class CIBridgeGUIBuilderService implements GUIBuilderService{
	private CIShellCIBridge cibridge;
	public CIBridgeGUIBuilderService(){
		
	}
	public CIBridgeGUIBuilderService(CIShellCIBridge cibridge){
		this.cibridge = cibridge;
		
	}
	@Override
	public GUI createGUI(String arg0, MetaTypeProvider arg1) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Dictionary createGUIandWait(String arg0, MetaTypeProvider arg1) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean showConfirm(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void showError(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void showError(String arg0, String arg1, Throwable arg2) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void showInformation(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean showQuestion(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void showWarning(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}

}
