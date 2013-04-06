package org.shivas.protocol.client.messages

import org.shivas.protocol.client.{Parsable, Formattable}
import org.shivas.protocol.client.types.{BaseCharactersServerType, GameServerType}
import beans.BeanProperty
import org.shivas.common.MutableTree

/**
 * @author Blackrush
 */
object Login {
  object HelloConnect {
    case class Message(ticket: String) extends Formattable {
      def render() = "HC" + ticket
    }
    def msg(ticket: String) = new Message(ticket)
  }

  object BadClient {
    case class Message(required: String) extends Formattable {
      def render() = "AlEv" + required
    }
    def msg(required: String) = new Message(required)
  }

  object AccessDenied {
    object Message extends Formattable {
      def render() = "AlEf"
    }
    def msg = Message
  }

  object Banned {
    object Message extends Formattable {
      def render() = "AlEb"
    }
    def msg = Message
  }

  object AlreadyConnected {
    object Message extends Formattable {
      def render() = "AlEc"
    }
    def msg = Message
  }

  object NicknameInfo {
    case class Message(nickname: String) extends Formattable {
      def render() = "Ad" + nickname
    }
    def msg(nickname: String) = new Message(nickname)
  }

  object CommunityInfo {
    case class Message(community: Int) extends Formattable {
      def render() = "Ac" + community
    }
    def msg(community: Int) = new Message(community)
  }

  object IdentificationSuccess {
    case class Message(hasRights: Boolean) extends Formattable {
      def render() = "AlK" + (if (hasRights) "1" else "0")
    }
    def msg(hasRights: Boolean) = new Message(hasRights)
  }

  object AccountQuestionInfo {
    case class Message(question: String) extends Formattable {
      def render() = "AQ" + question.replace(" ", "+")
    }
    def msg(question: String) = new Message(question)
  }

  object ServersInfo {
    case class Message(servers: Iterable[GameServerType]) extends Formattable {
      def render() = "AH" + servers.map { x => Seq(
        x.getId, x.getState.ordinal(), x.getCompletion,
        if (x.isJoinable) '1' else '0'
      ).mkString(";") }.mkString("|")
    }
    def msg(servers: Iterable[GameServerType]) = new Message(servers)
  }

  object PlayersList {
    case class Message(subscription: Long, players: Iterable[BaseCharactersServerType]) extends Formattable {
      def render() = "AxK" + subscription + "|" + players.map { x => Seq(
        x.getServerId, x.getCharacters
      ).mkString(",") }.mkString("|")
    }
    def msg(subscription: Long, players: Iterable[BaseCharactersServerType]) = new Message(subscription, players)
  }

  object ServerSelectionSuccess {
    case class Message(address: String, port: Int, ticket: String) extends Formattable {
      def render() = "AYK" + address + ":" + port + ";" + ticket
    }
    def msg(address: String, port: Int, ticket: String) = new Message(address, port, ticket)
  }

  object ServerSelectionError {
    object Message extends Formattable {
      def render() = "AYE"
    }
    def msg = Message
  }

  object VersionInfo extends Parsable {
    case class Message(version: String)

    @BeanProperty
    val header = ""
    def parse(input: String) = Message(input)
  }

  object Authentication extends Parsable {
    case class Message(username: String, password: String)

    @BeanProperty
    val header = ""
    def parse(input: String) = {
      val args = input.split("\n")
      Message(args(0), args(1))
    }
  }

  object ServersList extends Parsable {
    object Message

    @BeanProperty
    val header = "Ax"
    def parse(input: String) = Message
  }

  object ServerSelection extends Parsable {
    case class Message(serverId: Int)

    @BeanProperty
    val header = "AX"
    def parse(input: String) = Message(input.substring(header.length).toInt)
  }

  object QueueInfo extends Parsable {
    object Message

    @BeanProperty
    val header = "Af"
    def parse(input: String) = Message
  }

  val parsables = MutableTree.build[Char, Parsable] { it =>
    it(ServersList.header)     = ServersList
    it(ServerSelection.header) = ServerSelection
    it(QueueInfo.header)       = QueueInfo
  }
}
