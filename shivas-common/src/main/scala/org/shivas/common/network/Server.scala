package org.shivas.common.network

import org.joda.time.Instant
import beans.BeanProperty
import java.nio.ByteBuffer

/**
 * @author Blackrush
 */
trait Server {
  def start()
  def stop()

  @BeanProperty
  val startup: Option[Instant]

  @BeanProperty
  var protocol: Protocol

  @BeanProperty
  def running: Boolean

  @BeanProperty
  def connected: Int

  @BeanProperty
  def maxConnected: Int
}

trait Session {
  def write(o: Any)
  def !(o: Any) = write(o)

  @BeanProperty
  def attachment[T](klass: Class[T]): T

  def addAttachment[T](attachment: T)

  @BeanProperty
  def attachment[T](implicit m: Manifest[T]): T = attachment(m.runtimeClass).asInstanceOf[T]

  def +=[T](attachment: T) = addAttachment(attachment)

  def close()
}

trait Protocol {
  def isDefinedAt(o: Any): Boolean

  def encode(o: Any): ByteBuffer
  def decode(buf: ByteBuffer): Iterable[_]
}
