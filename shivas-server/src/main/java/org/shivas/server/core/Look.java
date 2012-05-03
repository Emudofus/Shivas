package org.shivas.server.core;

import java.io.Serializable;

public class Look implements Serializable {

	private static final long serialVersionUID = 7648869167600522705L;
	
	private short skin;
	private short size;
	private Colors colors;

	public Look(short skin, short size, Colors colors) {
		super();
		this.skin = skin;
		this.size = size;
		this.colors = colors;
	}

	public short getSkin() {
		return skin;
	}

	public void setSkin(short skin) {
		this.skin = skin;
	}

	public short getSize() {
		return size;
	}

	public void setSize(short size) {
		this.size = size;
	}

	public Colors getColors() {
		return colors;
	}

	public void setColors(Colors colors) {
		this.colors = colors;
	}

}
