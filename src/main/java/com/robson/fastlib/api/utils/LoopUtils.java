package com.robson.fastlib.api.utils;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.IntConsumer;

public interface LoopUtils {

    static void loopByTimes(IntConsumer action, int maxLoops, int intervalMillis) {
        Timer timer = new Timer();
        final int[] currentLoop = {0};

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (currentLoop[0] >= maxLoops) {
                    timer.cancel();
                    return;
                }

                action.accept(currentLoop[0]);
                currentLoop[0]++;
            }
        }, 0, intervalMillis);
    }

    static void loopByConditional(LoopAction action, int intervalMillis) {
        Timer timer = new Timer();
        final int[] current = {0};
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                boolean shouldcontinue = action.run(current[0]);
                current[0]++;
                if (!shouldcontinue) {
                    timer.cancel();
                }
            }
        }, 0, intervalMillis);
    }

    @FunctionalInterface
    interface LoopAction {
        boolean run(int currentLoop);
    }
}
