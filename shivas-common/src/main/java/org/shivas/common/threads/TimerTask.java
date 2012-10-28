package org.shivas.common.threads;

import org.joda.time.ReadableInstant;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 28/10/12
 * Time: 14:41
 */
public class TimerTask<G> implements Comparable<TimerTask<G>> {

    private final G group;
    private final ReadableInstant scheduled;
    private final Runnable action;

    public TimerTask(G group, ReadableInstant scheduled, Runnable action) {
        this.group = group;
        this.scheduled = scheduled;
        this.action = action;
    }

    public G getGroup() {
        return group;
    }

    public ReadableInstant getScheduled() {
        return scheduled;
    }

    public boolean isReady() {
        return scheduled.getMillis() <= System.currentTimeMillis();
    }

    public Runnable getAction() {
        return action;
    }

    @Override
    public int compareTo(TimerTask<G> o) {
        return scheduled.compareTo(o.scheduled);
    }
}
