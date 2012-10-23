package org.shivas.server.core.fights.frames;

import org.shivas.server.core.fights.Castable;
import org.shivas.server.core.fights.FightCell;
import org.shivas.server.core.fights.FightException;
import org.shivas.server.core.fights.Fighter;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 23/10/12
 * Time: 09:18
 */
public class FighterCastFrame extends Frame {
    private final Fighter caster;
    private final Castable castable;
    private final FightCell targetCell;

    public FighterCastFrame(Fighter caster, Castable castable, FightCell targetCell) {
        super(caster.getFight(), caster.getTurn());
        this.caster = caster;
        this.castable = castable;
        this.targetCell = targetCell;
    }

    @Override
    protected void doBegin() throws FightException {
        // TODO
    }

    @Override
    protected void doEnd() {
        // TODO
    }
}
