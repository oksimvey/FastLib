package com.robson.fastlib.api.data.structures;


import com.robson.fastlib.api.data.manager.DataFileManager;
import com.robson.fastlib.api.utils.math.FastVec3f;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FastSavedMap<A, B> extends FastTemporaryMap<A, B> {

    protected final FastDataParameter.DataType KEY_TYPE;

    protected final int CLEAN_TIME;

    private final String IDENTIFIER;

    private final File OUT_PUT;

    private FastTemporaryMap<A, Long> TRIEDTOREAD = new FastTemporaryMap<>(FastDataParameter.DataType.INTEGER);

    public FastSavedMap(FastDataParameter.DataType keytype, FastDataParameter.DataType valuetype) {
        super(valuetype);
        this.KEY_TYPE = keytype;
        this.IDENTIFIER = UUID.randomUUID().toString();
        this.OUT_PUT = DataFileManager.DYNAMIC_MAP_OUTPUT.resolve(keytype.toString() + valuetype.toString() + IDENTIFIER + ".dat").toFile();
        this.CLEAN_TIME = switch (keytype){
            case BOOLEAN -> 250;
            case GENERIC_DATA, INTEGER, FLOAT -> 1000;
            case STRING, ITEM_STACK, ENTITY -> 2000;
            case SHORT -> 500;
            case VECTORS -> 1500;
        };
        try {
            NbtIo.write(new CompoundTag(), OUT_PUT);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected int calculateCleanTime(){
        return (int) (CLEAN_TIME * Math.sqrt(KEY_SIZE << 5));
    }


    public B get(A key) {
        B value = super.get(key);
        if (value == null) {
            Long lastUpdate = TRIEDTOREAD.get(key);
            if (lastUpdate != null){
                if (System.currentTimeMillis() - lastUpdate >= CLEAN_TIME){
                    TRIEDTOREAD.remove(key);
                }
                return value;
            }
            TRIEDTOREAD.put(key, System.currentTimeMillis());
            try {
                CompoundTag tag = NbtIo.read(OUT_PUT);
                String keyString = writeKeyToString(key);
                if (tag != null && tag.contains(keyString)) {
                    Minecraft.getInstance().player.sendSystemMessage(Component.literal("restored"));
                    value = getValueFromTag(tag, keyString);
                    put(key, value);
                    tag.remove(keyString);
                    try {
                        NbtIo.write(tag, OUT_PUT);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return value;
    }


    public void removeForAllocation(A key, B value) {
        super.removeForAllocation(key, value);
        Minecraft.getInstance().player.sendSystemMessage(Component.literal("removed"));
        try {
            CompoundTag tag = NbtIo.read(OUT_PUT);
            try {
                NbtIo.write(writeToCompound(tag, key, value), OUT_PUT);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void remove(A key) {
        if (contains(key)) {
            DATA.asMap().remove(key);
            return;
        }
        try {
            CompoundTag tag = NbtIo.read(OUT_PUT);
            String string = writeKeyToString(key);
            if (tag != null && tag.contains(string)) {
                tag.remove(string);
            }
            try {
                NbtIo.write(tag, OUT_PUT);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void clearMap() {
        super.clearMap();
        try {
            NbtIo.write(new CompoundTag(), OUT_PUT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public String writeKeyToString(A key) {
        switch (KEY_TYPE) {

            case BOOLEAN -> {
                if (key instanceof Boolean bool) return bool.toString();
            }

            case STRING -> {
                if (key instanceof String str) return str;
            }

            case INTEGER -> {
                if (key instanceof Integer integer) return integer.toString();
            }

            case FLOAT -> {
                if (key instanceof Float float1) return float1.toString();
            }

            case SHORT -> {
                if (key instanceof Short short1) return short1.toString();
            }

            case ITEM_STACK -> {

            }
            case ENTITY -> {
                if (key instanceof Entity entity) return entity.getId() + "";
            }
            case VECTORS -> {
                if (key instanceof FastVec3f vec3f) {
                    return vec3f.x() + " " + vec3f.y() + " " + vec3f.z();
                }
            }
        }
        return "";
    }


    protected CompoundTag writeToCompound(CompoundTag tag, A key, B value) {

        String keyString = writeKeyToString(key);

        if (value instanceof Boolean bool) tag.putBoolean(keyString, bool);

        if (value instanceof String str) tag.putString(keyString, str);

        if (value instanceof Integer integer) tag.putInt(keyString, integer);

        if (value instanceof Float float1) tag.putFloat(keyString, float1);

        if (value instanceof Short short1) tag.putShort(keyString, short1);

        if (value instanceof ItemStack stack) ;

        if (value instanceof Entity entity) tag.putInt(keyString, entity.getId());

        if (value instanceof FastVec3f vec3f) {
            tag.putString(keyString, vec3f.x() + " " + vec3f.y() + " " + vec3f.z());

        }
        return tag;
    }

    private B getValueFromTag(CompoundTag tag, String keyString){
        return switch (VALUE_TYPE){
            case BOOLEAN -> (B) (Object) tag.getBoolean(keyString);
            case STRING -> (B) (Object) tag.getString(keyString);
            case INTEGER -> (B) (Object) tag.getInt(keyString);
            case FLOAT -> (B) (Object) tag.getFloat(keyString);
            case SHORT -> (B) (Object) tag.getShort(keyString);
            case GENERIC_DATA -> (B) (Object) tag.getString(keyString);
            case ITEM_STACK -> (B) (Object) tag.getString(keyString);
            case ENTITY -> (B) (Object) Minecraft.getInstance().level.getEntity(tag.getInt(keyString));
            case VECTORS -> (B) (Object) new FastVec3f(0 ,0, 0);
        };
    }
}
