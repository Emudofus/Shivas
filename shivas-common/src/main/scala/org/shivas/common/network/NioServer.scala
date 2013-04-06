package org.shivas.common.network

import org.joda.time.Instant
import collection.mutable
import org.shivas.common.event._
import org.shivas.common._
import java.util.concurrent.{Executors, Executor}
import com.google.common.base.Preconditions._
import java.nio.channels.spi.SelectorProvider
import java.nio.channels.{SocketChannel, SelectionKey, ServerSocketChannel}
import java.net.SocketAddress
import java.nio.ByteBuffer
import scala.Some

/**
 * @author Blackrush
 */
class NioServer private(address: SocketAddress, var protocol: Protocol, val worker: Executor, handlerManager: EventHandlerManager)
  extends ThreadedEventBus(worker, ScalaEventHandlerManager then handlerManager)
  with Server
{
  def start() {
    checkState(!running && startup.isEmpty) // is not running and has not been started

    startup = Some(Instant.now)
    running = true

    worker.execute(work)
  }

  def stop() {
    checkState(running)

    running = false
  }

  def handler(handler: ScalaEventHandlerManager.Handler) {
    this += handler
  }

  private val selector = SelectorProvider.provider().openSelector()
  private val server = ServerSocketChannel.open()

  private def work() {
    server.configureBlocking(false)
    server.bind(address)
    server.register(selector, SelectionKey.OP_ACCEPT)

    while (running) {
      selector.select()
      val it = selector.selectedKeys().iterator

      while (it.hasNext) {
        val key = it.next()
        it.remove()

        val session = Option(key.attachment).map(_.asInstanceOf[NioSession])

        if (key.isValid) {
          key match {
            case _ if key.isAcceptable => this ! NewClient
            case _ if key.isReadable => this ! Read(session.get)
          }
        }
      }
    }
  }

  handler {
    case NewClient         => accept()
    case Read(session)     => read(session)
    case Write(session, o) => write(session, o)
    case Close(session)    => close(session)
  }

  private def accept() {
    val accepted = server.accept()

    accepted.configureBlocking(false)
    val key = accepted.register(selector, SelectionKey.OP_READ)

    val session = new NioSession(this, key)
    key.attach(session)

    connected += 1
    if (connected > maxConnected) {
      connected = maxConnected
    }

    this ! Connected(session)
  }

  private def read(session: NioSession) {
    val buf = session.buffer
    val read = session.socket.read(buf)

    if (read < 0) {
      session.close()
    } else {
      for (data <- protocol.decode(buf)) {
        this ! Received(session, data)
      }

      buf.clear()
    }
  }

  private def write(session: NioSession, o: Any) {
    val data = protocol.encode(o)
    session.socket.write(data)

    this ! Sent(session, o)
  }

  private def close(session: NioSession) {
    val socket = session.socket

    session.key.cancel()
    socket.shutdownInput().shutdownOutput()
    socket.close()

    connected -= 1
    this ! Disconnected(session)
  }

  var startup: Option[Instant] = None
  var running = false
  var connected = 0
  var maxConnected = 0
}

object NewClient extends Event
case class Connected(session: NioSession) extends Event
case class Close(session: NioSession) extends Event
case class Disconnected(session: NioSession) extends Event
case class Write(session: NioSession, o: Any) extends Event
case class Sent(session: NioSession, o: Any) extends Event
case class Read(session: NioSession) extends Event
case class Received(session: NioSession, o: Any) extends Event

class NioSession(server: NioServer, private[network] val key: SelectionKey) extends Session {
  private[network] val socket = key.channel.asInstanceOf[SocketChannel]
  private val attachments = mutable.Map.empty[Class[_], Any]

  val buffer = ByteBuffer.allocate(NioServer.defaultCapacity)

  def write(o: Any) {
    server ! Write(this, o)
  }

  def close() {
    server ! Close(this)
  }

  server.handler {
    case Received(session, o) => session ! o
  }

  def attachment[T](klass: Class[T]) = attachments(klass).asInstanceOf[T]
  def addAttachment[T](attachment: T) = attachments(attachment.getClass) = attachment
}

object NioServer {
  val defaultWorkers = 2
  val defaultCapacity = 1024

  def create(address: SocketAddress, protocol: Protocol, handlerManager: EventHandlerManager, worker: Executor = Executors.newFixedThreadPool(defaultWorkers)) =
    new NioServer(address, protocol, worker, handlerManager)

  def apply(address: SocketAddress, protocol: Protocol, handlerManager: EventHandlerManager, worker: Executor = Executors.newFixedThreadPool(defaultWorkers)) =
    create(address, protocol, handlerManager, worker)
}
