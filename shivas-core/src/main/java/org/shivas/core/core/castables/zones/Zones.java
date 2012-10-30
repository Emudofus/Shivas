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

        Zone zone = null;

        switch (ZoneTypeEnum.valueOf(string.charAt(0))) {
        case SINGLE_CELL:
            zone = SingleCellZone.INSTANCE;
            break;

        case CROSS:
            zone = new CrossZone();
            break;

        case LINE:
            zone = new LineZone();
            break;

        case CIRCLE:
            zone = new CircleZone();
            break;
        }

        if (zone == null) {
            throw new IllegalArgumentException("invalid zone type \"" + string.charAt(0) + "\"");
        }

        zone.setLength(StringUtils.EXTENDED_ALPHABET.indexOf(string.charAt(1)));

        return zone;
    }
}
