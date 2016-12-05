package org.shivas.core.services;

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

import java.net.SocketAddress;
import java.util.Set;

public abstract class IoSessionDecorator implements IoSession {
	
	protected final IoSession session;

	public IoSessionDecorator(IoSession session) {
		this.session = session;
	}

	/**
	 * @return a unique identifier for this session.  Every session has its own
	 * ID which is different from each other.
	 *
	 * TODO : The way it's implemented does not guarantee that the contract is
	 * respected. It uses the HashCode() method which don't guarantee the key
	 * unicity.
	 */
	@Override
	public long getId() {
		return session.getId();
	}

	/**
	 * @return the {@link IoService} which provides I/O service to this session.
	 */
	@Override
	public IoService getService() {
		return session.getService();
	}

	/**
	 * @return the {@link IoHandler} which handles this session.
	 */
	@Override
	public IoHandler getHandler() {
		return session.getHandler();
	}

	/**
	 * @return the configuration of this session.
	 */
	@Override
	public IoSessionConfig getConfig() {
		return session.getConfig();
	}

	/**
	 * @return the filter chain that only affects this session.
	 */
	@Override
	public IoFilterChain getFilterChain() {
		return session.getFilterChain();
	}

	/**
	 * Get the queue that contains the message waiting for being written.
	 * As the reader might not be ready, it's frequent that the messages
	 * aren't written completely, or that some older messages are waiting
	 * to be written when a new message arrives. This queue is used to manage
	 * the backlog of messages.
	 *
	 * @return The queue containing the pending messages.
	 */
	@Override
	public WriteRequestQueue getWriteRequestQueue() {
		return session.getWriteRequestQueue();
	}

	/**
	 * @return the {@link TransportMetadata} that this session runs on.
	 */
	@Override
	public TransportMetadata getTransportMetadata() {
		return session.getTransportMetadata();
	}

	/**
	 * TODO This javadoc is wrong. The return tag should be short.
	 *
	 * @return a {@link ReadFuture} which is notified when a new message is
	 * received, the connection is closed or an exception is caught.  This
	 * operation is especially useful when you implement a client application.
	 * TODO : Describe here how we enable this feature.
	 * However, please note that this operation is disabled by default and
	 * throw {@link IllegalStateException} because all received events must be
	 * queued somewhere to support this operation, possibly leading to memory
	 * leak.  This means you have to keep calling {@link #read()} once you
	 * enabled this operation.  To enable this operation, please call
	 * {@link IoSessionConfig#setUseReadOperation(boolean)} with <tt>true</tt>.
	 *
	 * @throws IllegalStateException if
	 * {@link IoSessionConfig#setUseReadOperation(boolean) useReadOperation}
	 * option has not been enabled.
	 */
	@Override
	public ReadFuture read() {
		return session.read();
	}

	/**
	 * Writes the specified <code>message</code> to remote peer.  This
	 * operation is asynchronous; {@link IoHandler#messageSent(IoSession, Object)}
	 * will be invoked when the message is actually sent to remote peer.
	 * You can also wait for the returned {@link WriteFuture} if you want
	 * to wait for the message actually written.
	 *
	 * @param message The message to write
	 * @return The associated WriteFuture
	 */
	@Override
	public WriteFuture write(Object message) {
		return session.write(message);
	}

	/**
	 * (Optional) Writes the specified <tt>message</tt> to the specified <tt>destination</tt>.
	 * This operation is asynchronous; {@link IoHandler#messageSent(IoSession, Object)}
	 * will be invoked when the message is actually sent to remote peer. You can
	 * also wait for the returned {@link WriteFuture} if you want to wait for
	 * the message actually written.
	 * <p>
	 * When you implement a client that receives a broadcast message from a server
	 * such as DHCP server, the client might need to send a response message for the
	 * broadcast message the server sent.  Because the remote address of the session
	 * is not the address of the server in case of broadcasting, there should be a
	 * way to specify the destination when you write the response message.
	 * This interface provides {@link #write(Object, SocketAddress)} method so you
	 * can specify the destination.
	 *
	 * @param message The message to write
	 * @param destination <tt>null</tt> if you want the message sent to the
	 *                    default remote address
	 * @return The associated WriteFuture
	 */
	@Override
	public WriteFuture write(Object message, SocketAddress destination) {
		return session.write(message, destination);
	}

	/**
	 * Closes this session immediately or after all queued write requests
	 * are flushed.  This operation is asynchronous.  Wait for the returned
	 * {@link CloseFuture} if you want to wait for the session actually closed.
	 *
	 * @param immediately {@code true} to close this session immediately
	 *                    . The pending write requests
	 *                    will simply be discarded.
	 *                    {@code false} to close this session after all queued
	 *                    write requests are flushed.
	 * @return The associated CloseFuture
	 * @deprecated Use either the closeNow() or the flushAndClose() methods
	 */
	@Deprecated
	@Override
	public CloseFuture close(boolean immediately) {
		return session.close(immediately);
	}

	/**
	 * Closes this session immediately.  This operation is asynchronous, it
	 * returns a {@link CloseFuture}.
	 *
	 * @return The {@link CloseFuture} that can be use to wait for the completion of this operation
	 */
	@Override
	public CloseFuture closeNow() {
		return session.closeNow();
	}

	/**
	 * Closes this session after all queued write requests are flushed.  This operation
	 * is asynchronous.  Wait for the returned {@link CloseFuture} if you want to wait
	 * for the session actually closed.
	 *
	 * @return The associated CloseFuture
	 */
	@Override
	public CloseFuture closeOnFlush() {
		return session.closeOnFlush();
	}

	/**
	 * Closes this session after all queued write requests
	 * are flushed. This operation is asynchronous.  Wait for the returned
	 * {@link CloseFuture} if you want to wait for the session actually closed.
	 * @deprecated use {@link #close(boolean)}
	 *
	 * @return The associated CloseFuture
	 */
	@Override
	@Deprecated
	public CloseFuture close() {
		return session.close();
	}

	/**
	 * Returns an attachment of this session.
	 * This method is identical with <tt>getAttribute( "" )</tt>.
	 *
	 * @return The attachment
	 * @deprecated Use {@link #getAttribute(Object)} instead.
	 */
	@Override
	@Deprecated
	public Object getAttachment() {
		return session.getAttachment();
	}

	/**
	 * Sets an attachment of this session.
	 * This method is identical with <tt>setAttribute( "", attachment )</tt>.
	 *
	 * @param attachment The attachment
	 * @return Old attachment. <tt>null</tt> if it is new.
	 * @deprecated Use {@link #setAttribute(Object, Object)} instead.
	 */
	@Override
	@Deprecated
	public Object setAttachment(Object attachment) {
		return session.setAttachment(attachment);
	}

	/**
	 * Returns the value of the user-defined attribute of this session.
	 *
	 * @param key the key of the attribute
	 * @return <tt>null</tt> if there is no attribute with the specified key
	 */
	@Override
	public Object getAttribute(Object key) {
		return session.getAttribute(key);
	}

	/**
	 * Returns the value of user defined attribute associated with the
	 * specified key.  If there's no such attribute, the specified default
	 * value is associated with the specified key, and the default value is
	 * returned.  This method is same with the following code except that the
	 * operation is performed atomically.
	 * <pre>
	 * if (containsAttribute(key)) {
	 *     return getAttribute(key);
	 * } else {
	 *     setAttribute(key, defaultValue);
	 *     return defaultValue;
	 * }
	 * </pre>
	 *
	 * @param key the key of the attribute we want to retreive
	 * @param defaultValue the default value of the attribute
	 * @return The retrieved attribute or <tt>null</tt> if not found
	 */
	@Override
	public Object getAttribute(Object key, Object defaultValue) {
		return session.getAttribute(key, defaultValue);
	}

	/**
	 * Sets a user-defined attribute.
	 *
	 * @param key the key of the attribute
	 * @param value the value of the attribute
	 * @return The old value of the attribute.  <tt>null</tt> if it is new.
	 */
	@Override
	public Object setAttribute(Object key, Object value) {
		return session.setAttribute(key, value);
	}

	/**
	 * Sets a user defined attribute without a value.  This is useful when
	 * you just want to put a 'mark' attribute.  Its value is set to
	 * {@link Boolean#TRUE}.
	 *
	 * @param key the key of the attribute
	 * @return The old value of the attribute.  <tt>null</tt> if it is new.
	 */
	@Override
	public Object setAttribute(Object key) {
		return session.setAttribute(key);
	}

	/**
	 * Sets a user defined attribute if the attribute with the specified key
	 * is not set yet.  This method is same with the following code except
	 * that the operation is performed atomically.
	 * <pre>
	 * if (containsAttribute(key)) {
	 *     return getAttribute(key);
	 * } else {
	 *     return setAttribute(key, value);
	 * }
	 * </pre>
	 *
	 * @param key The key of the attribute we want to set
	 * @param value The value we want to set
	 * @return The old value of the attribute.  <tt>null</tt> if not found.
	 */
	@Override
	public Object setAttributeIfAbsent(Object key, Object value) {
		return session.setAttributeIfAbsent(key, value);
	}

	/**
	 * Sets a user defined attribute without a value if the attribute with
	 * the specified key is not set yet.  This is useful when you just want to
	 * put a 'mark' attribute.  Its value is set to {@link Boolean#TRUE}.
	 * This method is same with the following code except that the operation
	 * is performed atomically.
	 * <pre>
	 * if (containsAttribute(key)) {
	 *     return getAttribute(key);  // might not always be Boolean.TRUE.
	 * } else {
	 *     return setAttribute(key);
	 * }
	 * </pre>
	 *
	 * @param key The key of the attribute we want to set
	 * @return The old value of the attribute.  <tt>null</tt> if not found.
	 */
	@Override
	public Object setAttributeIfAbsent(Object key) {
		return session.setAttributeIfAbsent(key);
	}

	/**
	 * Removes a user-defined attribute with the specified key.
	 *
	 * @param key The key of the attribute we want to remove
	 * @return The old value of the attribute.  <tt>null</tt> if not found.
	 */
	@Override
	public Object removeAttribute(Object key) {
		return session.removeAttribute(key);
	}

	/**
	 * Removes a user defined attribute with the specified key if the current
	 * attribute value is equal to the specified value.  This method is same
	 * with the following code except that the operation is performed
	 * atomically.
	 * <pre>
	 * if (containsAttribute(key) &amp;&amp; getAttribute(key).equals(value)) {
	 *     removeAttribute(key);
	 *     return true;
	 * } else {
	 *     return false;
	 * }
	 * </pre>
	 *
	 * @param key The key we want to remove
	 * @param value The value we want to remove
	 * @return <tt>true</tt> if the removal was successful
	 */
	@Override
	public boolean removeAttribute(Object key, Object value) {
		return session.removeAttribute(key, value);
	}

	/**
	 * Replaces a user defined attribute with the specified key if the
	 * value of the attribute is equals to the specified old value.
	 * This method is same with the following code except that the operation
	 * is performed atomically.
	 * <pre>
	 * if (containsAttribute(key) &amp;&amp; getAttribute(key).equals(oldValue)) {
	 *     setAttribute(key, newValue);
	 *     return true;
	 * } else {
	 *     return false;
	 * }
	 * </pre>
	 *
	 * @param key The key we want to replace
	 * @param oldValue The previous value
	 * @param newValue The new value
	 * @return <tt>true</tt> if the replacement was successful
	 */
	@Override
	public boolean replaceAttribute(Object key, Object oldValue, Object newValue) {
		return session.replaceAttribute(key, oldValue, newValue);
	}

	/**
	 * @param key The key of the attribute we are looking for in the session
	 * @return <tt>true</tt> if this session contains the attribute with
	 * the specified <tt>key</tt>.
	 */
	@Override
	public boolean containsAttribute(Object key) {
		return session.containsAttribute(key);
	}

	/**
	 * @return the set of keys of all user-defined attributes.
	 */
	@Override
	public Set<Object> getAttributeKeys() {
		return session.getAttributeKeys();
	}

	/**
	 * @return <tt>true</tt> if this session is connected with remote peer.
	 */
	@Override
	public boolean isConnected() {
		return session.isConnected();
	}

	/**
	 * @return <tt>true</tt> if this session is active.
	 */
	@Override
	public boolean isActive() {
		return session.isActive();
	}

	/**
	 * @return <tt>true</tt> if and only if this session is being closed
	 * (but not disconnected yet) or is closed.
	 */
	@Override
	public boolean isClosing() {
		return session.isClosing();
	}

	/**
	 * @return <tt>true</tt> if the session has started and initialized a SslEngine,
	 * <tt>false</tt> if the session is not yet secured (the handshake is not completed)
	 * or if SSL is not set for this session, or if SSL is not even an option.
	 */
	@Override
	public boolean isSecured() {
		return session.isSecured();
	}

	/**
	 * @return the {@link CloseFuture} of this session.  This method returns
	 * the same instance whenever user calls it.
	 */
	@Override
	public CloseFuture getCloseFuture() {
		return session.getCloseFuture();
	}

	/**
	 * @return the socket address of remote peer.
	 */
	@Override
	public SocketAddress getRemoteAddress() {
		return session.getRemoteAddress();
	}

	/**
	 * @return the socket address of local machine which is associated with this
	 * session.
	 */
	@Override
	public SocketAddress getLocalAddress() {
		return session.getLocalAddress();
	}

	/**
	 * @return the socket address of the {@link IoService} listens to to manage
	 * this session.  If this session is managed by {@link IoAcceptor}, it
	 * returns the {@link SocketAddress} which is specified as a parameter of
	 * {@link IoAcceptor#bind()}.  If this session is managed by
	 * {@link IoConnector}, this method returns the same address with
	 * that of {@link #getRemoteAddress()}.
	 */
	@Override
	public SocketAddress getServiceAddress() {
		return session.getServiceAddress();
	}

	/**
	 *
	 * Associate the current write request with the session
	 *
	 * @param currentWriteRequest the current write request to associate
	 */
	@Override
	public void setCurrentWriteRequest(WriteRequest currentWriteRequest) {
		session.setCurrentWriteRequest(currentWriteRequest);
	}

	/**
	 * Suspends read operations for this session.
	 */
	@Override
	public void suspendRead() {
		session.suspendRead();
	}

	/**
	 * Suspends write operations for this session.
	 */
	@Override
	public void suspendWrite() {
		session.suspendWrite();
	}

	/**
	 * Resumes read operations for this session.
	 */
	@Override
	public void resumeRead() {
		session.resumeRead();
	}

	/**
	 * Resumes write operations for this session.
	 */
	@Override
	public void resumeWrite() {
		session.resumeWrite();
	}

	/**
	 * Is read operation is suspended for this session.
	 *
	 * @return <tt>true</tt> if suspended
	 */
	@Override
	public boolean isReadSuspended() {
		return session.isReadSuspended();
	}

	/**
	 * Is write operation is suspended for this session.
	 *
	 * @return <tt>true</tt> if suspended
	 */
	@Override
	public boolean isWriteSuspended() {
		return session.isWriteSuspended();
	}

	/**
	 * Update all statistical properties related with throughput assuming
	 * the specified time is the current time.  By default this method returns
	 * silently without updating the throughput properties if they were
	 * calculated already within last
	 * {@link IoSessionConfig#getThroughputCalculationInterval() calculation interval}.
	 * If, however, <tt>force</tt> is specified as <tt>true</tt>, this method
	 * updates the throughput properties immediately.
	 * @param currentTime the current time in milliseconds
	 * @param force Force the update if <tt>true</tt>
	 */
	@Override
	public void updateThroughput(long currentTime, boolean force) {
		session.updateThroughput(currentTime, force);
	}

	/**
	 * @return the total number of bytes which were read from this session.
	 */
	@Override
	public long getReadBytes() {
		return session.getReadBytes();
	}

	/**
	 * @return the total number of bytes which were written to this session.
	 */
	@Override
	public long getWrittenBytes() {
		return session.getWrittenBytes();
	}

	/**
	 * @return the total number of messages which were read and decoded from this session.
	 */
	@Override
	public long getReadMessages() {
		return session.getReadMessages();
	}

	/**
	 * @return the total number of messages which were written and encoded by this session.
	 */
	@Override
	public long getWrittenMessages() {
		return session.getWrittenMessages();
	}

	/**
	 * @return the number of read bytes per second.
	 */
	@Override
	public double getReadBytesThroughput() {
		return session.getReadBytesThroughput();
	}

	/**
	 * @return the number of written bytes per second.
	 */
	@Override
	public double getWrittenBytesThroughput() {
		return session.getWrittenBytesThroughput();
	}

	/**
	 * @return the number of read messages per second.
	 */
	@Override
	public double getReadMessagesThroughput() {
		return session.getReadMessagesThroughput();
	}

	/**
	 * @return the number of written messages per second.
	 */
	@Override
	public double getWrittenMessagesThroughput() {
		return session.getWrittenMessagesThroughput();
	}

	/**
	 * @return the number of messages which are scheduled to be written to this session.
	 */
	@Override
	public int getScheduledWriteMessages() {
		return session.getScheduledWriteMessages();
	}

	/**
	 * @return the number of bytes which are scheduled to be written to this
	 * session.
	 */
	@Override
	public long getScheduledWriteBytes() {
		return session.getScheduledWriteBytes();
	}

	/**
	 * Returns the message which is being written by {@link IoService}.
	 * @return <tt>null</tt> if and if only no message is being written
	 */
	@Override
	public Object getCurrentWriteMessage() {
		return session.getCurrentWriteMessage();
	}

	/**
	 * Returns the {@link WriteRequest} which is being processed by
	 * {@link IoService}.
	 *
	 * @return <tt>null</tt> if and if only no message is being written
	 */
	@Override
	public WriteRequest getCurrentWriteRequest() {
		return session.getCurrentWriteRequest();
	}

	/**
	 * @return the session's creation time in milliseconds
	 */
	@Override
	public long getCreationTime() {
		return session.getCreationTime();
	}

	/**
	 * @return the time in millis when I/O occurred lastly.
	 */
	@Override
	public long getLastIoTime() {
		return session.getLastIoTime();
	}

	/**
	 * @return the time in millis when read operation occurred lastly.
	 */
	@Override
	public long getLastReadTime() {
		return session.getLastReadTime();
	}

	/**
	 * @return the time in millis when write operation occurred lastly.
	 */
	@Override
	public long getLastWriteTime() {
		return session.getLastWriteTime();
	}

	/**
	 * @param status The researched idle status
	 * @return <tt>true</tt> if this session is idle for the specified
	 * {@link IdleStatus}.
	 */
	@Override
	public boolean isIdle(IdleStatus status) {
		return session.isIdle(status);
	}

	/**
	 * @return <tt>true</tt> if this session is {@link IdleStatus#READER_IDLE}.
	 * @see #isIdle(IdleStatus)
	 */
	@Override
	public boolean isReaderIdle() {
		return session.isReaderIdle();
	}

	/**
	 * @return <tt>true</tt> if this session is {@link IdleStatus#WRITER_IDLE}.
	 * @see #isIdle(IdleStatus)
	 */
	@Override
	public boolean isWriterIdle() {
		return session.isWriterIdle();
	}

	/**
	 * @return <tt>true</tt> if this session is {@link IdleStatus#BOTH_IDLE}.
	 * @see #isIdle(IdleStatus)
	 */
	@Override
	public boolean isBothIdle() {
		return session.isBothIdle();
	}

	/**
	 * @param status The researched idle status
	 * @return the number of the fired continuous <tt>sessionIdle</tt> events
	 * for the specified {@link IdleStatus}.
	 * <p>
	 * If <tt>sessionIdle</tt> event is fired first after some time after I/O,
	 * <tt>idleCount</tt> becomes <tt>1</tt>.  <tt>idleCount</tt> resets to
	 * <tt>0</tt> if any I/O occurs again, otherwise it increases to
	 * <tt>2</tt> and so on if <tt>sessionIdle</tt> event is fired again without
	 * any I/O between two (or more) <tt>sessionIdle</tt> events.
	 */
	@Override
	public int getIdleCount(IdleStatus status) {
		return session.getIdleCount(status);
	}

	/**
	 * @return the number of the fired continuous <tt>sessionIdle</tt> events
	 * for {@link IdleStatus#READER_IDLE}.
	 * @see #getIdleCount(IdleStatus)
	 */
	@Override
	public int getReaderIdleCount() {
		return session.getReaderIdleCount();
	}

	/**
	 * @return the number of the fired continuous <tt>sessionIdle</tt> events
	 * for {@link IdleStatus#WRITER_IDLE}.
	 * @see #getIdleCount(IdleStatus)
	 */
	@Override
	public int getWriterIdleCount() {
		return session.getWriterIdleCount();
	}

	/**
	 * @return the number of the fired continuous <tt>sessionIdle</tt> events
	 * for {@link IdleStatus#BOTH_IDLE}.
	 * @see #getIdleCount(IdleStatus)
	 */
	@Override
	public int getBothIdleCount() {
		return session.getBothIdleCount();
	}

	/**
	 * @param status The researched idle status
	 * @return the time in milliseconds when the last <tt>sessionIdle</tt> event
	 * is fired for the specified {@link IdleStatus}.
	 */
	@Override
	public long getLastIdleTime(IdleStatus status) {
		return session.getLastIdleTime(status);
	}

	/**
	 * @return the time in milliseconds when the last <tt>sessionIdle</tt> event
	 * is fired for {@link IdleStatus#READER_IDLE}.
	 * @see #getLastIdleTime(IdleStatus)
	 */
	@Override
	public long getLastReaderIdleTime() {
		return session.getLastReaderIdleTime();
	}

	/**
	 * @return the time in milliseconds when the last <tt>sessionIdle</tt> event
	 * is fired for {@link IdleStatus#WRITER_IDLE}.
	 * @see #getLastIdleTime(IdleStatus)
	 */
	@Override
	public long getLastWriterIdleTime() {
		return session.getLastWriterIdleTime();
	}

	/**
	 * @return the time in milliseconds when the last <tt>sessionIdle</tt> event
	 * is fired for {@link IdleStatus#BOTH_IDLE}.
	 * @see #getLastIdleTime(IdleStatus)
	 */
	@Override
	public long getLastBothIdleTime() {
		return session.getLastBothIdleTime();
	}
}
