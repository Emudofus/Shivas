package org.shivas.core.config;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 08/11/12
 * Time: 20:25
 */
public class Group {
    static String combine(String left, String right) {
        return left + "." + right;
    }

    private final ConfigProvider config;
    private final String key;

    public Group(ConfigProvider config, String key) {
        this.config = config;
        this.key = key;
    }

    public Group group(String key) {
        return new Group(config, combine(this.key, key));
    }

    public Group configure(String key, Object value) {
        config.configure(combine(this.key, key), value);
        return this;
    }
}
