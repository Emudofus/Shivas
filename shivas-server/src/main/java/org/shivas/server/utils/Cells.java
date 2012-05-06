package org.shivas.server.utils;

import org.shivas.common.StringUtils;
import org.shivas.data.entity.GameCell;
import org.shivas.protocol.client.enums.OrientationEnum;

public class Cells {
	private Cells() {}
	
	public static String encode(short cellid) {
        return Character.toString(StringUtils.EXTENDED_ALPHABET.charAt(cellid / 64)) +
                Character.toString(StringUtils.EXTENDED_ALPHABET.charAt(cellid % 64));
	}
	
	public static String encode(GameCell cell) {
		return encode(cell.getId());
	}
	
	public static short decode(String string) {
        return (short) (StringUtils.EXTENDED_ALPHABET.indexOf(string.charAt(0)) * 64 +
                StringUtils.EXTENDED_ALPHABET.indexOf(string.charAt(1)));
	}
	
	public static String encode(OrientationEnum orientation) {
		return String.valueOf(StringUtils.EXTENDED_ALPHABET.charAt(orientation.ordinal()));
	}

    public static OrientationEnum decode(char orientationCode){
        return OrientationEnum.valueOf(StringUtils.EXTENDED_ALPHABET.indexOf(orientationCode));
    }
}
