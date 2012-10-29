package org.shivas.server.core.castables;

import org.shivas.common.maths.Range;
import org.shivas.server.core.castables.effects.EffectInterface;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 06/10/12
 * Time: 19:43
 */
public interface Castable {
    short getCost();
    short getCriticalRate();
    short getFailureRate();

    Range getRange();

    Collection<EffectInterface> getEffects(boolean critical);
}
