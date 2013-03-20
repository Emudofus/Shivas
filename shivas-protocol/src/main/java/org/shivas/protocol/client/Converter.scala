package org.shivas.protocol.client

import java.io.{FileInputStream, FileReader, BufferedReader, File}
import java.nio.channels.FileChannel
import java.nio.charset.Charset

/**
 * @author Blackrush
 */
object Converter extends App {
  var fileName: String = null
  do {
    fileName = readLine("Enter file's path")
    handle(readFile(fileName))
  } while (!fileName.isEmpty)

  def readFile(fileName: String): String = {
    val fis = new FileInputStream(new File(fileName))
    try {
      val channel = fis.getChannel
      val buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size())

      Charset.forName("UTF-8").decode(buf).toString
    } finally {
      fis.close()
    }
  }

  def handle(code: String) {
    code.indexOf("")
  }
}
