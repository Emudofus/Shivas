package org.shivas.core.core.castables.effects;

import org.shivas.common.random.Dice;
import org.shivas.common.random.Dofus1Dice;
import org.shivas.core.core.fights.Fight;
import org.shivas.core.core.fights.FightCell;
import org.shivas.core.core.fights.FightException;
import org.shivas.core.core.fights.Fighter;
import org.shivas.core.core.fights.events.FighterLifeUpdateEvent;
import org.shivas.core.core.fights.events.FighterSlideEvent;
import org.shivas.core.utils.Cells;
import org.shivas.data.entity.SpellLevel;
import org.shivas.protocol.client.enums.OrientationEnum;
import org.shivas.protocol.client.enums.SpellEffectTypeEnum;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 02/11/12
 * Time: 12:29
 */
public class PushEffect extends Effect {
    private static final Dice BASE_DAMAGE = new Dofus1Dice(1, 9, 8); // 1d9+8

    public static short computeDamage(Dice dice, int value, int step, int level){
        int _loc1 = dice.roll();
        double _loc2 = level / 50;
        if (_loc2 < 0.1) _loc2 = 0.1;
        return (short) (Math.floor(_loc1 * _loc2) * (value - step + 1));
    }

    public static short computeDamage(int value, int step, int level) {
        return computeDamage(BASE_DAMAGE, value, step, level);
    }

    private int value;

    public PushEffect(SpellLevel spellLevel) {
        super(spellLevel, SpellEffectTypeEnum.PushBack);
    }

    @Override
    public void setValue1(int value) {
        this.value = value;
    }

    @Override
    protected PushEffect emptyCopy() {
        return new PushEffect(spellLevel);
    }

    @Override
    public void apply(Fight fight, Fighter caster, FightCell targetCell) throws FightException {
        if (targetCell.getCurrentFighter() == null) return;

        Fighter target = targetCell.getCurrentFighter();

        OrientationEnum orientation = Cells.getOrientationByCells(caster.getCurrentCell(), target.getCurrentCell(), fight.getMap());

        int rounds;
        FightCell cell = target.getCurrentCell();
        for (rounds = 0; rounds < value; ++rounds) {
            short nextCellId = Cells.getCellIdByOrientation(cell.getId(), orientation, fight.getMap());
            FightCell nextCell = fight.getCell(nextCellId);

            if (nextCell == null || !nextCell.isWalkable() || !nextCell.isAvailable()) {
                break;
            }
            cell = nextCell;
        }

        target.setCurrentCell(cell);

        if (rounds < value) {
            int damage = computeDamage(value, rounds, target.getLevel());
            int delta = target.getStats().life().minus(damage);

            fight.getEvent().publish(
                    new FighterLifeUpdateEvent(caster, target, -delta),
                    new FighterSlideEvent(caster, target, cell)
            );
        } else {
            fight.getEvent().publish(new FighterSlideEvent(caster, target, cell));
        }
    }
}
