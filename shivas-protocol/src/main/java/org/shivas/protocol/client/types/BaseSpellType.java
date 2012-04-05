package org.shivas.protocol.client.types;

/**
 * User: Blackrush
 * Date: 15/12/11
 * Time: 16:55
 * IDE : IntelliJ IDEA
 */
public class BaseSpellType {
    private int id;
    private short level;
    private String direction;

    public BaseSpellType() {

    }

    public BaseSpellType(int id, short level, String direction) {
        this.id = id;
        this.level = level;
        this.direction = direction;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public short getLevel() {
        return level;
    }

    public void setLevel(short level) {
        this.level = level;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
