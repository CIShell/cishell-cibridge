package org.cishell.cibridge.model;

import org.cishell.cibridge.model.Data;
import org.cishell.cibridge.model.PageInfo;

public class DataQueryResults {
	private final Data results;
	private final PageInfo pageInfo;

	public DataQueryResults(Data results, PageInfo pageInfo) {
		// TODO Auto-generated constructor stub
		this.results = results;
		this.pageInfo = pageInfo;
	}

	public Data getResults() {
		return results;
	}

	public PageInfo getPageInfo() {
		return pageInfo;
	}

}