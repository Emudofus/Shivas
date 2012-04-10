package org.shivas.game.database.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;

public class Breed implements Serializable {
	
	private static final long serialVersionUID = -243977568856899456L;
	
	@Id
	@Column(nullable=false)
	private short id;

	/**
	 * @return the id
	 */
	public short getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(short id) {
		this.id = id;
	}

}
