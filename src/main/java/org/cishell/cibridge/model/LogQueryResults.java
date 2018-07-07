package org.cishell.cibridge.model;
import org.cishell.cibridge.model.PageInfo;
public class LogQueryResults {
    private final PageInfo pageInfo;
    
    public LogQueryResults(PageInfo pageInfo) {
		// TODO Auto-generated constructor stub
    	this.pageInfo=pageInfo;
	}

	public PageInfo getPageInfo() {
		return pageInfo;
	}
    
}
