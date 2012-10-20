package org.shivas.protocol.client.enums;

public enum FightTeamEnum {
	CHALLENGERS,
	DEFENDERS,
	SPECTATORS;

    public FightTeamEnum opposite() {
        return this == CHALLENGERS ? DEFENDERS :
               this == DEFENDERS   ? CHALLENGERS :
               null;
    }
}
