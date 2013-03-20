package org.shivas.protocol.client.messages

import org.shivas.protocol.client.{Parsable, Formattable}
import org.shivas.protocol.client.types.{BaseCharactersServerType, BaseCharacterType, GameServerType}
import beans.BeanProperty

/**
 * @author Blackrush
 */
object Login {
  object HelloConnect {
    private class Message(ticket: String) extends Formattable {
      def render() = "HC" + ticket
    }
    def msg(ticket: String) = new Message(ticket)
  }

  object BadClient {
    private class Message(required: String) extends Formattable {
      def render() = "AlEv" + required
    }
    def msg(required: String) = new Message(required)
  }

  object AccessDenied {
    private class Message extends Formattable {
      def render() = "AlEf"
    }
    def msg = new Message
  }

  object Banned {
    private class Message extends Formattable {
      def render() = "AlEb"
    }
    def msg = new Message
  }

  object AlreadyConnected {
    private class Message extends Formattable {
      def render() = "AlEc"
    }
    def msg = new Message
  }

  object NicknameInfo {
    private class Message(nickname: String) extends Formattable {
      def render() = "Ad" + nickname
    }
    def msg(nickname: String) = new Message(nickname)
  }

  object CommunityInfo {
    private class Message(community: Int) extends Formattable {
      def render() = "Ac" + community
    }
    def msg(community: Int) = new Message(community)
  }

  object IdentificationSuccess {
    private class Message(hasRights: Boolean) extends Formattable {
      def render() = "AlK" + (if (hasRights) "1" else "0")
    }
    def msg(hasRights: Boolean) = new Message(hasRights)
  }

  object AccountQuestionInfo {
    private class Message(question: String) extends Formattable {
      def render() = "AQ" + question.replace(" ", "+")
    }
    def msg(question: String) = new Message(question)
  }

  object ServersInfo {
    private class Message(servers: Iterable[GameServerType]) extends Formattable {
      def render() = "AH" + servers.map { x => Seq(
        x.getId, x.getState.ordinal(), x.getCompletion,
        if (x.isJoinable) '1' else '0'
      ).mkString(";") }.mkString("|")
    }
    def msg(servers: Iterable[GameServerType]) = new Message(servers)
  }

  object PlayersList {
    private class Message(subscription: Long, players: Iterable[BaseCharactersServerType]) extends Formattable {
      def render() = "AxK" + subscription + "|" + players.map { x => Seq(
        x.getServerId, x.getCharacters
      ).mkString(",") }.mkString("|")
    }
    def msg(subscription: Long, players: Iterable[BaseCharactersServerType]) = new Message(subscription, players)
  }

  object ServerSelectionSuccess {
    private class Message(address: String, port: Int, ticket: String) extends Formattable {
      def render() = "AYK" + address + ":" + port + ";" + ticket
    }
    def msg(address: String, port: Int, ticket: String) = new Message(address, port, ticket)
  }

  object ServerSelectionError {
    private class Message extends Formattable {
      def render() = "AYE"
    }
    def msg = new Message
  }

  object VersionInfo {
    class Message extends Parsable {
      @BeanProperty
      var version: String = null

      def parse(input: String) {
        version = input
      }
    }
    val header = ""
    def msg = new Message
  }

  object Authentication {
    class Message extends Parsable {
      @BeanProperty
      var username: String = null

      @BeanProperty
      var password: String = null

      def parse(input: String) {
        val args = input.split("\n")
        username = args(0)
        password = args(1)
      }
    }
    val header = ""
    def msg = new Message
  }

  object ServersList {
    class Message extends Parsable {
      def parse(input: String) {}
    }
    val header = "Ax"
    def msg = new Message
  }

  object ServerSelection {
    class Message extends Parsable {
      @BeanProperty
      var serverId: Int = 0

      def parse(input: String) {
        serverId = Integer.parseInt(input)
      }
    }
    val header = "AX"
    def msg = new Message
  }

  object QueueInfo {
    class Message extends Parsable {
      def parse(input: String) {} // TODO
    }
    val header = "Af"
    def msg = new Message
  }
}
