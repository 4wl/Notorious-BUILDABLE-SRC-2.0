// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.hack.hacks.render;

import java.awt.Color;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import me.gavin.notorious.event.events.TotemPopEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;
import java.util.Map;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import java.util.HashMap;
import me.gavin.notorious.setting.ModeSetting;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.Hack;

@RegisterHack(name = "PopESP", description = "Renders pops", category = Hack.Category.Render)
public class PopESP extends Hack
{
    @RegisterSetting
    public final NumSetting fadeTime;
    @RegisterSetting
    public final NumSetting fadeSpeed;
    @RegisterSetting
    public final ModeSetting fadeMode;
    @RegisterSetting
    public final ModeSetting elevatorMode;
    @RegisterSetting
    public final ModeSetting renderMode;
    @RegisterSetting
    private final NumSetting lineWidth;
    @RegisterSetting
    public final NumSetting r;
    @RegisterSetting
    public final NumSetting g;
    @RegisterSetting
    public final NumSetting b;
    @RegisterSetting
    public final NumSetting a;
    private static final HashMap<EntityOtherPlayerMP, Long> popFakePlayerMap;
    float fade;
    
    public PopESP() {
        this.fadeTime = new NumSetting("FadeTime", 3000.0f, 1.0f, 5000.0f, 100.0f);
        this.fadeSpeed = new NumSetting("FadeSpeed", 0.05f, 0.01f, 1.0f, 0.01f);
        this.fadeMode = new ModeSetting("FadeMode", "Elevator", new String[] { "Elevator", "Fade", "None" });
        this.elevatorMode = new ModeSetting("ElevatorMode", "Heaven", new String[] { "Heaven", "Hell" });
        this.renderMode = new ModeSetting("RenderMode", "Both", new String[] { "Both", "Textured", "Wireframe" });
        this.lineWidth = new NumSetting("Line Width", 1.0f, 0.1f, 3.0f, 0.1f);
        this.r = new NumSetting("Red", 255.0f, 0.0f, 255.0f, 1.0f);
        this.g = new NumSetting("Green", 255.0f, 0.0f, 255.0f, 1.0f);
        this.b = new NumSetting("Blue", 255.0f, 0.0f, 255.0f, 1.0f);
        this.a = new NumSetting("Alpha", 255.0f, 0.0f, 255.0f, 1.0f);
        this.fade = 1.0f;
    }
    
    @Override
    public String getMetaData() {
        if (this.fadeMode.getMode().equals("Elevator")) {
            return " [" + ChatFormatting.GRAY + this.elevatorMode.getMode() + ChatFormatting.RESET + "]";
        }
        return " [" + ChatFormatting.GRAY + this.fadeMode.getMode() + ChatFormatting.RESET + "]";
    }
    
    @SubscribeEvent
    public void onRenderLast(final RenderWorldLastEvent event) {
        for (final Map.Entry<EntityOtherPlayerMP, Long> entry : new HashMap<EntityOtherPlayerMP, Long>(PopESP.popFakePlayerMap).entrySet()) {
            boolean wireFrame;
            boolean textured;
            if (this.renderMode.getMode().equals("Both")) {
                wireFrame = true;
                textured = true;
            }
            else if (this.renderMode.getMode().equals("Wireframe")) {
                wireFrame = true;
                textured = false;
            }
            else {
                wireFrame = false;
                textured = true;
            }
            if (System.currentTimeMillis() - entry.getValue() < (long)this.fadeTime.getValue() && this.fadeMode.getMode().equals("Elevator")) {
                if (this.elevatorMode.getMode().equals("Heaven")) {
                    final EntityOtherPlayerMP entityOtherPlayerMP = entry.getKey();
                    entityOtherPlayerMP.posY += this.fadeSpeed.getValue() * event.getPartialTicks();
                }
                else {
                    final EntityOtherPlayerMP entityOtherPlayerMP2 = entry.getKey();
                    entityOtherPlayerMP2.posY -= this.fadeSpeed.getValue() * event.getPartialTicks();
                }
            }
            else if (System.currentTimeMillis() - entry.getValue() < (long)this.fadeTime.getValue() && this.fadeMode.getMode().equals("Fade")) {
                this.fade -= this.fadeSpeed.getValue();
            }
            if (System.currentTimeMillis() - entry.getValue() > (long)this.fadeTime.getValue() || this.fade == 0.0f) {
                PopESP.popFakePlayerMap.remove(entry.getKey());
            }
            else {
                GL11.glPushMatrix();
                GL11.glDepthRange(0.01, 1.0);
                if (wireFrame) {
                    GL11.glDisable(2896);
                    GL11.glDisable(3553);
                    GL11.glPolygonMode(1032, 6913);
                    GL11.glEnable(3008);
                    GL11.glEnable(3042);
                    GL11.glLineWidth(this.lineWidth.getValue());
                    GL11.glEnable(2848);
                    GL11.glHint(3154, 4354);
                    GL11.glColor4f(this.r.getValue() / 255.0f, this.g.getValue() / 255.0f, this.b.getValue() / 255.0f, this.fadeMode.getMode().equals("Fade") ? this.fade : 1.0f);
                    this.renderEntityStatic((Entity)entry.getKey(), event.getPartialTicks(), true);
                    GL11.glHint(3154, 4352);
                    GL11.glPolygonMode(1032, 6914);
                    GL11.glEnable(2896);
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                    GL11.glEnable(3553);
                }
                if (textured) {
                    GL11.glPushAttrib(-1);
                    GL11.glEnable(3008);
                    GL11.glDisable(3553);
                    GL11.glDisable(2896);
                    GL11.glEnable(3042);
                    GL11.glDisable(2929);
                    GL11.glDepthMask(false);
                    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                    GL11.glLineWidth(1.5f);
                    GL11.glColor4f(this.r.getValue() / 255.0f, this.g.getValue() / 255.0f, this.b.getValue() / 255.0f, this.fadeMode.getMode().equals("Fade") ? this.fade : (this.a.getValue() / 255.0f));
                    this.renderEntityStatic((Entity)entry.getKey(), event.getPartialTicks(), true);
                    GL11.glEnable(2929);
                    GL11.glDepthMask(true);
                    GL11.glDisable(3008);
                    GL11.glEnable(3553);
                    GL11.glEnable(2896);
                    GL11.glDisable(3042);
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                    GL11.glPopAttrib();
                }
                GL11.glDepthRange(0.0, 1.0);
                GL11.glPopMatrix();
                this.fade = 1.0f;
            }
        }
    }
    
    @SubscribeEvent
    public void onPop(final TotemPopEvent event) {
        if (PopESP.mc.world.getEntityByID(event.getEntityId()) != null) {
            final Entity entity = PopESP.mc.world.getEntityByID(event.getEntityId());
            if (entity instanceof EntityPlayer) {
                final EntityPlayer player = (EntityPlayer)entity;
                final EntityOtherPlayerMP fakeEntity = new EntityOtherPlayerMP((World)PopESP.mc.world, player.getGameProfile());
                fakeEntity.copyLocationAndAnglesFrom((Entity)player);
                fakeEntity.rotationYawHead = player.rotationYawHead;
                fakeEntity.prevRotationYawHead = player.rotationYawHead;
                fakeEntity.rotationYaw = player.rotationYaw;
                fakeEntity.prevRotationYaw = player.rotationYaw;
                fakeEntity.rotationPitch = player.rotationPitch;
                fakeEntity.prevRotationPitch = player.rotationPitch;
                fakeEntity.cameraYaw = fakeEntity.rotationYaw;
                fakeEntity.cameraPitch = fakeEntity.rotationPitch;
                PopESP.popFakePlayerMap.put(fakeEntity, System.currentTimeMillis());
            }
        }
    }
    
    public void renderEntityStatic(final Entity entityIn, final float partialTicks, final boolean p_188388_3_) {
        if (entityIn.ticksExisted == 0) {
            entityIn.lastTickPosX = entityIn.posX;
            entityIn.lastTickPosY = entityIn.posY;
            entityIn.lastTickPosZ = entityIn.posZ;
        }
        final double d0 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * partialTicks;
        final double d2 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * partialTicks;
        final double d3 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * partialTicks;
        final float f = entityIn.prevRotationYaw + (entityIn.rotationYaw - entityIn.prevRotationYaw) * partialTicks;
        int i = entityIn.getBrightnessForRender();
        if (entityIn.isBurning()) {
            i = 15728880;
        }
        final int j = i % 65536;
        final int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
        PopESP.mc.getRenderManager().renderEntity(entityIn, d0 - PopESP.mc.getRenderManager().viewerPosX, d2 - PopESP.mc.getRenderManager().viewerPosY, d3 - PopESP.mc.getRenderManager().viewerPosZ, f, partialTicks, p_188388_3_);
    }
    
    private void glColor(final boolean textured, final boolean wireframe) {
        final Color clr = new Color(this.r.getValue(), this.g.getValue(), this.b.getValue(), this.a.getValue());
        if (textured) {
            GL11.glColor4f(clr.getRed() / 255.0f, clr.getGreen() / 255.0f, clr.getBlue() / 255.0f, clr.getAlpha() / 255.0f);
        }
        if (wireframe) {
            GL11.glColor4f(clr.getRed() / 255.0f, clr.getGreen() / 255.0f, clr.getBlue() / 255.0f, 1.0f);
        }
    }
    
    static {
        popFakePlayerMap = new HashMap<EntityOtherPlayerMP, Long>();
    }
}
