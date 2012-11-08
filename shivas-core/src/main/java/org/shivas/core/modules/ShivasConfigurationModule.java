package org.shivas.core.modules;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.MembersInjector;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import org.shivas.core.config.ConfigProvider;
import org.shivas.core.config.InjectConfig;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 08/11/12
 * Time: 22:12
 */
public class ShivasConfigurationModule extends AbstractModule {
    static class Pair<A, B> {
        public A first;
        public B second;

        private Pair(A first, B second) {
            this.first = first;
            this.second = second;
        }

        static <A, B> Pair<A, B> create(A first, B second) {
            return new Pair<A, B>(first, second);
        }
    }

    private final ConfigProvider config;

    public ShivasConfigurationModule(ConfigProvider config) {
        this.config = config;
    }

    @Override
    protected void configure() {
        bindListener(Matchers.any(), new TypeListener() {
            public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
                final List<Pair<Field, String>> fields = Lists.newArrayList();

                for (Field field : type.getRawType().getDeclaredFields()) {
                    InjectConfig annotation = field.getAnnotation(InjectConfig.class);
                    if (annotation == null) continue;

                    if (!field.isAccessible()) field.setAccessible(true);
                    fields.add(Pair.create(field, annotation.key()));
                }

                encounter.register(new MembersInjector<I>() {
                    public void injectMembers(I instance) {
                        for (Pair<Field, String> field : fields) {
                            Object value = config.get(field.second);
                            try {
                                field.first.set(instance, value);
                            } catch (IllegalAccessException e) {
                                addError(e);
                            }
                        }
                    }
                });
            }
        });
    }
}
