package org.shivas.protocol.server.messages;

import org.apache.mina.core.buffer.IoBuffer;

import org.shivas.protocol.server.Message;
import org.shivas.protocol.server.MessageType;

public class AccountCharactersRequestMessage implements Message {
	
	private int accountId;
	
	public AccountCharactersRequestMessage() {
	}

	public AccountCharactersRequestMessage(int accountId) {
		this.accountId = accountId;
	}

	public MessageType getMessageType() {
		return MessageType.ACCOUNT_CHARACTERS_REQUEST;
	}

	public void serialize(IoBuffer buf) {
		buf.putInt(accountId);
	}

	public void deserialize(IoBuffer buf) {
		accountId = buf.getInt();
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
	
}
