package org.shivas.common.network

import java.nio.ByteBuffer
import java.nio.charset.Charset

/**
 * @author Blackrush
 */
class DelimitedStringProtocol(delimiter: String, charset: Charset = Charset.defaultCharset()) extends Protocol {
  def isDefinedAt(o: Any) = o.isInstanceOf[String]

  def encode(o: Any) = charset.encode(o.toString + delimiter)
  def decode(buf: ByteBuffer) = charset.decode(buf).toString.split(delimiter)
}
