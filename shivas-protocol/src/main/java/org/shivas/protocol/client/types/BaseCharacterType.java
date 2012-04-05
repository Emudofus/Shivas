package org.shivas.protocol.client.types;

/**
 * User: Blackrush
 * Date: 02/11/11
 * Time: 10:23
 * IDE : IntelliJ IDEA
 */
public class BaseCharacterType {
    private long id;
    private String name;
    private short level;
    private short skin;
    private int color1, color2, color3;
    private int[] accessories;
    private boolean storeActive;

    public BaseCharacterType() {

    }

    public BaseCharacterType(long id, String name, short level, short skin, int color1, int color2, int color3, int[] accessories, boolean storeActive) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.skin = skin;
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        this.accessories = accessories;
        this.storeActive = storeActive;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getLevel() {
        return level;
    }

    public void setLevel(short level) {
        this.level = level;
    }

    public short getSkin() {
        return skin;
    }

    public void setSkin(short skin) {
        this.skin = skin;
    }

    public int getColor1() {
        return color1;
    }

    public void setColor1(int color1) {
        this.color1 = color1;
    }

    public int getColor2() {
        return color2;
    }

    public void setColor2(int color2) {
        this.color2 = color2;
    }

    public int getColor3() {
        return color3;
    }

    public void setColor3(int color3) {
        this.color3 = color3;
    }

    public int[] getAccessories() {
        return accessories;
    }

    public void setAccessories(int[] accessories) {
        this.accessories = accessories;
    }

    public boolean isStoreActive() {
        return storeActive;
    }

    public void setStoreActive(boolean storeActive) {
        this.storeActive = storeActive;
    }
}
