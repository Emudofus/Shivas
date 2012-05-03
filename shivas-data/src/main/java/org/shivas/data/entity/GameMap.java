package org.shivas.data.entity;

import java.io.Serializable;

public class GameMap implements Serializable {

	private static final long serialVersionUID = 924299994836075322L;
	
	private int id;
	private String date;
	private String key;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}

}
