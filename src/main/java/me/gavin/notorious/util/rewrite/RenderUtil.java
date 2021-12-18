// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.util.rewrite;

import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import java.awt.Color;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import me.gavin.notorious.stuff.Minecraft;

public final class RenderUtil implements Minecraft
{
    public static void prepareGL() {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
    }
    
    public static void releaseGL() {
        GL11.glDisable(2848);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
    
    public static AxisAlignedBB generateBB(final long x, final long y, final long z) {
        final BlockPos blockPos = new BlockPos((double)x, (double)y, (double)z);
        return new AxisAlignedBB(blockPos.getX() - RenderUtil.mc.getRenderManager().viewerPosX, blockPos.getY() - RenderUtil.mc.getRenderManager().viewerPosY, blockPos.getZ() - RenderUtil.mc.getRenderManager().viewerPosZ, blockPos.getX() + 1 - RenderUtil.mc.getRenderManager().viewerPosX, blockPos.getY() + 1 - RenderUtil.mc.getRenderManager().viewerPosY, blockPos.getZ() + 1 - RenderUtil.mc.getRenderManager().viewerPosZ);
    }
    
    public static void draw(final BlockPos blockPos, final boolean box, final boolean outline, final double boxHeight, final double outlineHeight, final Color colour) {
        final AxisAlignedBB axisAlignedBB = generateBB(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        prepareGL();
        if (box) {
            drawFilledBox(axisAlignedBB, boxHeight, colour.getRed() / 255.0f, colour.getGreen() / 255.0f, colour.getBlue() / 255.0f, colour.getAlpha() / 255.0f);
        }
        if (outline) {
            drawBoundingBox(axisAlignedBB, outlineHeight, colour.getRed() / 255.0f, colour.getGreen() / 255.0f, colour.getBlue() / 255.0f, colour.getAlpha() / 255.0f);
        }
        releaseGL();
    }
    
    public static void drawBoundingBox(final BufferBuilder bufferBuilder, final double minX, final double minY, final double minZ, final double maxX, final double maxY, final double maxZ, final float red, final float green, final float blue, final float alpha) {
        bufferBuilder.pos(minX, minY, minZ).color(red, green, blue, 0.0f).endVertex();
        bufferBuilder.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(minX, maxY, maxZ).color(red, green, blue, 0.0f).endVertex();
        bufferBuilder.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(maxX, maxY, maxZ).color(red, green, blue, 0.0f).endVertex();
        bufferBuilder.pos(maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(maxX, maxY, minZ).color(red, green, blue, 0.0f).endVertex();
        bufferBuilder.pos(maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(maxX, minY, minZ).color(red, green, blue, 0.0f).endVertex();
    }
    
    public static void addChainedFilledBoxVertices(final BufferBuilder bufferBuilder, final double x1, final double y1, final double z1, final double x2, final double y2, final double z2, final float red, final float green, final float blue, final float alpha) {
        bufferBuilder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x1, y1, z2).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x1, y2, z1).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x1, y2, z2).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x1, y2, z2).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x1, y1, z2).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x2, y1, z1).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x2, y2, z1).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x2, y2, z1).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x2, y1, z1).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x1, y2, z1).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x2, y1, z1).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x1, y1, z2).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x1, y2, z1).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x1, y2, z1).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x1, y2, z2).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x2, y2, z1).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
        bufferBuilder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
    }
    
    public static void renderFilledBox(final double minX, final double minY, final double minZ, final double maxX, final double maxY, final double maxZ, final double height, final float red, final float green, final float blue, final float alpha) {
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);
        addChainedFilledBoxVertices(bufferbuilder, minX, minY, minZ, maxX, maxY + height, maxZ, red, green, blue, alpha);
        tessellator.draw();
    }
    
    public static void renderBoundingBox(final double minX, final double minY, final double minZ, final double maxX, final double maxY, final double maxZ, final double height, final float red, final float green, final float blue, final float alpha) {
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        drawBoundingBox(bufferbuilder, minX, minY, minZ, maxX, maxY + height, maxZ, red, green, blue, alpha);
        tessellator.draw();
    }
    
    public static void drawFilledBox(final AxisAlignedBB axisAlignedBB, final double height, final float red, final float green, final float blue, final float alpha) {
        renderFilledBox(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ, axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ, height, red, green, blue, alpha);
    }
    
    public static void drawBoundingBox(final AxisAlignedBB axisAlignedBB, final double height, final float red, final float green, final float blue, final float alpha) {
        renderBoundingBox(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ, axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ, height, red, green, blue, alpha);
    }
}
