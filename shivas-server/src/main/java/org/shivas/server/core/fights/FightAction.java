package org.shivas.server.core.fights;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 06/10/12
 * Time: 20:01
 */
public abstract class FightAction {
    private final SettableFuture<FightAction> endFuture = SettableFuture.create();

    protected void end() {
        endFuture.set(this);
    }

    public ListenableFuture<FightAction> getEndFuture() {
        return endFuture;
    }
}
