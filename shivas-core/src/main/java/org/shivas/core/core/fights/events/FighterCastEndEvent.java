package org.shivas.core.core.fights.events;

import org.shivas.core.core.castables.Castable;
import org.shivas.core.core.fights.Fighter;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 29/10/12
 * Time: 18:31
 */
public class FighterCastEndEvent extends FighterEvent {
    private final Castable castable;

    public FighterCastEndEvent(Fighter fighter, Castable castable) {
        super(FightEventType.FIGHTER_CAST_END, fighter);
        this.castable = castable;
    }

    public Castable getCastable() {
        return castable;
    }
}
