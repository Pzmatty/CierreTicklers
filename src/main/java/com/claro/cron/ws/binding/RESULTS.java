package com.claro.cron.ws.binding;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.claro.cron.ws.binding.*;


//@XmlAccessorType (XmlAccessType.FIELD)
@XmlRootElement(name = "RESULTADO")
public class RESULTS {

	@XmlElement(name = "UPDATED")
	private ArrayList<String> Updated;
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

	public ArrayList<String> getUpdated() {
		return Updated;
	}

	public void setUpdated(ArrayList<String> updated) {
		Updated = updated;
	}



}
