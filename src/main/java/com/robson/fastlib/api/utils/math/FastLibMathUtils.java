package com.robson.fastlib.api.utils.math;

import com.robson.fastlib.api.data.manager.PlayerDataManager;
import com.robson.fastlib.api.data.types.PlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public interface FastLibMathUtils {

    float EULER = 2.718281f;

    float PI = 3.141592f;

    static float degreeToRadians(float degree) {
        return PI * (degree / 180f);
    }

    static float radiansToDegree(float radians) {
        return 180f * (radians / PI);
    }

    static FastVec3f rotate3DVector(FastVec3f vec, float degrees) {
        float theta = degreeToRadians(degrees);
        float x = (float) ((vec.x() * Math.cos(theta)) - (vec.z() * Math.sin(theta)));
        float z = (float) ((vec.x() * Math.sin(theta)) + (vec.z() * Math.cos(theta)));
        return new FastVec3f(x, vec.y(), z);
    }

    static FastVec2f rotate2DVector(FastVec2f vec, float degrees) {
        float theta = degreeToRadians(degrees);
        float x = (float) ((vec.x() * Math.cos(theta)) - (vec.y() * Math.sin(theta)));
        float y = (float) ((vec.x() * Math.sin(theta)) + (vec.y() * Math.cos(theta)));
        return new FastVec2f(x, y);
    }


    /// x is the screen x position
    /// y is the screen y position
    /// z is the scale, affected by distance
    static FastVec3f transformWorldToScreen(FastVec3f worldpos){
        var mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return new FastVec3f(0, 0, 0);

        if (worldpos == null) return new FastVec3f(0, 0, 0);

        Vec3 campos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        FastVec3f relative = worldpos.sub((float) campos.x, (float) campos.y, (float) campos.z);

        // Yaw rotation (adjusted for Minecraft's coordinate system)
        float yawRad = (float) Math.toRadians(180 - Minecraft.getInstance().gameRenderer.getMainCamera().getYRot());
        float sinYaw = (float) Math.sin(yawRad);
        float cosYaw = (float) Math.cos(yawRad);

        float x1 = relative.x() * cosYaw - relative.z() * sinYaw;
        float z1 = relative.x() * sinYaw + relative.z() * cosYaw;

        // Use positive pitch radians
        float pitchRad = (float) Math.toRadians(Minecraft.getInstance().gameRenderer.getMainCamera().getXRot());
        float sinPitch = (float) Math.sin(pitchRad);
        float cosPitch = (float) Math.cos(pitchRad);

        // Remove Z clamping
        float y1 = relative.y() * cosPitch - z1 * sinPitch;
        float z2 = relative.y() * sinPitch + z1 * cosPitch;

        Vec3 viewSpace = new Vec3(x1, y1, z2);

        // Check if point is behind camera or too close (use -0.01 for front points)
        if (viewSpace.z > -0.01f) return new FastVec3f(0, 0, 0);

        float guiwidth = mc.getWindow().getGuiScaledWidth();

        float guiheight = mc.getWindow().getGuiScaledHeight();

        float fovRad = (float) Math.toRadians(mc.options.fov().get());
        float scale = (float) (1.0 / Math.tan(fovRad / 2));
        float aspect = (float) guiwidth / guiheight;

        // Flip signs for correct projection mapping
        float x_proj = (float) (-(viewSpace.x / viewSpace.z) * scale / aspect);
        float y_proj = (float) (-(viewSpace.y / viewSpace.z) * scale);

        int screenX = (int) ((x_proj + 1) / 2 * guiwidth);
        int screenY = (int) ((1 - y_proj) / 2 * guiheight);

        if (Float.isNaN(x_proj) || Float.isNaN(y_proj)) return new FastVec3f(0, 0, 0);
        if (screenX < 0 || screenX > guiwidth||
                screenY < 0 || screenY > guiheight) return new FastVec3f(0, 0, 0);

        return new FastVec3f(screenX, screenY, 1 / relative.length());
    }
}
