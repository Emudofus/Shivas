package org.shivas.host;

import com.google.inject.AbstractModule;
import org.shivas.core.config.Config;
import org.shivas.core.config.DefaultConfig;
import org.shivas.core.modules.ShivasCommandModule;
import org.shivas.core.modules.ShivasDatabaseModule;
import org.shivas.core.modules.ShivasModInstallerModule;
import org.shivas.core.modules.ShivasServiceModule;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 30/10/12
 * Time: 13:30
 */
public class HostModule extends AbstractModule {
    @Override
    protected void configure() {
        Config config = new DefaultConfig();
        bind(Config.class).toInstance(config);

        install(new ShivasServiceModule());
        install(new ShivasDatabaseModule());
        install(new ShivasCommandModule());
        install(new ShivasModInstallerModule(config.modPath()));
    }
}
