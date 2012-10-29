package org.shivas.server.core.castables;

import org.shivas.server.core.castables.effects.EffectInterface;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 06/10/12
 * Time: 19:43
 */
public interface Castable {
    Collection<EffectInterface> getEffects();
    Collection<EffectInterface> getCriticalEffects();
}
