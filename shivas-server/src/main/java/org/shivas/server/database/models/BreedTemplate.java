package org.shivas.server.database.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="breeds")
public class BreedTemplate implements Serializable {

	private static final long serialVersionUID = 1047598056891168587L;
	
	@Id
	private int id;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

}
