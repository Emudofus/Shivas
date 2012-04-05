package org.shivas.protocol.client.types;

import org.shivas.protocol.client.enums.OrientationEnum;

/**
 * User: Blackrush
 * Date: 13/11/11
 * Time: 12:41
 * IDE : IntelliJ IDEA
 */
public class RolePlayCharacterType extends BaseRolePlayActorType {
    private String name;
    private byte breedId;
    private short skin, size;
    private boolean gender;
    private short level;
    //todo alignment
    private int color1, color2, color3;
    private int[] accessories;
    private boolean hasGuild;
    private String guildName;
    private GuildEmblem guildEmblem;

    public RolePlayCharacterType() {
    }

    public RolePlayCharacterType(long id, String name, byte breedId, short skin, short size, boolean gender, short level, int color1, int color2, int color3, int[] accessories, short currentCellId, OrientationEnum currentOrientation, boolean hasGuild, String guildName, GuildEmblem guildEmblem) {
        super(id, currentCellId, currentOrientation);
        this.name = name;
        this.breedId = breedId;
        this.skin = skin;
        this.size = size;
        this.gender = gender;
        this.level = level;
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        this.accessories = accessories;
        this.hasGuild = hasGuild;
        this.guildName = guildName;
        this.guildEmblem = guildEmblem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getBreedId() {
        return breedId;
    }

    public void setBreedId(byte breedId) {
        this.breedId = breedId;
    }

    public short getSkin() {
        return skin;
    }

    public void setSkin(short skin) {
        this.skin = skin;
    }

    public short getSize() {
        return size;
    }

    public void setSize(short size) {
        this.size = size;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public short getLevel() {
        return level;
    }

    public void setLevel(short level) {
        this.level = level;
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

    public boolean hasGuild() {
        return hasGuild;
    }

    public void setHasGuild(boolean hasGuild) {
        this.hasGuild = hasGuild;
    }

    public String getGuildName() {
        return guildName;
    }

    public void setGuildName(String guildName) {
        this.guildName = guildName;
    }

    public GuildEmblem getGuildEmblem() {
        return guildEmblem;
    }

    public void setGuildEmblem(GuildEmblem guildEmblem) {
        this.guildEmblem = guildEmblem;
    }
}
