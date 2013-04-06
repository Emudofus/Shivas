package org.shivas.common.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Blackrush
 *         Mambo
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventHandler {
    public static enum Priority {
        GOD, // with great powers comes great responsibility
        VERY_HIGH,
        HIGH,
        NORMAL,
        LOW,
        VERY_LOW,
        MONITOR
    }

    Priority priority() default Priority.NORMAL;
}
