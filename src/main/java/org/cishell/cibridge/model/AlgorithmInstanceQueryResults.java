package org.cishell.cibridge.model;

import org.cishell.cibridge.model.AlgorithmInstance;

public class AlgorithmInstanceQueryResults {
	AlgorithmInstance results;
	PageInfo pageInfo;

	public AlgorithmInstanceQueryResults(AlgorithmInstance results, PageInfo pageInfo) {
		// TODO Auto-generated constructor stub
		this.results = results;
		this.pageInfo = pageInfo;
	}

	public AlgorithmInstance getResults() {
		return results;
	}

	public PageInfo getPageInfo() {
		return pageInfo;
	}
}
