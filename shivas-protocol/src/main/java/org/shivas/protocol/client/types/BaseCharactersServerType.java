package org.shivas.protocol.client.types;

public class BaseCharactersServerType {
	private int serverId;
	private int characters;
	
	public BaseCharactersServerType() {
	}

	public BaseCharactersServerType(int serverId, int characters) {
		super();
		this.serverId = serverId;
		this.characters = characters;
	}

	/**
	 * @return the serverId
	 */
	public int getServerId() {
		return serverId;
	}

	/**
	 * @param serverId the serverId to set
	 */
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	/**
	 * @return the characters
	 */
	public int getCharacters() {
		return characters;
	}

	/**
	 * @param characters the characters to set
	 */
	public void setCharacters(int characters) {
		this.characters = characters;
	}
}
