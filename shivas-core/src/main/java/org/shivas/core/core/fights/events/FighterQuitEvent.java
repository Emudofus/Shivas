package org.shivas.core.core.fights.events;

import org.shivas.core.core.fights.Fighter;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 01/11/12
 * Time: 12:44
 */
public class FighterQuitEvent extends FighterEvent {
    public FighterQuitEvent(Fighter fighter) {
        super(FightEventType.FIGHTER_QUIT, fighter);
    }
}
