package org.cishell.cibridge.model;
import java.util.List;

import org.cishell.cibridge.model.AlgorithmDefinition;
import org.cishell.cibridge.model.PageInfo;

public class AlgorithmDefinitionQueryResults {
    private final List<AlgorithmDefinition> results;
    private final PageInfo pageInfo;
    public AlgorithmDefinitionQueryResults(List<AlgorithmDefinition> results,PageInfo pageInfo) {
		// TODO Auto-generated constructor stub
    	this.results=results;
    	this.pageInfo=pageInfo;
	}
	public List<AlgorithmDefinition> getResults() {
		return results;
	}
	public PageInfo getPageInfo() {
		return pageInfo;
	}
    
}
