package com.claro.cron.ws.binding;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;


public class Entry {
	
	List<Field> Field;

	//@XmlElementWrapper(name = "Entry")
	@XmlElement(name = "Field")
	public List<Field> getField() {
		return Field;
	}

	public void setField(List<Field> field) {
		Field = field;
	}

	
	
	

}
