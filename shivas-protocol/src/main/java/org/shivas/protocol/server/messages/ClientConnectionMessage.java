package org.shivas.protocol.server.messages;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;

import org.shivas.common.Account;
import org.shivas.protocol.server.Message;
import org.shivas.protocol.server.MessageType;

public class ClientConnectionMessage implements Message {
	
	private Account account;
	private String salt;
	
	public ClientConnectionMessage() {
	}

	public ClientConnectionMessage(Account account, String salt) {
		this.account = account;
		this.salt = salt;
	}

	public MessageType getMessageType() {
		return MessageType.CLIENT_CONNECTION;
	}

	public void serialize(IoBuffer buf) {
		buf.putObject(account);
		try {
			buf.putString(salt, Charset.forName("UTF-8").newEncoder());
		} catch (CharacterCodingException e) {
			e.printStackTrace();
		}
	}

	public void deserialize(IoBuffer buf) {
		try {
			account = (Account) buf.getObject();
			salt = buf.getString(Charset.forName("UTF-8").newDecoder());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * @param accountId the accountId to set
	 */
	public void setAccount(Account account) {
		this.account = account;
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
