package org.cishell.cibridge.core.model;

import java.util.List;

public class NotificationQueryResults {
	private final List<Notification> results;
	private final PageInfo pageInfo;

	public NotificationQueryResults(List<Notification> results, PageInfo pageInfo) {
		// TODO Auto-generated constructor stub
		this.results = results;
		this.pageInfo = pageInfo;
	}

	public List<Notification> getResults() {
		return results;
	}

	public PageInfo getPageInfo() {
		return pageInfo;
	}

}
