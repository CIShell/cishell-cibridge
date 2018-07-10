package org.cishell.cibridge.model;

import org.cishell.cibridge.model.Data;
import org.cishell.cibridge.model.PageInfo;
import java.util.List;

public class DataQueryResults {
	private final List<Data> results;
	private final PageInfo pageInfo;

	public DataQueryResults(List<Data> results, PageInfo pageInfo) {
		// TODO Auto-generated constructor stub
		this.results = results;
		this.pageInfo = pageInfo;
	}

	public List<Data> getResults() {
		return results;
	}

	public PageInfo getPageInfo() {
		return pageInfo;
	}

}
