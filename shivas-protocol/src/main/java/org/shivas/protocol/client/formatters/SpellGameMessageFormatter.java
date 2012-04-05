package org.shivas.protocol.client.formatters;

import org.shivas.protocol.client.types.BaseSpellType;

import java.util.Collection;

/**
 * User: Blackrush
 * Date: 06/11/11
 * Time: 10:09
 * IDE:  IntelliJ IDEA
 */
public class SpellGameMessageFormatter {
    public static String spellListMessage(Collection<BaseSpellType> spells){
        StringBuilder sb = new StringBuilder(2 + 4 * spells.size()).append("SL");

        boolean first = true;
        for (BaseSpellType spell : spells){
            if (first) first = false;
            else sb.append(';');

            sb.append(spell.getId()).append('~');
            sb.append(spell.getLevel()).append('~');
            sb.append(spell.getDirection());
        }

        return sb.toString();
    }

    public static Object boostSpellErrorMessage() {
        return "SUE";
    }

    public static String boostSpellSuccessMessage(long spellId, short spellLevel) {
        return "SUK" + spellId + "~" + spellLevel;
    }
}
