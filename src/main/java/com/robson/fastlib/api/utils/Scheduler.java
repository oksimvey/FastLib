package com.robson.fastlib.api.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Scheduler {

    private static final ScheduledExecutorService SCHEDULER = Executors.newScheduledThreadPool(1);

    public static void schedule(Runnable task, long delay, TimeUnit unit) {
        SCHEDULER.schedule(task, delay, unit);
    }

}
