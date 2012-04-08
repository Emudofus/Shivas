package org.shivas.protocol.server.messages;

import org.apache.mina.core.buffer.IoBuffer;

import org.shivas.protocol.server.Message;
import org.shivas.protocol.server.MessageType;

public class AccountCharactersMessage implements Message {
	
	private int accountId;
	private byte characters;
	
	public AccountCharactersMessage() {
	}

	public AccountCharactersMessage(int accountId, byte characters) {
		this.accountId = accountId;
		this.characters = characters;
	}

	public MessageType getMessageType() {
		return MessageType.ACCOUNT_CHARACTERS;
	}

	public void serialize(IoBuffer buf) {
		buf.putInt(accountId);
		buf.put(characters);
	}

	public void deserialize(IoBuffer buf) {
		accountId = buf.getInt();
		characters = buf.get();
	}

	/**
	 * @return the accountId
	 */
	public int getAccountId() {
		return accountId;
	}

	/**
	 * @param accountId the accountId to set
	 */
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	/**
	 * @return the characters
	 */
	public byte getCharacters() {
		return characters;
	}

	/**
	 * @param characters the characters to set
	 */
	public void setCharacters(byte characters) {
		this.characters = characters;
	}

}
