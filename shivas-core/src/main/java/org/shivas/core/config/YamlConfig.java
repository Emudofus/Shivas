package org.shivas.core.config;

import org.joda.time.Duration;
import org.shivas.protocol.client.enums.FightTypeEnum;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 01/11/12
 * Time: 19:02
 */
public class YamlConfig extends AbstractConfig {
    public YamlConfig() throws FileNotFoundException {
        load(new File("./config.yaml"));
    }

    private void load(File file) throws FileNotFoundException {
        Yaml yaml = new Yaml();
        Map<String, Object> params = (Map<String, Object>) yaml.load(new FileReader(file));

        clientVersion = (String) params.get("client_version");
        motd = (String) params.get("motd");

        database((Map<String, Object>) params.get("database"));
        data((Map<String, Object>) params.get("data"));
        mods((Map<String, Object>) params.get("mods"));
        login((Map<String, Object>) params.get("login"));
        game((Map<String, Object>) params.get("game"));
        commands((Map<String, Object>) params.get("commands"));
        world((Map<String, Object>) params.get("world"));
        start((Map<String, Object>) params.get("start"));
        loggers((Map<String, Object>) params.get("loggers"));
        fights((Map<String, Object>) params.get("fights"));
    }

    private void database(Map<String, Object> params) {
        databaseConnection = (String) params.get("connection");
        databaseUser = (String) params.get("user");
        databasePassword = (String) params.get("password");
        databaseFlushDelay = ((Integer) params.get("flush_delay")) * 1000;
        databaseSaveDelay = ((Integer) params.get("save_delay")) * 60;
        databaseRefreshRate = (Integer) params.get("refresh_rate");
    }

    private void data(Map<String, Object> params) {
        dataPath = (String) params.get("path");
        dataExtension = (String) params.get("extension");
    }

    private void mods(Map<String, Object> params) {
        modPath = (String) params.get("path");
    }

    private void login(Map<String, Object> params) {
        loginPort = (Integer) params.get("port");
    }

    private void game(Map<String, Object> params) {
        gameId = (Integer) params.get("id");
        gameAddress = (String) params.get("address");
        gamePort = (Integer) params.get("port");
    }

    private void commands(Map<String, Object> params) {
        cmdEnabled = (Boolean) params.get("enabled");
        cmdPrefix = (String) params.get("prefix");
    }

    private void world(Map<String, Object> params) {
        maxPlayersPerAccount = (Integer) params.get("max_players_per_account");
        deleteAnswerLevelNeeded = ((Integer) params.get("delete_answer_level_needed")).shortValue();
        maxSpellLevel = ((Integer) params.get("max_spell_level")).shortValue();
        addAllWaypoints = (Boolean) params.get("add_all_waypoints");
        npcBuyCoefficient = (Integer) params.get("npc_buy_coefficient");
    }

    private void start(Map<String, Object> params) {
        startSize = ((Integer) params.get("size")).shortValue();
        startLevel = ((Integer) params.get("level")).shortValue();
        startKamas = (Integer) params.get("kamas");
        startMapId = (Integer) params.get("map_id");
        startCell = ((Integer) params.get("cell_id")).shortValue();

        Map<String, Object> stats = (Map<String, Object>) params.get("stats");
        startActionPoints = ((Integer) stats.get("action_points")).shortValue();
        startMovementPoints = ((Integer) stats.get("movement_points")).shortValue();
        startVitality = stats.containsKey("vitality") ?
                ((Integer) stats.get("vitality")).shortValue() : 0;
        startWisdom = stats.containsKey("wisdom") ?
                ((Integer) stats.get("wisdom")).shortValue() : 0;
        startStrength = stats.containsKey("strength") ?
                ((Integer) stats.get("strength")).shortValue() : 0;
        startIntelligence = stats.containsKey("intelligence") ?
                ((Integer) stats.get("intelligence")).shortValue() : 0;
        startChance = stats.containsKey("chance") ?
                ((Integer) stats.get("chance")).shortValue() : 0;
        startAgility = stats.containsKey("agility") ?
                ((Integer) stats.get("agility")).shortValue() : 0;
    }

    private void loggers(Map<String, Object> params) {
        Map<String, Object> info = (Map<String, Object>) params.get("info"),
                            error = (Map<String, Object>) params.get("error"),
                            warn = (Map<String, Object>) params.get("warn");

        infoName = (String) info.get("name");
        infoColor = (String) info.get("color");

        errorName = (String) error.get("name");
        errorColor = (String) error.get("color");

        warnName = (String) warn.get("name");
        warnColor = (String) warn.get("color");
    }

    private void fights(Map<String, Object> params) {
        fightWorkersNum = (Integer) params.get("workers");

        turnDuration = new HashMap<FightTypeEnum, Duration>();
        Duration others = null;
        for (Map<String, Object> p : (List<Map<String, Object>>) params.get("turn_duration")) {
            String typeStr = (String) p.get("type");
            Duration duration = Duration.standardSeconds((Integer) p.get("seconds"));
            if (typeStr == null) {
                others = duration;
            } else {
                FightTypeEnum type = FightTypeEnum.valueOf(typeStr);
                turnDuration.put(type, duration);
            }
        }
        for (FightTypeEnum type : FightTypeEnum.values()) {
            if (!turnDuration.containsKey(type)) {
                turnDuration.put(type, others);
            }
        }
    }
}
