package org.shivas.server.core.exchanges;

import org.shivas.server.core.interactions.Interaction;
import org.shivas.server.core.interactions.InteractionException;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 02/10/12
 * Time: 23:56
 */
public interface Salable extends Interaction {
    void sell(long itemId, int quantity) throws InteractionException;
}
