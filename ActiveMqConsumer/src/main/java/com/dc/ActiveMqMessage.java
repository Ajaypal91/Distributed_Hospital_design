package com.dc;

import java.io.Serializable;

public class ActiveMqMessage implements Serializable {
	
	String msg;
	String patient;
	String type;
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getPatient() {
		return patient;
	}
	public void setPatient(String patient) {
		this.patient = patient;
	}

}
