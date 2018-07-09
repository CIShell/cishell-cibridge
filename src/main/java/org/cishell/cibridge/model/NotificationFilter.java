package org.cishell.cibridge.model;

public class NotificationFilter {
	private String ID;
	private Boolean isClosed;
	private int limit;
	private int offset;
	
	public NotificationFilter(String ID,Boolean isClosed, int limit,int offset) {
		// TODO Auto-generated constructor stub
		this.ID=ID;
		this.isClosed=isClosed;
		this.limit=limit;
		this.offset=offset;
	}
	public NotificationFilter(Boolean isClosed, int limit,int offset) {
		// TODO Auto-generated constructor stub
		this.ID=null;
		this.isClosed=isClosed;
		this.limit=limit;
		this.offset=offset;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public Boolean getIsClosed() {
		return isClosed;
	}
	public void setIsClosed(Boolean isClosed) {
		this.isClosed = isClosed;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	
}
