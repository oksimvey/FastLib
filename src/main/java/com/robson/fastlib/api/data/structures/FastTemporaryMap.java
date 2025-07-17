package com.robson.fastlib.api.data.structures;

import com.robson.fastlib.api.utils.math.FastLibMathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FastTemporaryMap<A, B> {

    private static final ScheduledExecutorService THREADER = Executors.newScheduledThreadPool(1);

    private static final List<FastTemporaryMap<?, ?>> ALL = new ArrayList<>();

    private static final int SIZE_DIVISOR = (int) (FastLibMathUtils.EULER * 100f);

    protected int SIZE;

    protected int KEY_SIZE;

    protected final ConcurrentHashMap<A, FastDataParameter<B>> DATA;

    protected final FastDataParameter.DataType VALUE_TYPE;

    private volatile boolean threadRunning = false;

    public static void clearAll() {
        for (FastTemporaryMap<?, ?> data : ALL) {
            data.clearMap();
        }
    }

    public FastTemporaryMap(FastDataParameter.DataType type) {
        this.VALUE_TYPE = type;
        this.SIZE = 1;
        this.KEY_SIZE = 1;
        this.DATA = new ConcurrentHashMap<>();
        ALL.add(this);
        this.threadMap();
    }

    public B get(A key){
        FastDataParameter<B> data = getParameter(key);
        if (data != null) {
            return data.getData();
        }
        return null;
    }

    protected FastDataParameter<B> getParameter(A key){
        if (key == null) return null;
        return DATA.get(key);
    }

    public final void put(A key, B value) {
        if (value == null) {
            return;
        }
        FastDataParameter<B> data = new FastDataParameter<>(value, VALUE_TYPE);
        this.SIZE += calculateSizeIncrement(data);
        this.KEY_SIZE++;
        putOnMap(key, data);
        if (!threadRunning) {
            threadMap();
        }
    }

    public final boolean contains(A key){
        return get(key) != null;
    }

    protected void clearMap(){
        DATA.clear();
    }

    protected void resetAccess(A key){
        DATA.get(key).resetAccesses();
    }

    protected final void putOnMap(A key, FastDataParameter<B> data){
        DATA.put(key, data);
    }

    protected void threadMap() {
        if (DATA.isEmpty()) {
            threadRunning = false;
            return;
        }

        threadRunning = true;
        THREADER.schedule(this::threadMap, calculateCleanTime(), TimeUnit.MILLISECONDS);
        if (Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.sendSystemMessage(Component.literal("ticking"));
        }
        for (Map.Entry<A, FastDataParameter<B>> entry : DATA.entrySet()) {
            A key = entry.getKey();
            FastDataParameter<B> data = entry.getValue();
            if (data.accesses == 0) {
                removeForAllocation(key, data.getData());
                this.KEY_SIZE--;
                this.SIZE -= calculateSizeIncrement(data);
                continue;
            }
            if (System.currentTimeMillis() - data.lastUpdate > calculateExpirationTime(data)) {
                resetAccess(key);
            }
        }
    }

    private int calculateExpirationTime(FastDataParameter<B> current) {
        return (int) ((current.size * Math.sqrt((current.accesses + 1))) / SIZE);
    }

    private int calculateSizeIncrement(FastDataParameter<B> data) {
        return data.size / SIZE_DIVISOR;
    }

    protected int calculateCleanTime(){
        return (int) (1000 * Math.sqrt(KEY_SIZE << 5));
    }

    protected void removeForAllocation(A key, B value){
        DATA.remove(key);
    }

    public void remove(A key){
        DATA.remove(key);
    }
}
