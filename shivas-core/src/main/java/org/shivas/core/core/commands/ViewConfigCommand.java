package org.shivas.core.core.commands;

import org.shivas.common.params.Conditions;
import org.shivas.common.params.Parameters;
import org.shivas.common.params.Types;
import org.shivas.core.config.ConfigProvider;
import org.shivas.core.config.UnknownKeyException;
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
    @Inject ConfigProvider config;

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
            Object value = config.get(key);
            log.log("%s => %s", key, value.toString());
        } catch (UnknownKeyException e) {
            log.error("unknown key %s", key);
        }
    }
}
