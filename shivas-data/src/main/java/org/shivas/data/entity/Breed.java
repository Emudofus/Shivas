package org.shivas.data.entity;

import java.io.Serializable;

public class Breed implements Serializable {

	private static final long serialVersionUID = 7657680626426281371L;
	
	private int id;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
