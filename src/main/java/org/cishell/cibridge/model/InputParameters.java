package org.cishell.cibridge.model;

import java.util.ArrayList;
import java.util.List;

public class InputParameters {
   public final String ID;
   public final String title;
   public final String description;
   public final List<ParameterDefinition> parameters;

    
    
    public InputParameters(String ID,String title,String description,List<ParameterDefinition> parameters) {
		// TODO Auto-generated constructor stub
    	this.ID=ID;
    	this.title=title;
    	this.description=description;
    	this.parameters=parameters;
	}
    public InputParameters(String title,String description,List<ParameterDefinition> parameters) {
		// TODO Auto-generated constructor stub
    	this.ID=null;
    	this.title=title;
    	this.description=description;
    	this.parameters=parameters;
	}
	public String getID() {
		return ID;
	}
	public String getTitle() {
		return title;
	}
	public String getDescription() {
		return description;
	}
	public List<ParameterDefinition> getParameters() {
		return parameters;
	}
    
}
