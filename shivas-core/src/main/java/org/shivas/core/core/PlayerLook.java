package org.shivas.core.core;

import org.shivas.core.database.models.Player;

public class PlayerLook implements Look {

	private static final long serialVersionUID = 7648869167600522705L;
	
	protected final Player player;
	
	private short skin;
	private short size;
	private Colors colors;

	public PlayerLook(Player player, short skin, short size, Colors colors) {
		this.player = player;
		this.skin = skin;
		this.size = size;
		this.colors = colors;
	}
	
	public int[] accessories() {
		return player.getBag().accessoriesTemplateId();
	}

	public short skin() {
		return skin;
	}

	public void setSkin(short skin) {
		this.skin = skin;
	}

	public short size() {
		return size;
	}

	public void setSize(short size) {
		this.size = size;
	}

	public Colors colors() {
		return colors;
	}

	public void setColors(Colors colors) {
		this.colors = colors;
	}

}
