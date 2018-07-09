package org.cishell.cibridge.model;
import org.cishell.cibridge.model.AlgorithmDefinition;
import org.cishell.cibridge.model.PageInfo;

public class AlgorithmDefinitionQueryResults {
    private final AlgorithmDefinition results;
    private final PageInfo pageInfo;
    public AlgorithmDefinitionQueryResults(AlgorithmDefinition results,PageInfo pageInfo) {
		// TODO Auto-generated constructor stub
    	this.results=results;
    	this.pageInfo=pageInfo;
	}
	public AlgorithmDefinition getResults() {
		return results;
	}
	public PageInfo getPageInfo() {
		return pageInfo;
	}
    
}
