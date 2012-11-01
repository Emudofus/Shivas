package org.shivas.mods.example;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import org.shivas.core.core.commands.Command;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 31/10/12
 * Time: 21:23
 */
public class ExampleModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder<Command> multibinder = Multibinder.newSetBinder(binder(), Command.class);
        multibinder.addBinding().to(MyExampleCommand.class);
        multibinder.addBinding().to(ReportCommand.class);
        multibinder.addBinding().to(StaffCommand.class);
    }
}
