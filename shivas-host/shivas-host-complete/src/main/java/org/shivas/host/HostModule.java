package org.shivas.host;

import com.google.inject.AbstractModule;
import org.shivas.core.config.Config;
import org.shivas.core.config.DefaultConfig;
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
        bind(Config.class).to(DefaultConfig.class);

        install(new ShivasServiceModule());
    }
}
