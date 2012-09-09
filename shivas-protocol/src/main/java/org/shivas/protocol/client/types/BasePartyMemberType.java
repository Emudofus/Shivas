package org.shivas.protocol.client.types;

/**
 * User: Blackrush
 * Date: 22/12/11
 * Time: 21:11
 * IDE : IntelliJ IDEA
 */
public class BasePartyMemberType {
    private long id;
    private String name;
    private short skin;
    private int color1, color2, color3;
    private int[] items;
    private short life, maxLife;
    private short level;
    private short initiative;
    private short prospection;
    private short side;

    public BasePartyMemberType() {

    }

    public BasePartyMemberType(long id, String name, short skin, int color1, int color2, int color3, int[] items, short life, short maxLife, short level, short initiative, short prospection, short side) {
        this.id = id;
        this.name = name;
        this.skin = skin;
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        this.items = items;
        this.life = life;
        this.maxLife = maxLife;
        this.level = level;
        this.initiative = initiative;
        this.prospection = prospection;
        this.side = side;
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

    public int[] getItems() {
        return items;
    }

    public void setItems(int[] items) {
        this.items = items;
    }

    public short getLife() {
        return life;
    }

    public void setLife(short life) {
        this.life = life;
    }

    public short getMaxLife() {
		return maxLife;
	}

	public void setMaxLife(short maxLife) {
		this.maxLife = maxLife;
	}

	public short getLevel() {
        return level;
    }

    public void setLevel(short level) {
        this.level = level;
    }

    public short getInitiative() {
        return initiative;
    }

    public void setInitiative(short initiative) {
        this.initiative = initiative;
    }

    public short getProspection() {
        return prospection;
    }

    public void setProspection(short prospection) {
        this.prospection = prospection;
    }

	public short getSide() {
		return side;
	}

	public void setSide(short side) {
		this.side = side;
	}
}
