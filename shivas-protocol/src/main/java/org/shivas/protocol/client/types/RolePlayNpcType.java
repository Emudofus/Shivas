package org.shivas.protocol.client.types;

import org.shivas.protocol.client.enums.Gender;
import org.shivas.protocol.client.enums.OrientationEnum;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 26/01/12
 * Time: 18:09
 */
public class RolePlayNpcType extends BaseRolePlayActorType {
    private int templateId;
    private short scaleX, scaleY;
    private short skin;
    private Gender gender;
    private int color1, color2, color3;
    private int[] accessories;
    private int customArtwork;
    private int extraClip;

    public RolePlayNpcType() {
    }

    public RolePlayNpcType(long id, int templateId, short scaleX, short scaleY, short skin, Gender gender, int color1, int color2, int color3, short cellId, OrientationEnum orientation, int[] accessories, int customArtwork, int extraClip) {
        super(id, cellId, orientation);
        this.templateId = templateId;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.skin = skin;
        this.gender = gender;
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        this.accessories = accessories;
        this.customArtwork = customArtwork;
        this.extraClip = extraClip;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public short getScaleX() {
        return scaleX;
    }

    public void setScaleX(short scaleX) {
        this.scaleX = scaleX;
    }

    public short getScaleY() {
        return scaleY;
    }

    public void setScaleY(short scaleY) {
        this.scaleY = scaleY;
    }

    public short getSkin() {
        return skin;
    }

    public void setSkin(short skin) {
        this.skin = skin;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
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

    public int getCustomArtwork() {
        return customArtwork;
    }

    public void setCustomArtwork(int customArtwork) {
        this.customArtwork = customArtwork;
    }

    public int getExtraClip() {
        return extraClip;
    }

    public void setExtraClip(int extraClip) {
        this.extraClip = extraClip;
    }

    public boolean sameScale() {
        return scaleX == scaleY;
    }

    public int getSize(){
        return scaleX;
    }
}
