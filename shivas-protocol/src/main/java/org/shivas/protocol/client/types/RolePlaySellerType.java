package org.shivas.protocol.client.types;

import org.shivas.protocol.client.enums.OrientationEnum;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 25/02/12
 * Time: 18:50
 */
public class RolePlaySellerType extends BaseRolePlayActorType {
    private String name;
    private short skin, size;
    private int color1, color2, color3;
    private int[] accessories;
    private boolean hasGuild;
    private String guildName;
    private GuildEmblem guildEmblem;

    public RolePlaySellerType() {
    }

    public RolePlaySellerType(long id, short currentCellId, OrientationEnum currentOrientation, String name, short skin, short size, int color1, int color2, int color3, int[] accessories, boolean hasGuild, String guildName, GuildEmblem guildEmblem) {
        super(id, currentCellId, currentOrientation);
        this.name = name;
        this.skin = skin;
        this.size = size;
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
