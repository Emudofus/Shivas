package org.shivas.protocol.client.messages

import org.shivas.protocol.client.enums.ChannelEnum
import org.shivas.protocol.client.{Parsable, Formattable}
import beans.BeanProperty
import org.shivas.common.MutableTree

/**
 * @author Blackrush
 */
object Channel {
  object ChannelList {
    case class Message(channels: Seq[ChannelEnum]) extends Formattable {
      def render() = "cC+" + channels.map(_.value).mkString
    }
    def msg(channels: Seq[ChannelEnum]) = new Message(channels)
  }

  object AddChannel {
    case class Message(channel: ChannelEnum) extends Formattable {
      def render() = "cC+" + channel.value
    }
    def msg(channel: ChannelEnum) = new Message(channel)
  }

  object RemoveChannel {
    case class Message(channel: ChannelEnum) extends Formattable {
      def render() = "cC-" + channel.value
    }
    def msg(channel: ChannelEnum) = new Message(channel)
  }

  object EmoteList {
    val defaultSuffix = "0"

    case class Message(emotes: String, suffix: String) extends Formattable {
      def render() = "eL" + emotes + "|" + suffix
    }
    def msg(emotes: String, suffix: String = defaultSuffix) = new Message(emotes, suffix)
  }

  object PublicClientMessage {
    case class Message(channel: ChannelEnum, authorId: Long, authorName: String, text: String) extends Formattable {
      def render() = "cMK" + Seq(
        channel.value,
        authorId, authorName,
        text
      ).mkString("|")
    }
    def msg(channel: ChannelEnum, authorId: Long, authorName: String, text: String) = new Message(channel, authorId, authorName, text)
  }

  object PrivateClientMessage {
    case class Message(copy: Boolean, senderId: Long, senderName: String, text: String) extends Formattable {
      def render() = "cMK" + Seq(
        if (copy) "F" else "T",
        senderId, senderName,
        text
      ).mkString("|")
    }
    def msg(copy: Boolean, senderId: Long, senderName: String, text: String) = new Message(copy, senderId, senderName, text)
  }

  object PublicClientMessageError {
    object Message extends Formattable {
      def render() = "cMEf"
    }
    def msg = Message
  }

  object Println {
    case class Message(text: String) extends Formattable {
      def render() = "cs" + text
    }
    def msg(text: String) = new Message(text)
  }

  object ChannelUpdate extends Parsable {
    case class Message(channel: ChannelEnum, add: Boolean)

    @BeanProperty
    val header = "cC"

    def parse(input: String) = Message(ChannelEnum.valueOf(input(3)), input(2) == '+')
  }

  val parsables = MutableTree.build[Char, Parsable] { it =>
    it(ChannelUpdate.header) = ChannelUpdate
  }
}
