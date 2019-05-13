package com.claro.cron.jpa.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//@Entity
//@Table(name="CRON_REMEDY_LOG")
public class CronRemedyLog {

	
	@Id
	@Column(name = "CRL_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)	
    private int id;
	
    @Column(name = "CRL_USER")
    private String user;
    
    @Column(name = "CRL_LAST_READ_DATE")
    private Date lastReadDate;
    
    @Column(name = "CRL_STATE")
    private String state;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Date getLastReadDate() {
		return lastReadDate;
	}

	public void setLastReadDate(Date lastReadDate) {
		this.lastReadDate = lastReadDate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
    
	
}
