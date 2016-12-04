package org.shivas.core.services;

import java.net.SocketAddress;
import java.util.Set;

import org.apache.mina.core.filterchain.IoFilterChain;
import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoService;
import org.apache.mina.core.service.TransportMetadata;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.core.write.WriteRequest;
import org.apache.mina.core.write.WriteRequestQueue;

public abstract class IoSessionDecorator implements IoSession {
	
	protected final IoSession session;

	public IoSessionDecorator(IoSession session) {
		this.session = session;
	}

	@Override
	public boolean isSecured() {
		return session.isSecured();
	}

	@Override
	public CloseFuture closeNow() {
		return session.closeNow();
	}

	@Override
	public CloseFuture closeOnFlush() {
		return session.closeOnFlush();
	}

	@Override
	public boolean isActive() {
		return session.isActive();
	}

	/**
	 * @return
	 * @deprecated
	 * @see org.apache.mina.core.session.IoSession#close()
	 */
	public CloseFuture close() {
		return session.close();
	}

	/**
	 * @param arg0
	 * @return
	 * @see org.apache.mina.core.session.IoSession#close(boolean)
	 */
	public CloseFuture close(boolean arg0) {
		return arg0 ? session.closeNow() : session.closeOnFlush();
	}

	/**
	 * @param arg0
	 * @return
	 * @see org.apache.mina.core.session.IoSession#containsAttribute(java.lang.Object)
	 */
	public boolean containsAttribute(Object arg0) {
		return session.containsAttribute(arg0);
	}

	/**
	 * @return
	 * @deprecated
	 * @see org.apache.mina.core.session.IoSession#getAttachment()
	 */
	public Object getAttachment() {
		return session.getAttachment();
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 * @see org.apache.mina.core.session.IoSession#getAttribute(java.lang.Object, java.lang.Object)
	 */
	public Object getAttribute(Object arg0, Object arg1) {
		return session.getAttribute(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @return
	 * @see org.apache.mina.core.session.IoSession#getAttribute(java.lang.Object)
	 */
	public Object getAttribute(Object arg0) {
		return session.getAttribute(arg0);
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#getAttributeKeys()
	 */
	public Set<Object> getAttributeKeys() {
		return session.getAttributeKeys();
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#getBothIdleCount()
	 */
	public int getBothIdleCount() {
		return session.getBothIdleCount();
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#getCloseFuture()
	 */
	public CloseFuture getCloseFuture() {
		return session.getCloseFuture();
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#getConfig()
	 */
	public IoSessionConfig getConfig() {
		return session.getConfig();
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#getCreationTime()
	 */
	public long getCreationTime() {
		return session.getCreationTime();
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#getCurrentWriteMessage()
	 */
	public Object getCurrentWriteMessage() {
		return session.getCurrentWriteMessage();
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#getCurrentWriteRequest()
	 */
	public WriteRequest getCurrentWriteRequest() {
		return session.getCurrentWriteRequest();
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#getFilterChain()
	 */
	public IoFilterChain getFilterChain() {
		return session.getFilterChain();
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#getHandler()
	 */
	public IoHandler getHandler() {
		return session.getHandler();
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#getId()
	 */
	public long getId() {
		return session.getId();
	}

	/**
	 * @param arg0
	 * @return
	 * @see org.apache.mina.core.session.IoSession#getIdleCount(org.apache.mina.core.session.IdleStatus)
	 */
	public int getIdleCount(IdleStatus arg0) {
		return session.getIdleCount(arg0);
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#getLastBothIdleTime()
	 */
	public long getLastBothIdleTime() {
		return session.getLastBothIdleTime();
	}

	/**
	 * @param arg0
	 * @return
	 * @see org.apache.mina.core.session.IoSession#getLastIdleTime(org.apache.mina.core.session.IdleStatus)
	 */
	public long getLastIdleTime(IdleStatus arg0) {
		return session.getLastIdleTime(arg0);
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#getLastIoTime()
	 */
	public long getLastIoTime() {
		return session.getLastIoTime();
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#getLastReadTime()
	 */
	public long getLastReadTime() {
		return session.getLastReadTime();
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#getLastReaderIdleTime()
	 */
	public long getLastReaderIdleTime() {
		return session.getLastReaderIdleTime();
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#getLastWriteTime()
	 */
	public long getLastWriteTime() {
		return session.getLastWriteTime();
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#getLastWriterIdleTime()
	 */
	public long getLastWriterIdleTime() {
		return session.getLastWriterIdleTime();
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#getLocalAddress()
	 */
	public SocketAddress getLocalAddress() {
		return session.getLocalAddress();
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#getReadBytes()
	 */
	public long getReadBytes() {
		return session.getReadBytes();
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#getReadBytesThroughput()
	 */
	public double getReadBytesThroughput() {
		return session.getReadBytesThroughput();
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#getReadMessages()
	 */
	public long getReadMessages() {
		return session.getReadMessages();
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#getReadMessagesThroughput()
	 */
	public double getReadMessagesThroughput() {
		return session.getReadMessagesThroughput();
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#getReaderIdleCount()
	 */
	public int getReaderIdleCount() {
		return session.getReaderIdleCount();
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#getRemoteAddress()
	 */
	public SocketAddress getRemoteAddress() {
		return session.getRemoteAddress();
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#getScheduledWriteBytes()
	 */
	public long getScheduledWriteBytes() {
		return session.getScheduledWriteBytes();
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#getScheduledWriteMessages()
	 */
	public int getScheduledWriteMessages() {
		return session.getScheduledWriteMessages();
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#getService()
	 */
	public IoService getService() {
		return session.getService();
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#getServiceAddress()
	 */
	public SocketAddress getServiceAddress() {
		return session.getServiceAddress();
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#getTransportMetadata()
	 */
	public TransportMetadata getTransportMetadata() {
		return session.getTransportMetadata();
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#getWriteRequestQueue()
	 */
	public WriteRequestQueue getWriteRequestQueue() {
		return session.getWriteRequestQueue();
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#getWriterIdleCount()
	 */
	public int getWriterIdleCount() {
		return session.getWriterIdleCount();
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#getWrittenBytes()
	 */
	public long getWrittenBytes() {
		return session.getWrittenBytes();
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#getWrittenBytesThroughput()
	 */
	public double getWrittenBytesThroughput() {
		return session.getWrittenBytesThroughput();
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#getWrittenMessages()
	 */
	public long getWrittenMessages() {
		return session.getWrittenMessages();
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#getWrittenMessagesThroughput()
	 */
	public double getWrittenMessagesThroughput() {
		return session.getWrittenMessagesThroughput();
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#isBothIdle()
	 */
	public boolean isBothIdle() {
		return session.isBothIdle();
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#isClosing()
	 */
	public boolean isClosing() {
		return session.isClosing();
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#isConnected()
	 */
	public boolean isConnected() {
		return session.isConnected();
	}

	/**
	 * @param arg0
	 * @return
	 * @see org.apache.mina.core.session.IoSession#isIdle(org.apache.mina.core.session.IdleStatus)
	 */
	public boolean isIdle(IdleStatus arg0) {
		return session.isIdle(arg0);
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#isReadSuspended()
	 */
	public boolean isReadSuspended() {
		return session.isReadSuspended();
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#isReaderIdle()
	 */
	public boolean isReaderIdle() {
		return session.isReaderIdle();
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#isWriteSuspended()
	 */
	public boolean isWriteSuspended() {
		return session.isWriteSuspended();
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#isWriterIdle()
	 */
	public boolean isWriterIdle() {
		return session.isWriterIdle();
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#read()
	 */
	public ReadFuture read() {
		return session.read();
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 * @see org.apache.mina.core.session.IoSession#removeAttribute(java.lang.Object, java.lang.Object)
	 */
	public boolean removeAttribute(Object arg0, Object arg1) {
		return session.removeAttribute(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @return
	 * @see org.apache.mina.core.session.IoSession#removeAttribute(java.lang.Object)
	 */
	public Object removeAttribute(Object arg0) {
		return session.removeAttribute(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @return
	 * @see org.apache.mina.core.session.IoSession#replaceAttribute(java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	public boolean replaceAttribute(Object arg0, Object arg1, Object arg2) {
		return session.replaceAttribute(arg0, arg1, arg2);
	}

	/**
	 * 
	 * @see org.apache.mina.core.session.IoSession#resumeRead()
	 */
	public void resumeRead() {
		session.resumeRead();
	}

	/**
	 * 
	 * @see org.apache.mina.core.session.IoSession#resumeWrite()
	 */
	public void resumeWrite() {
		session.resumeWrite();
	}

	/**
	 * @param arg0
	 * @return
	 * @deprecated
	 * @see org.apache.mina.core.session.IoSession#setAttachment(java.lang.Object)
	 */
	public Object setAttachment(Object arg0) {
		return session.setAttachment(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 * @see org.apache.mina.core.session.IoSession#setAttribute(java.lang.Object, java.lang.Object)
	 */
	public Object setAttribute(Object arg0, Object arg1) {
		return session.setAttribute(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @return
	 * @see org.apache.mina.core.session.IoSession#setAttribute(java.lang.Object)
	 */
	public Object setAttribute(Object arg0) {
		return session.setAttribute(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 * @see org.apache.mina.core.session.IoSession#setAttributeIfAbsent(java.lang.Object, java.lang.Object)
	 */
	public Object setAttributeIfAbsent(Object arg0, Object arg1) {
		return session.setAttributeIfAbsent(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @return
	 * @see org.apache.mina.core.session.IoSession#setAttributeIfAbsent(java.lang.Object)
	 */
	public Object setAttributeIfAbsent(Object arg0) {
		return session.setAttributeIfAbsent(arg0);
	}

	/**
	 * @param arg0
	 * @see org.apache.mina.core.session.IoSession#setCurrentWriteRequest(org.apache.mina.core.write.WriteRequest)
	 */
	public void setCurrentWriteRequest(WriteRequest arg0) {
		session.setCurrentWriteRequest(arg0);
	}

	/**
	 * 
	 * @see org.apache.mina.core.session.IoSession#suspendRead()
	 */
	public void suspendRead() {
		session.suspendRead();
	}

	/**
	 * 
	 * @see org.apache.mina.core.session.IoSession#suspendWrite()
	 */
	public void suspendWrite() {
		session.suspendWrite();
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @see org.apache.mina.core.session.IoSession#updateThroughput(long, boolean)
	 */
	public void updateThroughput(long arg0, boolean arg1) {
		session.updateThroughput(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 * @see org.apache.mina.core.session.IoSession#write(java.lang.Object, java.net.SocketAddress)
	 */
	public WriteFuture write(Object arg0, SocketAddress arg1) {
		return session.write(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @return
	 * @see org.apache.mina.core.session.IoSession#write(java.lang.Object)
	 */
	public WriteFuture write(Object arg0) {
		return session.write(arg0);
	}

}
