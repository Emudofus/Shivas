package org.shivas.core.core.castables.zones;

import org.shivas.common.StringUtils;
import org.shivas.protocol.client.enums.ZoneTypeEnum;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 29/10/12
 * Time: 08:44
 */
public final class Zones {
    private Zones() {}

    public static Zone parseZone(String string) {
        if (string.length() != 2) {
            throw new IllegalArgumentException("string must have a length of 2");
        }

        int length = StringUtils.EXTENDED_ALPHABET.indexOf(string.charAt(1));

        switch (ZoneTypeEnum.valueOf(string.charAt(0))) {
        case CROSS:
            return new CrossZone(length);

        case LINE:
            return new LineZone(length);

        case CIRCLE:
            return new CircleZone(length);

        default:
            return SingleCellZone.INSTANCE;
        }
    }
}
