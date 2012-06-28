package org.shivas.server.core;

import java.io.Serializable;

public interface Look extends Serializable {

	short skin();
	void setSkin(short skin);
	
	short size();
	void setSize(short size);
	
	Colors colors();
	void setColors(Colors colors);
	
	int[] accessories();
	
}
