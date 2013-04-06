package org.shivas.common.event;

/**
 * @author Blackrush
 */
public abstract class Event implements EventInterface {
    private boolean stopped;

    @Override
    public boolean hasBeenStopped() {
        return stopped;
    }

    @Override
    public void stopPropagation() {
        stopped = true;
    }

    @Override
    public void reply(Object message) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
