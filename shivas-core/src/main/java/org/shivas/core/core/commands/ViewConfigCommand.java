package org.shivas.core.core.commands;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import org.shivas.common.params.Conditions;
import org.shivas.common.params.Parameters;
import org.shivas.common.params.Types;
import org.shivas.core.core.logging.DofusLogger;
import org.shivas.core.services.game.GameClient;

import javax.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 08/11/12
 * Time: 22:55
 */
public class ViewConfigCommand extends Command {
    @Inject Config config;

    @Override
    public String getName() {
        return "config";
    }

    @Override
    public String getHelp() {
        return "Show you the config";
    }

    @Override
    public Conditions getConditions() {
        return new Conditions() {{
            add("key", Types.STRING, "config's key");
        }};
    }

    @Override
    public boolean canUse(GameClient client) {
        return client.account().hasRights();
    }

    @Override
    public void use(GameClient client, DofusLogger log, Parameters params) {
        String key = params.get("key", String.class);
        try {
            String value = config.getValue(key).render();
            log.log("%s => %s", key, value);
        } catch (ConfigException.Missing e) {
            log.error("unknown key %s", key);
        }
    }
}
