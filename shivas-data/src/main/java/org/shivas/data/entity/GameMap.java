package org.shivas.data.entity;

import java.io.Serializable;

public class GameMap implements Serializable {

	private static final long serialVersionUID = 924299994836075322L;
	
	private int id;
	private String date;
	private String key;
	
	public GameMap(int id, String date, String key) {
		this.id = id;
		this.date = date;
		this.key = key;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

}
