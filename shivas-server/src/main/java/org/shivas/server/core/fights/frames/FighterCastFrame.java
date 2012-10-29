package org.shivas.server.core.fights.frames;

import org.shivas.server.core.castables.Castable;
import org.shivas.server.core.castables.effects.EffectInterface;
import org.shivas.server.core.fights.FightCell;
import org.shivas.server.core.fights.FightException;
import org.shivas.server.core.fights.Fighter;
import org.shivas.server.core.fights.events.FighterCastEndEvent;
import org.shivas.server.core.fights.events.FighterCastEvent;
import org.shivas.server.utils.Cells;

import static org.shivas.common.statistics.CharacteristicType.*;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 23/10/12
 * Time: 09:18
 */
public class FighterCastFrame extends Frame {
    private final Castable castable;
    private final FightCell targetCell;

    public FighterCastFrame(Fighter caster, Castable castable, FightCell targetCell) {
        super(caster.getTurn());
        this.castable = castable;
        this.targetCell = targetCell;
    }

    private boolean computeFailure() {
        if (castable.getFailureRate() <= 0) return false;

        int criticalFailureRate = castable.getFailureRate() + fighter.getStats().get(CriticalFailure).total();
        if (criticalFailureRate < 2){
            criticalFailureRate = 2;
        }
        return fight.getRandom().nextInt(criticalFailureRate) == 1;
    }

    private boolean computeCritical() {
        if (castable.getCriticalRate() <= 0) return false;

        short agility = fighter.getStats().get(Agility).safeTotal();
        int criticalRate = castable.getCriticalRate() + fighter.getStats().get(CriticalHit).safeTotal();
        criticalRate = (short)((criticalRate * 2.9901) / Math.log(agility + 12));

        if (criticalRate < 2){
            criticalRate = 2;
        }
        return fight.getRandom().nextInt(criticalRate) == 1;
    }

    @Override
    public void begin() throws FightException {
        if (fighter.getStats().get(ActionPoints).safeTotal() < castable.getCost()) {
            throw new FightException("you have not enough action points");
        }

        int distance = Cells.distanceBetween(fighter.getCurrentCell(), targetCell, fight.getMap());
        if (!castable.getRange().contains(distance)) {
            throw new FightException("the target is too far or too");
        }

        boolean failure = computeFailure(),
                critical = !failure && computeCritical();

        fight.getEvent().publish(new FighterCastEvent(fighter, castable, targetCell, critical, failure));

        if (!failure) {
            for (EffectInterface effect : castable.getEffects(critical)) {
                for (FightCell cell : effect.getZone().filter(fighter.getCurrentCell(), targetCell, fight.getCells(), fight.getMap())) {
                    effect.apply(fight, fighter, cell);
                }
            }
        }

        fighter.getStats().get(ActionPoints).minusContext(castable.getCost());

        end();
    }

    @Override
    protected void doEnd() {
        fight.getEvent().publish(new FighterCastEndEvent(fighter, castable));
    }
}
