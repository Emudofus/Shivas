package org.shivas.core.core.fights.events;

import org.shivas.protocol.client.enums.ActionTypeEnum;
import org.shivas.core.core.fights.Fighter;
import org.shivas.core.core.paths.Path;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 21/10/12
 * Time: 14:59
 */
public class FighterMovementEvent extends FighterActionEvent {
    private final Path path;

    public FighterMovementEvent(Fighter fighter, Path path) {
        super(fighter, ActionTypeEnum.MOVEMENT);
        this.path = path;
    }

    public Path getPath() {
        return path;
    }
}
