package com.claro.cron.ws.binding;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


//@XmlAccessorType (XmlAccessType.FIELD)
@XmlRootElement(name = "RESULTADO")
public class RESULTADO {

	@XmlElement(name = "Entry")
	private ArrayList<Entry> Entry;
//	@XmlElementWrapper(name = "Entry")
//	@XmlElement(name = "Field")
//	private ArrayList<Field> Entry;
//
//	public ArrayList<Field> getEntry() {
//		return Entry;
//	}
//
//	public void setEntry(ArrayList<Field> entry) {
//		Entry = entry;
//	}

	public ArrayList<Entry> getEntry() {
		return Entry;
	}

	public void setEntry(ArrayList<Entry> entry) {
		Entry = entry;
	}

}
