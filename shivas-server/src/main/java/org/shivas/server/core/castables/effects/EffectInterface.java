package org.shivas.server.core.castables.effects;

import org.shivas.common.random.Dice;
import org.shivas.data.entity.SpellLevel;
import org.shivas.protocol.client.enums.SpellEffectTypeEnum;
import org.shivas.server.core.castables.zones.Zone;
import org.shivas.server.core.fights.Fight;
import org.shivas.server.core.fights.FightCell;
import org.shivas.server.core.fights.FightException;
import org.shivas.server.core.fights.Fighter;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 29/10/12
 * Time: 08:50
 */
public interface EffectInterface {
    SpellLevel getSpellLevel();
    SpellEffectTypeEnum getSpellEffect();

    void setValue1(int value1);
    void setValue2(int value2);
    void setValue3(int value3);
    void setChance(int chance);
    Dice getDice();
    void setDice(Dice dice);
    void setNbTurns(int nbTurns);
    void setZone(Zone zone);

    void apply(Fight fight, Fighter caster, FightCell target) throws FightException;

    EffectInterface copy();
}
