// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.util;

import org.lwjgl.util.vector.Vector4f;
import net.minecraft.client.gui.ScaledResolution;
import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.util.vector.Matrix4f;
import net.minecraft.client.Minecraft;

public class ProjectionUtil
{
    private static final Minecraft mc;
    private static final Matrix4f modelMatrix;
    private static final Matrix4f projectionMatrix;
    static Vec3d camPos;
    
    public static void updateMatrix() {
        if (ProjectionUtil.mc.getRenderViewEntity() == null) {
            return;
        }
        final Vec3d viewerPos = ActiveRenderInfo.projectViewFromEntity(ProjectionUtil.mc.getRenderViewEntity(), (double)ProjectionUtil.mc.getRenderPartialTicks());
        final Vec3d relativeCamPos = ActiveRenderInfo.getCameraPosition();
        loadMatrix(ProjectionUtil.modelMatrix, 2982);
        loadMatrix(ProjectionUtil.projectionMatrix, 2983);
        ProjectionUtil.camPos = viewerPos.add(relativeCamPos);
    }
    
    private static void loadMatrix(final Matrix4f matrix, final int glBit) {
        final FloatBuffer floatBuffer = GLAllocation.createDirectFloatBuffer(16);
        GL11.glGetFloat(glBit, floatBuffer);
        matrix.load(floatBuffer);
    }
    
    public static Vec3d toScaledScreenPos(final Vec3d posIn) {
        final Vector4f vector4f = getTransformedMatrix(posIn);
        final ScaledResolution scaledResolution = new ScaledResolution(ProjectionUtil.mc);
        final int width = scaledResolution.getScaledWidth();
        final int height = scaledResolution.getScaledHeight();
        vector4f.x = width / 2.0f + (0.5f * vector4f.x * width + 0.5f);
        vector4f.y = height / 2.0f - (0.5f * vector4f.y * height + 0.5f);
        final double posZ = isVisible(vector4f, width, height) ? 0.0 : -1.0;
        return new Vec3d((double)vector4f.x, (double)vector4f.y, posZ);
    }
    
    public static Vec3d toScreenPos(final Vec3d posIn) {
        final Vector4f vector4f = getTransformedMatrix(posIn);
        final int width = ProjectionUtil.mc.displayWidth;
        final int height = ProjectionUtil.mc.displayHeight;
        vector4f.x = width / 2.0f + (0.5f * vector4f.x * width + 0.5f);
        vector4f.y = height / 2.0f - (0.5f * vector4f.y * height + 0.5f);
        final double posZ = isVisible(vector4f, width, height) ? 0.0 : -1.0;
        return new Vec3d((double)vector4f.x, (double)vector4f.y, posZ);
    }
    
    private static Vector4f getTransformedMatrix(final Vec3d posIn) {
        final Vec3d relativePos = ProjectionUtil.camPos.subtract(posIn);
        final Vector4f vector4f = new Vector4f((float)relativePos.x, (float)relativePos.y, (float)relativePos.z, 1.0f);
        transform(vector4f, ProjectionUtil.modelMatrix);
        transform(vector4f, ProjectionUtil.projectionMatrix);
        if (vector4f.w > 0.0f) {
            final Vector4f vector4f2 = vector4f;
            vector4f2.x *= -100000.0f;
            final Vector4f vector4f3 = vector4f;
            vector4f3.y *= -100000.0f;
        }
        else {
            final float invert = 1.0f / vector4f.w;
            final Vector4f vector4f4 = vector4f;
            vector4f4.x *= invert;
            final Vector4f vector4f5 = vector4f;
            vector4f5.y *= invert;
        }
        return vector4f;
    }
    
    private static void transform(final Vector4f vec, final Matrix4f matrix) {
        final float x = vec.x;
        final float y = vec.y;
        final float z = vec.z;
        vec.x = x * matrix.m00 + y * matrix.m10 + z * matrix.m20 + matrix.m30;
        vec.y = x * matrix.m01 + y * matrix.m11 + z * matrix.m21 + matrix.m31;
        vec.z = x * matrix.m02 + y * matrix.m12 + z * matrix.m22 + matrix.m32;
        vec.w = x * matrix.m03 + y * matrix.m13 + z * matrix.m23 + matrix.m33;
    }
    
    private static boolean isVisible(final Vector4f pos, final int width, final int height) {
        double var6 = width;
        double var7 = pos.x;
        if (var7 >= 0.0 && var7 <= var6) {
            var6 = height;
            var7 = pos.y;
            return var7 >= 0.0 && var7 <= var6;
        }
        return false;
    }
    
    static {
        mc = Minecraft.getMinecraft();
        modelMatrix = new Matrix4f();
        projectionMatrix = new Matrix4f();
        ProjectionUtil.camPos = new Vec3d(0.0, 0.0, 0.0);
    }
}
