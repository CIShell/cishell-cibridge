package org.cishell.cibridge.core.model;

import java.util.List;

public class LogQueryResults {
	private final List<Log> results;
	private final PageInfo pageInfo;

	public LogQueryResults(List<Log> results, PageInfo pageInfo) {
		// TODO Auto-generated constructor stub
		this.results = results;
		this.pageInfo = pageInfo;

	}

	public List<Log> getResults() {
		return results;
	}

	public PageInfo getPageInfo() {
		return pageInfo;
	}

}
