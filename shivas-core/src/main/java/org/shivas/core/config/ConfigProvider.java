package org.shivas.core.config;

import org.joda.time.Duration;

public interface ConfigProvider {

    void configure(String key, Object value);

    Object get(String key);
    Object get(String key, Object defaultz);

    boolean getBool(String key);
    boolean getBool(String key, boolean defaultz);
    short getShort(String key);
    short getShort(String key, int defaultz);
    int getInt(String key);
    int getInt(String key, int defaultz);
    long getLong(String key);
    long getLong(String key, long defaultz);
    String getString(String key);
    String getString(String key, String defaultz);
    Duration getDuration(String key);
    Duration getDuration(String key, Duration defaultz);

    <T> T getData(String key, Class<T> clazz);
    <T, U> T getValue(String key, Class<T> clazz, U param);
}
