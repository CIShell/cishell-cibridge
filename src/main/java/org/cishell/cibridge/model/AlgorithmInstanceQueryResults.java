package org.cishell.cibridge.model;

import org.cishell.cibridge.model.AlgorithmInstance;

import java.util.List;

public class AlgorithmInstanceQueryResults {
	List<AlgorithmInstance> results;
	PageInfo pageInfo;

	public AlgorithmInstanceQueryResults(List<AlgorithmInstance> results, PageInfo pageInfo) {
		// TODO Auto-generated constructor stub
		this.results = results;
		this.pageInfo = pageInfo;
	}

	public List<AlgorithmInstance> getResults() {
		return results;
	}

	public PageInfo getPageInfo() {
		return pageInfo;
	}
}
