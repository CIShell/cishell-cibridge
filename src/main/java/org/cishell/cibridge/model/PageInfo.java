package org.cishell.cibridge.model;

public class PageInfo {
	private final boolean hasNextPage;
	private final boolean hasPreviousPage;

	public PageInfo(boolean hasNextPage, boolean hasPreviousPage) {
		// TODO Auto-generated constructor stub
		this.hasNextPage = hasNextPage;
		this.hasPreviousPage = hasPreviousPage;
	}

	public boolean isHasNextPage() {
		return hasNextPage;
	}

	public boolean isHasPreviousPage() {
		return hasPreviousPage;
	}

}
