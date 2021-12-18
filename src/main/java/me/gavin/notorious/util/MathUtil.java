// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.util;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.player.EntityPlayer;
import me.gavin.notorious.stuff.IMinecraft;

public class MathUtil implements IMinecraft
{
    public static float normalize(final float value, final float min, final float max) {
        return 1.0f - (value - min) / (max - min);
    }
    
    public static float[] calculateLookAt(final double x, final double y, final double z, final EntityPlayer me) {
        double dirx = lerp(MathUtil.mc.getRenderPartialTicks(), me.lastTickPosX, me.posX) - x;
        double diry = lerp(MathUtil.mc.getRenderPartialTicks(), me.lastTickPosY, me.posY) + me.getEyeHeight() - y;
        double dirz = lerp(MathUtil.mc.getRenderPartialTicks(), me.lastTickPosZ, me.posZ) - z;
        final double distance = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);
        dirx /= distance;
        diry /= distance;
        dirz /= distance;
        float pitch = (float)Math.asin(diry);
        float yaw = (float)Math.atan2(dirz, dirx);
        pitch = (float)(pitch * 180.0f / 3.141592653589793);
        yaw = (float)(yaw * 180.0f / 3.141592653589793);
        yaw += 90.0f;
        return new float[] { yaw, pitch };
    }
    
    private static float[] getLegitRotations(final Vec3d vec) {
        final Vec3d eyesPos = getEyesPos();
        final double diffX = vec.x - eyesPos.x;
        final double diffY = vec.y - eyesPos.y;
        final double diffZ = vec.z - eyesPos.z;
        final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[] { MathUtil.mc.player.rotationYaw + MathHelper.wrapDegrees(yaw - MathUtil.mc.player.rotationYaw), MathUtil.mc.player.rotationPitch + MathHelper.wrapDegrees(pitch - MathUtil.mc.player.rotationPitch) };
    }
    
    public static double roundValueToCenter(final double inputVal) {
        double roundVal = (double)Math.round(inputVal);
        if (roundVal > inputVal) {
            roundVal -= 0.5;
        }
        else if (roundVal <= inputVal) {
            roundVal += 0.5;
        }
        return roundVal;
    }
    
    private static Vec3d getEyesPos() {
        return new Vec3d(MathUtil.mc.player.posX, MathUtil.mc.player.posY + MathUtil.mc.player.getEyeHeight(), MathUtil.mc.player.posZ);
    }
    
    public static float[] calcAngle(final Vec3d from, final Vec3d to) {
        final double difX = to.x - from.x;
        final double difY = (to.y - from.y) * -1.0;
        final double difZ = to.z - from.z;
        final double dist = MathHelper.sqrt(difX * difX + difZ * difZ);
        return new float[] { (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0), (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difY, dist))) };
    }
    
    public static float lerp(final float delta, final float start, final float end) {
        return start + delta * (end - start);
    }
    
    public static double lerp(final double delta, final double start, final double end) {
        return start + delta * (end - start);
    }
    
    public static double square(final double input) {
        return input * input;
    }
    
    public static int clamp(final int num, final int min, final int max) {
        return (num < min) ? min : Math.min(num, max);
    }
}
