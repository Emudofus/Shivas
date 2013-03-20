package org.shivas.host;

import com.google.inject.AbstractModule;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.shivas.core.modules.ShivasCommandModule;
import org.shivas.core.modules.ShivasDatabaseModule;
import org.shivas.core.modules.ShivasModInstallerModule;
import org.shivas.core.modules.ShivasServiceModule;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 30/10/12
 * Time: 13:30
 */
public class HostModule extends AbstractModule {
    @Override
    protected void configure() {
        try {
            Config config = ConfigFactory.parseFileAnySyntax(new File("./config"));
            bind(Config.class).toInstance(config);

            install(new ShivasServiceModule());
            install(new ShivasDatabaseModule());
            install(new ShivasCommandModule());
            install(new ShivasModInstallerModule(config.getString("mods.path")));
        } catch (Throwable cause) {
            addError(cause);
        }
    }
}
