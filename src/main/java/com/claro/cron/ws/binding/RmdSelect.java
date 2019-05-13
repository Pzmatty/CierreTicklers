package com.claro.cron.ws.binding;

import javax.xml.bind.annotation.XmlRootElement;



@XmlRootElement ( name = "RMDSELECT")
//@XmlAccessorType (XmlAccessType.FIELD)
public class RmdSelect {
	

	private String SISTEMA;
	private String FORMA;
	private String CONDICIONES;
	private RESULTADO RESULTADO;
	
	
	
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
	public String getCONDICIONES() {
		return CONDICIONES;
	}
	public void setCONDICIONES(String cONDICIONES) {
		CONDICIONES = cONDICIONES;
	}

	public RESULTADO getRESULTADO() {
		return RESULTADO;
	}
	public void setRESULTADO(RESULTADO rESULTADO) {
		RESULTADO = rESULTADO;
	}


	


	
	
		
}
