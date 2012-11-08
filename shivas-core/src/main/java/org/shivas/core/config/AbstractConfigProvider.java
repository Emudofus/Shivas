package org.shivas.core.config;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.joda.time.Duration;
import org.shivas.data.Containers;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 01/11/12
 * Time: 19:03
 */
public abstract class AbstractConfigProvider implements ConfigProvider {
    private Map<String, Object> values;

    protected AbstractConfigProvider() {
        values = Maps.newHashMap();
        doConfigure();
        values = ImmutableMap.copyOf(values);
    }

    protected abstract void doConfigure();

    @Override
    public void configure(String key, Object value) {
        values.put(key, value);
    }

    protected Group group(String key) {
        return new Group(this, key);
    }

    @Override
    public Object get(String key) {
        Object value = values.get(key);
        if (value == null) throw new UnknownKeyException(key);
        return value;
    }

    @Override
    public Object get(String key, Object defaultz) {
        Object value = values.get(key);
        return value != null ? value : defaultz;
    }

    @Override
    public boolean getBool(String key) {
        return (Boolean) get(key);
    }

    @Override
    public boolean getBool(String key, boolean defaultz) {
        Object value = get(key, null);
        if (value == null) return defaultz;
        return (Boolean) value;
    }

    @Override
    public short getShort(String key) {
        return ((Number) get(key)).shortValue();
    }

    @Override
    public short getShort(String key, int defaultz) {
        Object value = get(key, null);
        if (value == null) return (short) defaultz;
        return ((Number) value).shortValue();
    }

    @Override
    public int getInt(String key) {
        return ((Number) get(key)).intValue();
    }

    @Override
    public int getInt(String key, int defaultz) {
        Object value = get(key, null);
        if (value == null) return defaultz;
        return ((Number) value).intValue();
    }

    @Override
    public long getLong(String key) {
        return ((Number) get(key)).longValue();
    }

    @Override
    public long getLong(String key, long defaultz) {
        Object value = get(key, null);
        if (value == null) return defaultz;
        return ((Number) value).longValue();
    }

    @Override
    public String getString(String key) {
        return (String) get(key);
    }

    @Override
    public String getString(String key, String defaultz) {
        Object value = get(key, null);
        if (value == null) return defaultz;
        return (String) value;
    }

    @Override
    public Duration getDuration(String key) {
        return (Duration) get(key);
    }

    @Override
    public Duration getDuration(String key, Duration defaultz) {
        Object value = get(key, null);
        if (value == null) return defaultz;
        return (Duration) value;
    }

    @Override
    public <T, U> T getValue(String key, Class<T> clazz, U param) {
        Map<?, ?> map = (Map<?, ?>) get(key);
        Object value = map.get(param);
        return clazz.cast(value);
    }

    @Override
    public <T> T getData(String key, Class<T> clazz) {
        int id = getInt(key);
        return Containers.instance().get(clazz).byId(id);
    }
}
