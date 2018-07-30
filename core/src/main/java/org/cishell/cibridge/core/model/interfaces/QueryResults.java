package org.cishell.cibridge.core.model.interfaces;

import java.util.List;

import org.cishell.cibridge.core.model.AlgorithmDefinition;
import org.cishell.cibridge.core.model.PageInfo;

public interface QueryResults<T> {
	
	public List<T> getResults();
	public PageInfo getPageInfo();
	public QueryResults<T> getQueryResults(List<T> objList, PageInfo pageInfo);
	
}
