// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.hack.hacks.render;

import me.gavin.notorious.util.RenderUtil;
import me.gavin.notorious.Notorious;
import me.gavin.notorious.hack.hacks.client.ClickGUI;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.util.NColor;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.ModeSetting;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.Hack;

@RegisterHack(name = "ESP", description = "Draws a box around entities.", category = Hack.Category.Render)
public class ESP extends Hack
{
    @RegisterSetting
    public final ModeSetting espMode;
    @RegisterSetting
    public final ModeSetting colorMode;
    @RegisterSetting
    public final ColorSetting outlineColor;
    @RegisterSetting
    public final ColorSetting boxColor;
    @RegisterSetting
    public final NumSetting lineWidth;
    @RegisterSetting
    public final BooleanSetting players;
    @RegisterSetting
    public final BooleanSetting animals;
    @RegisterSetting
    public final BooleanSetting mobs;
    @RegisterSetting
    public final BooleanSetting items;
    
    public ESP() {
        this.espMode = new ModeSetting("ESPMode", "RotateBox", new String[] { "RotateBox", "Glow" });
        this.colorMode = new ModeSetting("ColorMode", "ClientSync", new String[] { "ClientSync", "RGB" });
        this.outlineColor = new ColorSetting("Outline", new NColor(255, 255, 255, 255));
        this.boxColor = new ColorSetting("Box", new NColor(255, 255, 255, 125));
        this.lineWidth = new NumSetting("LineWidth", 2.0f, 0.1f, 4.0f, 0.1f);
        this.players = new BooleanSetting("Players", true);
        this.animals = new BooleanSetting("Animals", true);
        this.mobs = new BooleanSetting("Mobs", true);
        this.items = new BooleanSetting("Items", true);
    }
    
    @Override
    public String getMetaData() {
        return " [" + ChatFormatting.GRAY + this.lineWidth.getValue() + ChatFormatting.RESET + "]";
    }
    
    public void onDisable() {
        for (final Entity e : ESP.mc.world.loadedEntityList) {
            e.setGlowing(false);
        }
    }
    
    @SubscribeEvent
    public void onUpdate(final PlayerLivingUpdateEvent event) {
        for (Entity entity : ESP.mc.world.loadedEntityList) {}
    }
    
    @SubscribeEvent
    public void onRender(final RenderWorldLastEvent event) {
        for (final Entity e : ESP.mc.world.loadedEntityList) {
            final AxisAlignedBB box = e.getEntityBoundingBox();
            final double x = (e.posX - e.lastTickPosX) * event.getPartialTicks();
            final double y = (e.posY - e.lastTickPosY) * event.getPartialTicks();
            final double z = (e.posZ - e.lastTickPosZ) * event.getPartialTicks();
            final AxisAlignedBB bb = new AxisAlignedBB(box.minX + x, box.minY + y, box.minZ + z, box.maxX + x, box.maxY + y, box.maxZ + z);
            if (e == ESP.mc.player && ESP.mc.gameSettings.thirdPersonView == 0) {
                continue;
            }
            if (e instanceof EntityPlayer && this.players.isEnabled()) {
                if (this.espMode.getMode().equals("RotateBox")) {
                    this.render(e);
                }
                else {
                    e.setGlowing(true);
                }
            }
            else if (e instanceof EntityAnimal && this.animals.isEnabled()) {
                if (this.espMode.getMode().equals("RotateBox")) {
                    this.render(e);
                }
                else {
                    e.setGlowing(true);
                }
            }
            else if ((e instanceof EntityMob || e instanceof EntitySlime) && this.mobs.isEnabled()) {
                if (this.espMode.getMode().equals("RotateBox")) {
                    this.render(e);
                }
                else {
                    e.setGlowing(true);
                }
            }
            else {
                if (!(e instanceof EntityItem) || !this.items.isEnabled()) {
                    continue;
                }
                if (this.espMode.getMode().equals("RotateBox")) {
                    this.render(e);
                }
                else {
                    e.setGlowing(true);
                }
            }
        }
    }
    
    private void render(final Entity entity) {
        GlStateManager.pushMatrix();
        final AxisAlignedBB b = entity.getEntityBoundingBox().offset(-ESP.mc.getRenderManager().viewerPosX, -ESP.mc.getRenderManager().viewerPosY, -ESP.mc.getRenderManager().viewerPosZ);
        final double x = (b.maxX - b.minX) / 2.0 + b.minX;
        final double y = (b.maxY - b.minY) / 2.0 + b.minY;
        final double z = (b.maxZ - b.minZ) / 2.0 + b.minZ;
        GL11.glTranslated(x, y, z);
        GL11.glRotated(-MathHelper.clampedLerp((double)entity.prevRotationYaw, (double)entity.rotationYaw, (double)ESP.mc.getRenderPartialTicks()), 0.0, 1.0, 0.0);
        GL11.glTranslated(-x, -y, -z);
        RenderUtil.entityESPBox(entity, this.colorMode.getMode().equals("RGB") ? this.boxColor.getAsColor() : Notorious.INSTANCE.hackManager.getHack(ClickGUI.class).guiColor.getAsColor(), this.colorMode.getMode().equals("RGB") ? this.outlineColor.getAsColor() : Notorious.INSTANCE.hackManager.getHack(ClickGUI.class).guiColor.getAsColor(), (int)this.lineWidth.getValue());
        GlStateManager.popMatrix();
    }
}
