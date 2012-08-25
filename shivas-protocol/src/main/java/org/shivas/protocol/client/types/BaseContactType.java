package org.shivas.protocol.client.types;

import org.shivas.protocol.client.enums.ContactStateEnum;
import org.shivas.protocol.client.enums.Gender;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 31/12/11
 * Time: 18:51
 */
public class BaseContactType {
    private String nickname;
    private String name;
    private short level;
    private short alignmentId;
    private byte breedId;
    private Gender gender;
    private short skin;
    private boolean connected, reciprocal;
    private ContactStateEnum state;

    public BaseContactType() {
    }

    public BaseContactType(String nickname, boolean connected, boolean reciprocal, String name, short level, short alignmentId, byte breedId, Gender gender, short skin, ContactStateEnum state) {
        this.nickname = nickname;
        this.name = name;
        this.level = level;
        this.alignmentId = alignmentId;
        this.breedId = breedId;
        this.gender = gender;
        this.skin = skin;
        this.connected = connected;
        this.reciprocal = reciprocal;
        this.state = state;
    }

    public BaseContactType(String nickname, boolean connected, boolean reciprocal, ContactStateEnum state) {
        this.nickname = nickname;
        this.connected = connected;
        this.reciprocal = reciprocal;
        this.state = state;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public short getAlignmentId() {
        return alignmentId;
    }

    public void setAlignmentId(short alignmentId) {
        this.alignmentId = alignmentId;
    }

    public byte getBreedId() {
        return breedId;
    }

    public void setBreedId(byte breedId) {
        this.breedId = breedId;
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

    public boolean isReciprocal() {
        return reciprocal;
    }

    public void setReciprocal(boolean reciprocal) {
        this.reciprocal = reciprocal;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

	public ContactStateEnum getState() {
		return state;
	}

	public void setState(ContactStateEnum state) {
		this.state = state;
	}
}
