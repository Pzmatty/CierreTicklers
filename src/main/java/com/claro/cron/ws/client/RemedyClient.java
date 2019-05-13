package com.claro.cron.ws.client;

public interface RemedyClient {
	
	
	public String[] rmdSelectByStatus(String status);
	public String rmdUpdateStatus(String incident, String status);

}
