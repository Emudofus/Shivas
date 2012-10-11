package org.shivas.data.entity;

import org.shivas.protocol.client.enums.Gender;
import org.shivas.protocol.client.enums.NpcTypeEnum;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 27/09/12
 * Time: 17:52
 */
public class NpcTemplate implements Serializable {
    private static final long serialVersionUID = 7294532367891387836L;

    private int id;
    private NpcTypeEnum type;
    private Gender gender;
    private short skin, size;
    private int color1, color2, color3;
    private ItemTemplate[] accessories;
    private int extraClip, customArtwork;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public NpcTypeEnum getType() {
        return type;
    }

    public void setType(NpcTypeEnum type) {
        this.type = type;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
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

    public ItemTemplate[] getAccessories() {
        return accessories;
    }

    public void setAccessories(ItemTemplate[] accessories) {
        this.accessories = accessories;
    }

    public ItemTemplate getAccessory(int index) {
        return accessories[index];
    }

    public int getExtraClip() {
        return extraClip;
    }

    public void setExtraClip(int extraClip) {
        this.extraClip = extraClip;
    }

    public int getCustomArtwork() {
        return customArtwork;
    }

    public void setCustomArtwork(int customArtwork) {
        this.customArtwork = customArtwork;
    }
}
