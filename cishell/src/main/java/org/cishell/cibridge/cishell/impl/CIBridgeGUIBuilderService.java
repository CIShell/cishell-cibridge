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

	//TODO all methods implementation
	@Override
	public GUI createGUI(String id, MetaTypeProvider params) {
		return null;
	}
	@Override
	public Dictionary createGUIandWait(String id, MetaTypeProvider params) {
		return null;
	}
	@Override
	public boolean showConfirm(String title, String message, String detail) {
		return false;
	}
	@Override
	public void showError(String title, String message, String detail) {
		
	}
	@Override
	public void showError(String title, String message, Throwable error) {
		
	}
	@Override
	public void showInformation(String title, String message, String detail) {
		
	}
	@Override
	public boolean showQuestion(String title, String message, String detail) {
		return false;
	}
	@Override
	public void showWarning(String title, String message, String detail) {
		
	}

}
