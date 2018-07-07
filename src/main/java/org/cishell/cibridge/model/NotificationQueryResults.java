package org.cishell.cibridge.model;

import org.cishell.cibridge.model.PageInfo;
import org.cishell.cibridge.model.Notification;

public class NotificationQueryResults {
	private final Notification results;
	private final PageInfo pageInfo;

	public NotificationQueryResults(Notification results, PageInfo pageInfo) {
		// TODO Auto-generated constructor stub
		this.results = results;
		this.pageInfo = pageInfo;
	}

	public Notification getResults() {
		return results;
	}

	public PageInfo getPageInfo() {
		return pageInfo;
	}

}
