package org.shivas.core.core.fights.events;

import org.shivas.core.core.fights.Fighter;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 17/10/12
 * Time: 22:58
 */
public class FighterEvent extends FightEvent {
    private final FightEventType fightEventType;
    private final Fighter fighter;

    public FighterEvent(FightEventType fightEventType, Fighter fighter) {
        this.fightEventType = fightEventType;
        this.fighter = fighter;
    }

    @Override
    public FightEventType getFightEventType() {
        return fightEventType;
    }

    public Fighter getFighter() {
        return fighter;
    }
}
