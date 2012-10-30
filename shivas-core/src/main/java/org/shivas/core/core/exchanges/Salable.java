package org.shivas.core.core.exchanges;

import org.shivas.core.core.interactions.Interaction;
import org.shivas.core.core.interactions.InteractionException;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 02/10/12
 * Time: 23:56
 */
public interface Salable extends Interaction {
    void sell(long itemId, int quantity) throws InteractionException;
}
