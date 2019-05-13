package com.claro.cron.ws.binding;

import javax.xml.bind.annotation.XmlRootElement;



@XmlRootElement ( name = "RMDUPDATE")
//@XmlAccessorType (XmlAccessType.FIELD)
public class RmdUpdate {
	

	private String SISTEMA;
	private String FORMA;
	private String ID;
	private String COLUMNAS;
	private RESULTS RESULTS;
	
	public String getSISTEMA() {
		return SISTEMA;
	}
	public void setSISTEMA(String sISTEMA) {
		SISTEMA = sISTEMA;
	}
	public String getFORMA() {
		return FORMA;
	}
	public void setFORMA(String fORMA) {
		FORMA = fORMA;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getCOLUMNAS() {
		return COLUMNAS;
	}
	public void setCOLUMNAS(String cOLUMNAS) {
		COLUMNAS = cOLUMNAS;
	}
	public RESULTS getRESULTS() {
		return RESULTS;
	}
	public void setRESULTS(RESULTS rESULTS) {
		RESULTS = rESULTS;
	}


	
	
		
}
