package org.shivas.protocol.client.enums;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 04/02/12
 * Time: 21:29
 * To change this template use File | Settings | File Templates.
 */
public enum GuildMemberRightsEnum {
    BOOST(2),
	RIGHTS(4),
	INVITE(8),
	BAN(16),
	RATES_XP_ALL(32),
	RANK(64),
	RATES_XP(256),
	POSPERCO(128),
	COLLPERCO(512),
	USEENCLOS(4096),
	AMENCLOS(8192),
	OTHDINDE(16384);

    private int value;
    GuildMemberRightsEnum(int value) {
        this.value = value;
    }
    public int value(){
        return value;
    }

    private static HashMap<Integer, GuildMemberRightsEnum> values = new HashMap<Integer, GuildMemberRightsEnum>();
    static{
        for (GuildMemberRightsEnum value : values()){
            values.put(value.value, value);
        }
    }
    public static GuildMemberRightsEnum valueOf(int value){
        return values.get(value);
    }
}
