package com.claro.cron.beans;

import java.util.Date;

public class ResponseBean {

	
	String 	readStatus;
	String 	readComment;
	Date 	readTime;
	int		rowsCount;
	
	
	public String getReadStatus() {
		return readStatus;
	}
	public void setReadStatus(String readStatus) {
		this.readStatus = readStatus;
	}
	public Date getReadTime() {
		return readTime;
	}
	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}
	public int getRowsCount() {
		return rowsCount;
	}
	public void setRowsCount(int rowsCount) {
		this.rowsCount = rowsCount;
	}
	public String getReadComment() {
		return readComment;
	}
	public void setReadComment(String readComment) {
		this.readComment = readComment;
	}
	
	
	
}
