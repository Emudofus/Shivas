package org.shivas.common.threads;

import org.joda.time.Instant;
import org.joda.time.ReadableDuration;
import org.joda.time.ReadableInstant;
import org.shivas.common.collections.SortedArrayList;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 28/10/12
 * Time: 14:41
 */
public class Timer<G> implements Runnable {
    private final List<TimerTask<G>> tasks = new SortedArrayList<TimerTask<G>>();
    private final Thread thread;
    private final Object lock = new Object();

    private boolean running;

    public Timer(String id) {
        thread = new Thread(this);
        thread.setName("shivas-timer-" + id);
    }

    public boolean isRunning() {
        return running;
    }

    public void start() {
        running = true;
        thread.start();
    }

    public void stop() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void purge() {
        synchronized (lock) {
            tasks.clear();
        }
    }

    public void purge(G group) {
        synchronized (lock) {
            for (int i = 0; i < tasks.size(); ++i) {
                TimerTask<G> task = tasks.get(i);
                if (task.getGroup() == group) {
                    tasks.remove(i);
                }
            }
        }
    }

    public void schedule(G group, ReadableDuration duration, Runnable action) {
        schedule(group, Instant.now().plus(duration), action);
    }

    public void schedule(G group, ReadableInstant instant, Runnable action) {
        synchronized (lock) {
            if (instant.isAfter(Instant.now())) {
                tasks.add(new TimerTask<G>(group, instant, action));
            }
        }
    }

    private void waitForTask() {
        try {
            while (running && (tasks.isEmpty() || !tasks.get(0).isReady())) {
                Thread.sleep(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (running) {
            waitForTask();

            TimerTask<?> task;
            synchronized (lock) {
                task = tasks.remove(0);
            }
            task.getAction().run();
        }
    }
}
