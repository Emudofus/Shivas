package org.shivas.protocol.server.messages;

import org.apache.mina.core.buffer.IoBuffer;

import org.shivas.protocol.server.Message;
import org.shivas.protocol.server.MessageType;
import org.shivas.protocol.server.enums.ServerStatus;

public class ServerStatusUpdateMessage implements Message {
	
	private ServerStatus status;
	
	public ServerStatusUpdateMessage() {
	}

	public ServerStatusUpdateMessage(ServerStatus status) {
		this.status = status;
	}

	public MessageType getMessageType() {
		return MessageType.SERVER_STATUS_UPDATE;
	}

	public void serialize(IoBuffer buf) {
		buf.putEnum(status);
	}

	public void deserialize(IoBuffer buf) {
		status = buf.getEnum(ServerStatus.class);
	}

	/**
	 * @return the status
	 */
	public ServerStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(ServerStatus status) {
		this.status = status;
	}

}
