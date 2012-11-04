package org.shivas.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import org.shivas.core.core.commands.*;

import javax.inject.Singleton;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 30/10/12
 * Time: 10:54
 */
public class ShivasCommandModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder<Command> b = Multibinder.newSetBinder(binder(), Command.class);

        b.addBinding().to(AverageHandleCommand.class).in(Singleton.class);
        b.addBinding().to(CreateGuildCommand.class).in(Singleton.class);
        b.addBinding().to(GiveItemCommand.class).in(Singleton.class);
        b.addBinding().to(KickCommand.class).in(Singleton.class);
        b.addBinding().to(SaveCommand.class).in(Singleton.class);
        b.addBinding().to(PrintCommand.class).in(Singleton.class);
    }
}
