package com.claro.cron.ws.binding;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;



//@XmlAccessorType (XmlAccessType.FIELD)
@XmlRootElement
@XmlType(propOrder = { "id", "value" })
public class Field {

	private String id;
	private String value;
	 
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	 
	 
	 
	
}
