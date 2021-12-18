// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.util;

import java.util.Objects;
import net.minecraft.client.Minecraft;
import org.lwjgl.util.glu.Sphere;
import org.lwjgl.util.glu.Cylinder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.Frustum;
import me.gavin.notorious.stuff.IMinecraft;

public class RenderUtil implements IMinecraft
{
    private static final Frustum frustrum;
    public static final Tessellator tessellator;
    
    public static void prepare() {
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableCull();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
    }
    
    public static void prepare2D() {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    }
    
    public static void release() {
        GL11.glDisable(2848);
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }
    
    public static void release2D() {
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static AxisAlignedBB generateBB(final long x, final long y, final long z) {
        final BlockPos blockPos = new BlockPos((double)x, (double)y, (double)z);
        final AxisAlignedBB bb = new AxisAlignedBB(blockPos.getX() - RenderUtil.mc.getRenderManager().viewerPosX, blockPos.getY() - RenderUtil.mc.getRenderManager().viewerPosY, blockPos.getZ() - RenderUtil.mc.getRenderManager().viewerPosZ, blockPos.getX() + 1 - RenderUtil.mc.getRenderManager().viewerPosX, blockPos.getY() + 1 - RenderUtil.mc.getRenderManager().viewerPosY, blockPos.getZ() + 1 - RenderUtil.mc.getRenderManager().viewerPosZ);
        return bb;
    }
    
    public static void renderFilledBB(final AxisAlignedBB box, final Color color) {
        renderBB(box, color, RenderMode.FILLED);
    }
    
    public static void renderOutlineBB(final AxisAlignedBB box, final Color color) {
        renderBB(box, color, RenderMode.OUTLINE);
    }
    
    public static void drawBorderedRect(final int left, final int top, final int right, final int bottom, final int lineWidth, final Color borderColor, final Color insideColor) {
        Gui.drawRect(left, top, left + right, top + bottom, insideColor.getRGB());
        Gui.drawRect(left, top, left + lineWidth, top + bottom, borderColor.getRGB());
        Gui.drawRect(left, top, left + lineWidth, top + bottom, borderColor.getRGB());
    }
    
    public static void renderBB(AxisAlignedBB box, final Color color, final RenderMode mode) {
        prepare();
        final float r = color.getRed() / 255.0f;
        final float g = color.getGreen() / 255.0f;
        final float b = color.getBlue() / 255.0f;
        final float a = color.getAlpha() / 255.0f;
        box = box.offset(-RenderUtil.mc.getRenderManager().viewerPosX, -RenderUtil.mc.getRenderManager().viewerPosY, -RenderUtil.mc.getRenderManager().viewerPosZ);
        if (mode == RenderMode.FILLED) {
            RenderGlobal.renderFilledBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, r, g, b, a);
        }
        else {
            RenderGlobal.drawBoundingBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, r, g, b, a);
        }
        release();
    }
    
    public static void entityESPBox(final Entity entity, final Color boxC, final Color outlineC, final int lineWidth) {
        final AxisAlignedBB ebox = entity.getEntityBoundingBox();
        final double lerpX = MathUtil.lerp(RenderUtil.mc.getRenderPartialTicks(), entity.lastTickPosX, entity.posX);
        final double lerpY = MathUtil.lerp(RenderUtil.mc.getRenderPartialTicks(), entity.lastTickPosY, entity.posY);
        final double lerpZ = MathUtil.lerp(RenderUtil.mc.getRenderPartialTicks(), entity.lastTickPosZ, entity.posZ);
        final AxisAlignedBB lerpBox = new AxisAlignedBB(ebox.minX - 0.05 - lerpX + (lerpX - RenderUtil.mc.getRenderManager().viewerPosX), ebox.minY - lerpY + (lerpY - RenderUtil.mc.getRenderManager().viewerPosY), ebox.minZ - 0.05 - lerpZ + (lerpZ - RenderUtil.mc.getRenderManager().viewerPosZ), ebox.maxX + 0.05 - lerpX + (lerpX - RenderUtil.mc.getRenderManager().viewerPosX), ebox.maxY + 0.1 - lerpY + (lerpY - RenderUtil.mc.getRenderManager().viewerPosY), ebox.maxZ + 0.05 - lerpZ + (lerpZ - RenderUtil.mc.getRenderManager().viewerPosZ));
        prepare();
        GL11.glLineWidth((float)lineWidth);
        RenderGlobal.renderFilledBox(lerpBox, boxC.getRed() / 255.0f, boxC.getGreen() / 255.0f, boxC.getBlue() / 255.0f, boxC.getAlpha() / 255.0f);
        RenderGlobal.drawSelectionBoundingBox(lerpBox, outlineC.getRed() / 255.0f, outlineC.getGreen() / 255.0f, outlineC.getBlue() / 255.0f, outlineC.getAlpha() / 255.0f);
        release();
    }
    
    public static void drawMultiColoredRect(final float left, final float top, final float right, final float bottom, final Color topleftcolor, final Color toprightcolor, final Color bottomleftcolor, final Color bottomrightcolor) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double)right, (double)top, 0.0).color(toprightcolor.getRed(), toprightcolor.getGreen(), toprightcolor.getBlue(), toprightcolor.getAlpha()).endVertex();
        bufferbuilder.pos((double)left, (double)top, 0.0).color(topleftcolor.getRed(), topleftcolor.getGreen(), topleftcolor.getBlue(), topleftcolor.getAlpha()).endVertex();
        bufferbuilder.pos((double)left, (double)bottom, 0.0).color(bottomleftcolor.getRed(), bottomleftcolor.getGreen(), bottomleftcolor.getBlue(), bottomleftcolor.getAlpha()).endVertex();
        bufferbuilder.pos((double)right, (double)bottom, 0.0).color(bottomrightcolor.getRed(), bottomrightcolor.getGreen(), bottomrightcolor.getBlue(), bottomrightcolor.getAlpha()).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
    
    public static void drawPenis(final EntityPlayer player, final double x, final double y, final double z, final float pspin, final float pcumsize, final float pamount) {
        GL11.glDisable(2896);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2929);
        GL11.glEnable(2848);
        GL11.glDepthMask(true);
        GL11.glLineWidth(1.0f);
        GL11.glTranslated(x, y, z);
        GL11.glRotatef(-player.rotationYaw, 0.0f, player.height, 0.0f);
        GL11.glTranslated(-x, -y, -z);
        GL11.glTranslated(x, y + player.height / 2.0f - 0.22499999403953552, z);
        GL11.glColor4f(1.38f, 0.55f, 2.38f, 1.0f);
        GL11.glRotated((double)((player.isSneaking() ? 35 : 0) + pspin), (double)(1.0f + pspin), 0.0, (double)pcumsize);
        GL11.glTranslated(0.0, 0.0, 0.07500000298023224);
        final Cylinder shaft = new Cylinder();
        shaft.setDrawStyle(100013);
        shaft.draw(0.1f, 0.11f, 0.4f, 25, 20);
        GL11.glTranslated(0.0, 0.0, -0.12500000298023223);
        GL11.glTranslated(-0.09000000074505805, 0.0, 0.0);
        final Sphere right = new Sphere();
        right.setDrawStyle(100013);
        right.draw(0.14f, 10, 20);
        GL11.glTranslated(0.16000000149011612, 0.0, 0.0);
        final Sphere left = new Sphere();
        left.setDrawStyle(100013);
        left.draw(0.14f, 10, 20);
        GL11.glColor4f(1.35f, 0.0f, 0.0f, 1.0f);
        GL11.glTranslated(-0.07000000074505806, 0.0, 0.589999952316284);
        final Sphere tip = new Sphere();
        tip.setDrawStyle(100013);
        tip.draw(0.13f, 15, 20);
        GL11.glDepthMask(true);
        GL11.glDisable(2848);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(2896);
        GL11.glEnable(3553);
    }
    
    public static void drawSideGradientRect(final int left, final int top, final int right, final int bottom, final int startColor, final int endColor) {
        final float f = (startColor >> 24 & 0xFF) / 255.0f;
        final float f2 = (startColor >> 16 & 0xFF) / 255.0f;
        final float f3 = (startColor >> 8 & 0xFF) / 255.0f;
        final float f4 = (startColor & 0xFF) / 255.0f;
        final float f5 = (endColor >> 24 & 0xFF) / 255.0f;
        final float f6 = (endColor >> 16 & 0xFF) / 255.0f;
        final float f7 = (endColor >> 8 & 0xFF) / 255.0f;
        final float f8 = (endColor & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double)right, (double)top, 0.0).color(f6, f7, f8, f5).endVertex();
        bufferbuilder.pos((double)left, (double)top, 0.0).color(f2, f3, f4, f).endVertex();
        bufferbuilder.pos((double)left, (double)bottom, 0.0).color(f2, f3, f4, f).endVertex();
        bufferbuilder.pos((double)right, (double)bottom, 0.0).color(f6, f7, f8, f5).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
    
    public static void drawGradientRect(final int left, final int top, final int right, final int bottom, final int startColor, final int endColor) {
        final float s = (startColor >> 24 & 0xFF) / 255.0f;
        final float s2 = (startColor >> 16 & 0xFF) / 255.0f;
        final float s3 = (startColor >> 8 & 0xFF) / 255.0f;
        final float s4 = (startColor & 0xFF) / 255.0f;
        final float f4 = (endColor >> 24 & 0xFF) / 255.0f;
        final float f5 = (endColor >> 16 & 0xFF) / 255.0f;
        final float f6 = (endColor >> 8 & 0xFF) / 255.0f;
        final float f7 = (endColor & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double)right, (double)top, 0.0).color(s2, s3, s4, s).endVertex();
        bufferbuilder.pos((double)left, (double)top, 0.0).color(s2, s3, s4, s).endVertex();
        bufferbuilder.pos((double)left, (double)bottom, 0.0).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos((double)right, (double)bottom, 0.0).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
    
    public static void drawBorderedRect(float x, float y, float x1, float y1, final int insideC, final int borderC) {
        prepare2D();
        x *= 2.0f;
        x1 *= 2.0f;
        y *= 2.0f;
        y1 *= 2.0f;
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        drawVLine(x, y, y1 - 1.0f, borderC);
        drawVLine(x1 - 1.0f, y, y1, borderC);
        drawHLine(x, x1 - 1.0f, y, borderC);
        drawHLine(x, x1 - 2.0f, y1 - 1.0f, borderC);
        drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        release2D();
    }
    
    public static void drawVLine(final float x, float y, float x1, final int y1) {
        if (x1 < y) {
            final float var5 = y;
            y = x1;
            x1 = var5;
        }
        drawRect(x, y + 1.0f, x + 1.0f, x1, y1);
    }
    
    public static void drawHLine(float x, float y, final float x1, final int y1) {
        if (y < x) {
            final float var5 = x;
            x = y;
            y = var5;
        }
        drawRect(x, x1, y + 1.0f, x1 + 1.0f, y1);
    }
    
    public static void drawRect(final float x, final float y, final float x1, final float y1, final int color) {
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        final BufferBuilder builder = RenderUtil.tessellator.getBuffer();
        builder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        builder.pos((double)x, (double)y1, 0.0).color(red, green, blue, alpha).endVertex();
        builder.pos((double)x1, (double)y1, 0.0).color(red, green, blue, alpha).endVertex();
        builder.pos((double)x1, (double)y, 0.0).color(red, green, blue, alpha).endVertex();
        builder.pos((double)x, (double)y, 0.0).color(red, green, blue, alpha).endVertex();
        RenderUtil.tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static boolean isInViewFrustrum(final AxisAlignedBB bb) {
        final Entity current = Minecraft.getMinecraft().getRenderViewEntity();
        RenderUtil.frustrum.setPosition(Objects.requireNonNull(current).posX, current.posY, current.posZ);
        return RenderUtil.frustrum.isBoundingBoxInFrustum(bb);
    }
    
    static {
        frustrum = new Frustum();
        tessellator = Tessellator.getInstance();
    }
    
    private enum RenderMode
    {
        FILLED, 
        OUTLINE;
    }
}
