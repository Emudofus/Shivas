package org.shivas.common.event;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.reflect.TypeToken;
import org.shivas.common.collections.SortedList;
import org.shivas.common.threads.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @author Blackrush
 *         Mambo
 */
public class ReflectiveEventHandlerManager extends EventHandlerManager {
    private static final Logger log = LoggerFactory.getLogger(ReflectiveEventHandlerManager.class);

    private final Map<TypeToken<?>, List<Holder>> holders = Maps.newHashMap();

    protected void put(TypeToken<?> messageClass, Holder holder) {
        List<Holder> holders = this.holders.get(messageClass);
        if (holders == null) {
            holders = SortedList.create();
            this.holders.put(messageClass, holders);
        }
        holders.add(holder);
    }

    @Override
    public void onRegistered(Class<?> handlerClass) {
        for (Method method : handlerClass.getMethods()) {
            EventHandler annotation = method.getAnnotation(EventHandler.class);
            if (annotation == null) break;

            Type[] types = method.getGenericParameterTypes();
            if (types.length != 1) {
                throw new RuntimeException("an EventHandler must take only one parameter Event<M>");
            }
            Type type = types[0];

            method.setAccessible(true);

            put(TypeToken.of(type), new Holder(method, annotation.priority()));
        }
    }

    @Override
    public void onUnregistered(Class<?> handlerClass) {

    }

    @Override
    public void dispatch(Multimap<Class<?>, Object> handlers, EventInterface event, Future<Object> future) {
        List<Holder> holders = this.holders.get(TypeToken.of(event.getClass()));
        if (holders == null) return;

        for (Holder holder : holders) {
            for (Object handler : handlers.get(holder.method.getDeclaringClass())) {
                if (event.hasBeenStopped()) return;

                try {
                    Object reply = holder.method.invoke(handler, event);
                    if (reply != null) {
                        future.set(reply);
                    }
                } catch (IllegalAccessException e) {
                    log.error("can't dispatch event", e);
                } catch (InvocationTargetException e) {
                    future.set(e.getTargetException());
                } catch (Exception e) {
                    future.set(e);
                }
            }
        }
    }

    private static class Holder implements Comparable<Holder> {
        private final Method method;
        private final EventHandler.Priority priority;

        private Holder(Method method, EventHandler.Priority priority) {
            this.method = method;
            this.priority = priority;
        }

        @Override
        public int compareTo(Holder o) {
            return priority.ordinal() - o.priority.ordinal();
        }
    }
}
