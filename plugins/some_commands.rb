import org.shivas.core.core.plugins.Plugin
import org.shivas.core.core.commands.Command
import org.shivas.common.params.Conditions
import java.lang.Runtime
import java.lang.System
import java.util.ArrayList

class SomeCommandsPlugin < Plugin
	def getCommands
		ArrayList.new [
			HelloWorldCommand.new,
			MemoryUsageCommand.new,
			GarbageCollectorCommand.new,
      RestoreLifeCommand.new
		]
	end
end

class HelloWorldCommand < Command

	def getName
    "hello"
  end
	def getHelp
    "Hello world"
  end

	def use(client, log, params)
		log.info "hello world"
		log.info Shivas.getConfig().dataPath()
	end
end

class MemoryUsageCommand < Command

	def getName
    "memory"
  end
	def getHelp
    "Show the used memory"
  end

	def canUse(client)
		client.account().hasRights()
	end

	def use(client, log, params)
		max = Runtime.runtime.totalMemory() / 1024 / 1024
		free = Runtime.runtime.freeMemory() / 1024 / 1024
		used = max - free

		log.info "max memory : %d Mo, free memory : %d Mo", max, free
		log.info "used memory : %d Mo", used
	end
end

class GarbageCollectorCommand < Command

	def getName
    "gc"
  end
	def getHelp
    "Cleans memory"
  end

	def canUse(client)
		client.account().hasRights()
	end

	def use(client, log, params)
		before = Runtime.runtime.freeMemory()
		System.runFinalization()
		System.gc()
		after = Runtime.runtime.freeMemory()

		delta = after - before

		if delta > 0
			log.info "you cleaned %d Ko", (delta / 1024)
		elsif delta < 0
			delta = -delta
			log.info "%d Ko has been allocated during the garbage collecting", (delta / 1024)
		else
			log.info "garbage collecting has no effect here"
		end
	end
end

class RestoreLifeCommand < Command

  def getName
    "life"
  end
  def getHelp
    "Restore your life"
  end

  def use(client, log, params)
    stats = client.player().stats

    delta = stats.life().setMax()

    client.write(stats.packet())
    log.info("You recovered %d life point", delta)
  end
end

SomeCommandsPlugin.new
