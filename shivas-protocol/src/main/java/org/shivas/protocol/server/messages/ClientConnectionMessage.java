package org.shivas.protocol.server.messages;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;

import org.shivas.protocol.server.Message;
import org.shivas.protocol.server.MessageType;

public class ClientConnectionMessage implements Message {
	
	private int accountId;
	private String salt;
	
	public ClientConnectionMessage() {
	}

	public ClientConnectionMessage(int accountId, String salt) {
		this.accountId = accountId;
		this.salt = salt;
	}

	public MessageType getMessageType() {
		return MessageType.CLIENT_CONNECTION;
	}

	public void serialize(IoBuffer buf) {
		buf.putInt(accountId);
		try {
			buf.putString(salt, Charset.forName("UTF-8").newEncoder());
		} catch (CharacterCodingException e) {
			e.printStackTrace();
		}
	}

	public void deserialize(IoBuffer buf) {
		accountId = buf.getInt();
		try {
			salt = buf.getString(Charset.forName("UTF-8").newDecoder());
		} catch (CharacterCodingException e) {
			e.printStackTrace();
		}
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
	 * @return the salt
	 */
	public String getSalt() {
		return salt;
	}

	/**
	 * @param salt the salt to set
	 */
	public void setSalt(String salt) {
		this.salt = salt;
	}

}
