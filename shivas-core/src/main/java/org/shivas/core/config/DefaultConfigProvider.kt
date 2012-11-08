package org.shivas.core.config

import org.joda.time.Duration
import org.shivas.common.statistics.CharacteristicType
import org.shivas.protocol.client.enums.FightTypeEnum
import java.util.Map
import java.util.HashMap

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 08/11/12
 * Time: 21:29
 */

inline fun ConfigProvider.group(key: String, handler: Group.() -> Unit) = Group(this, key).handler()
inline fun Group.group(key: String, handler: Group.() -> Unit) = group(key)?.handler()

inline fun Int.seconds(): Duration = Duration.standardSeconds(toLong())!!
inline fun Int.minutes(): Duration = Duration.standardMinutes(toLong())!!
inline fun Int.millions(): Int = this * 1000000

fun <K, V> Array<K>.asMap(closure: (K) -> V): HashMap<K, V> {
    val result = HashMap<K, V>()
    for (key in this) {
        val value = closure(key)
        result.put(key, value)
    }
    return result
}

public class DefaultConfigProvider(): AbstractConfigProvider() {
    protected override fun doConfigure() {
        val version = "DEV"

        group("database") {
            configure("datasource", "jdbc:mysql://localhost/shivas")
            configure("user", "root")
            configure("password", "")

            group("options") {
                configure("flush_delay", 30.seconds())
                configure("save_delay", 5.minutes())
                configure("refresh_rate", 1.minutes())
            }
        }

        group("data") {
            configure("path", "./data/")
            configure("extension", "xml")
        }

        group("mods") {
            configure("path", "./mods/")
        }

        group("commands") {
            configure("enabled", true)
            configure("prefix", "!")
        }

        group("login") {
            configure("port", 5555)
        }

        group("game") {
            configure("id", 1)
            configure("address", "127.0.0.1")
            configure("port", 5556)
        }

        group("world") {
            configure("motd", "Bienvenue sur Shivas V.${version} !")
            configure("client_version", "1.29.1")
            configure("max_players_per_account", 5)
            configure("delete_answer_level_needed", 10)
            configure("add_all_waypoints", true)
            configure("npc_buy_coefficient", 10)

            group("start") {
                configure("map", 7411)
                configure("cell", 255)
                configure("kamas", 1.millions())
                configure("level", 200)
                configure("size", 100)

                configure("action_points", 12)
                configure("movement_points", 6)

                array("vitality", "wisdom", "strength", "intelligence", "chance", "agility").forEach {
                    configure(it, 101)
                }
            }
        }

        group("loggers") {
            // see DofusLogger
        }

        group("fights") {
            configure("turn_duration", FightTypeEnum.values().asMap<FightTypeEnum, Duration> {
                when (it) {
                    FightTypeEnum.AGRESSION -> 51.seconds()
                    FightTypeEnum.DUEL -> 46.seconds()
                    else -> 31.seconds()
                }
            })
        }
    }
}
