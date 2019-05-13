package com.claro.cron.jpa.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="TICKLERS_SOLING_DEV")
public class TicklerSoling {

	
	@Id
	@Column(name = "TSO_TCK_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)	
    private int id;
	
    @Column(name = "TSO_REMEDY_ID")
    private String remedyId;
    
    @Column(name = "TSO_STATE")
    private Date state;
    
    @Column(name = "TSO_HIGHT_DATE")
    private Date highDate;

    @Column(name = "TSO_NIM_NUMBER")
    private Date nim;

    
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRemedyId() {
		return remedyId;
	}

	public void setRemedyId(String remedyId) {
		this.remedyId = remedyId;
	}

	public Date getState() {
		return state;
	}

	public void setState(Date state) {
		this.state = state;
	}

	public Date getHighDate() {
		return highDate;
	}

	public void setHighDate(Date highDate) {
		this.highDate = highDate;
	}

	public Date getNim() {
		return nim;
	}

	public void setNim(Date nim) {
		this.nim = nim;
	}

}
