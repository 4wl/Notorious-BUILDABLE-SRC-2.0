// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.hack.hacks.render;

import net.minecraft.client.renderer.OpenGlHelper;
import java.awt.Color;
import net.minecraft.world.World;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.entity.player.EntityPlayer;
import me.gavin.notorious.event.events.PacketEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;
import java.util.Map;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import java.util.HashMap;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.Hack;

@RegisterHack(name = "RBandESP", description = "ez", category = Hack.Category.Render)
public class RBandESP extends Hack
{
    @RegisterSetting
    public final NumSetting fadeTime;
    @RegisterSetting
    private final NumSetting lineWidth;
    @RegisterSetting
    public final ColorSetting color;
    private final HashMap<EntityOtherPlayerMP, Long> popFakePlayerMap;
    
    public RBandESP() {
        this.fadeTime = new NumSetting("FadeTime", 3000.0f, 1.0f, 5000.0f, 100.0f);
        this.lineWidth = new NumSetting("Line Width", 1.0f, 0.1f, 3.0f, 0.1f);
        this.color = new ColorSetting("Color", 0, 255, 255, 255);
        this.popFakePlayerMap = new HashMap<EntityOtherPlayerMP, Long>();
    }
    
    @SubscribeEvent
    public void onRenderLast(final RenderWorldLastEvent event) {
        for (final Map.Entry<EntityOtherPlayerMP, Long> entry : new HashMap<EntityOtherPlayerMP, Long>(this.popFakePlayerMap).entrySet()) {
            if (System.currentTimeMillis() - entry.getValue() > (long)this.fadeTime.getValue()) {
                this.popFakePlayerMap.remove(entry.getKey());
            }
            else {
                GL11.glPushMatrix();
                GL11.glDepthRange(0.0, 0.01);
                GL11.glDisable(2896);
                GL11.glDisable(3553);
                GL11.glPolygonMode(1032, 6913);
                GL11.glEnable(3008);
                GL11.glEnable(3042);
                GL11.glLineWidth(this.lineWidth.getValue());
                GL11.glEnable(2848);
                GL11.glHint(3154, 4354);
                this.glColor();
                this.renderEntity((Entity)entry.getKey(), event.getPartialTicks(), false);
                GL11.glHint(3154, 4352);
                GL11.glPolygonMode(1032, 6914);
                GL11.glEnable(2896);
                GL11.glDepthRange(0.0, 1.0);
                GL11.glEnable(3553);
                GL11.glPopMatrix();
            }
        }
    }
    
    @SubscribeEvent
    public void onPacket(final PacketEvent.Receive event) {
        for (final EntityPlayer e : RBandESP.mc.world.playerEntities) {
            if (event.getPacket() instanceof SPacketPlayerPosLook && RBandESP.mc.world.getEntityByID(e.getEntityId()) != null) {
                final Entity entity = RBandESP.mc.world.getEntityByID(e.getEntityId());
                if (!(entity instanceof EntityPlayer)) {
                    continue;
                }
                final EntityPlayer player = (EntityPlayer)entity;
                final EntityOtherPlayerMP fakeEntity = new EntityOtherPlayerMP((World)RBandESP.mc.world, player.getGameProfile());
                fakeEntity.copyLocationAndAnglesFrom((Entity)player);
                fakeEntity.rotationYawHead = player.rotationYawHead;
                fakeEntity.prevRotationYawHead = player.rotationYawHead;
                fakeEntity.rotationYaw = player.rotationYaw;
                fakeEntity.prevRotationYaw = player.rotationYaw;
                fakeEntity.rotationPitch = player.rotationPitch;
                fakeEntity.prevRotationPitch = player.rotationPitch;
                fakeEntity.cameraYaw = fakeEntity.rotationYaw;
                fakeEntity.cameraPitch = fakeEntity.rotationPitch;
                this.popFakePlayerMap.put(fakeEntity, System.currentTimeMillis());
            }
        }
    }
    
    private void glColor() {
        final Color clr = this.color.getAsColor();
        GL11.glColor4f(clr.getRed() / 255.0f, clr.getGreen() / 255.0f, clr.getBlue() / 255.0f, this.color.getAlpha().getValue());
    }
    
    public void renderEntity(final Entity entityIn, final float partialTicks, final boolean p_188388_3_) {
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
        RBandESP.mc.getRenderManager().renderEntity(entityIn, d0 - RBandESP.mc.getRenderManager().viewerPosX, d2 - RBandESP.mc.getRenderManager().viewerPosY, d3 - RBandESP.mc.getRenderManager().viewerPosZ, f, partialTicks, p_188388_3_);
    }
}
