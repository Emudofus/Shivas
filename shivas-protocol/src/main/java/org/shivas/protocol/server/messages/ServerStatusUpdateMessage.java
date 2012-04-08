package org.shivas.protocol.server.messages;

import org.apache.mina.core.buffer.IoBuffer;

import org.shivas.protocol.client.enums.WorldStateEnum;
import org.shivas.protocol.server.Message;
import org.shivas.protocol.server.MessageType;

public class ServerStatusUpdateMessage implements Message {
	
	private WorldStateEnum status;
	
	public ServerStatusUpdateMessage() {
	}

	public ServerStatusUpdateMessage(WorldStateEnum status) {
		this.status = status;
	}

	public MessageType getMessageType() {
		return MessageType.SERVER_STATUS_UPDATE;
	}

	public void serialize(IoBuffer buf) {
		buf.putEnum(status);
	}

	public void deserialize(IoBuffer buf) {
		status = buf.getEnum(WorldStateEnum.class);
	}

	/**
	 * @return the status
	 */
	public WorldStateEnum getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(WorldStateEnum status) {
		this.status = status;
	}

}
