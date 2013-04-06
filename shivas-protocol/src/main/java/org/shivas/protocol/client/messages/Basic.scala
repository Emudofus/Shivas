package org.shivas.protocol.client.messages

import org.shivas.protocol.client.{Parsable, Formattable}
import org.joda.time.ReadableInstant
import org.joda.time.format.DateTimeFormat
import beans.BeanProperty
import org.shivas.protocol.client.enums.ChannelEnum

/**
 * @author Blackrush
 */
object Basic {

  object Nothing {
    object Message extends Formattable {
      def render() = "BN"
    }
    def msg = Message
  }

  object Date {
    val pattern = "yyyy|MM|dd"
    val formatter = DateTimeFormat.forPattern(pattern)

    case class Message(now: ReadableInstant) extends Formattable {
      def render() = "BD" + formatter.print(now)
    }
    def msg(now: ReadableInstant) = new Message(now)
  }

  object Console {
    val defaultColor = 2

    case class Message(text: String, color: Int) extends Formattable {
      def render() = "BAT" + color + text
    }
    def msg(text: String, color: Int = defaultColor) = new Message(text, color)
  }

  object Alert {
    case class Message(text: String) extends Formattable {
      def render() = "BAIO<b>" + text + "</b>"
    }
    def msg(text: String) = new Message(text)
  }

  object WhoIsSuccess {
    val unknownArea = -1

    case class Message(nickname: String, name: String, singleGame: Boolean, area: Int) extends Formattable {
      def render() = "BWK" + nickname + "|" + (if (singleGame) "1" else "2") + "|" + name + "|" + area
    }
    def msg(nickname: String, name: String, singleGame: Boolean = true, area: Int = unknownArea) = new Message(nickname, name, singleGame, area)
  }

  object WhoIsError {
    case class Message(nickname: String) extends Formattable {
      def render() = "BWE" + nickname
    }
    def msg(nickname: String) = new Message(nickname)
  }

  object AdminCommand extends Parsable {
    case class Message(command: String)

    @BeanProperty
    val header = "BA"

    def parse(input: String) = Message(input.substring(header.length))
  }

  object DateRequest extends Parsable {
    object Message

    @BeanProperty
    val header = "BD"

    def parse(input: String) = Message
  }

  object ClientMessage extends Parsable {
    case class Public(channel: ChannelEnum, text: String)
    case class Private(senderId: Long, text: String)

    @BeanProperty
    val header = "BM"

    def parse(input: String) = {
      val args = input.split("\\|")
      val text = args(1)

      if (!args(0).isEmpty)
        Private(args(0).toInt, text)
      else
        Public(ChannelEnum.valueOf(args(0).charAt(0)), text)
    }
  }
}
