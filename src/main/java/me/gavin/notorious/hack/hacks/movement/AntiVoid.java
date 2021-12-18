// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.hack.hacks.movement;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.ModeSetting;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.Hack;

@RegisterHack(name = "AntiVoid", description = "ez", category = Hack.Category.Movement)
public class AntiVoid extends Hack
{
    @RegisterSetting
    public final ModeSetting mode;
    @RegisterSetting
    public final NumSetting yOffset;
    
    public AntiVoid() {
        this.mode = new ModeSetting("Mode", "TP", new String[] { "TP", "Jump", "Freeze" });
        this.yOffset = new NumSetting("YOffset", 0.5f, 0.1f, 1.0f, 0.1f);
    }
    
    @Override
    public String getMetaData() {
        return " [" + ChatFormatting.GRAY + this.mode.getMode() + ChatFormatting.RESET + "]";
    }
    
    @SubscribeEvent
    public void onUpdate(final PlayerLivingUpdateEvent event) {
        final double yLevel = AntiVoid.mc.player.posY;
        if (this.mode.getMode().equals("TP")) {
            if (yLevel <= (int)this.yOffset.getValue()) {
                AntiVoid.mc.player.setPosition(AntiVoid.mc.player.posX, AntiVoid.mc.player.posY + 2.0, AntiVoid.mc.player.posZ);
                this.notorious.messageManager.sendMessage("Attempting to TP out of void hole.");
            }
        }
        else if (this.mode.getMode().equals("Jump")) {
            if (yLevel <= (int)this.yOffset.getValue()) {
                AntiVoid.mc.player.jump();
                this.notorious.messageManager.sendMessage("Attempting to jump out of void hole.");
            }
        }
        else if (!AntiVoid.mc.player.noClip && yLevel <= this.yOffset.getValue()) {
            final RayTraceResult trace = AntiVoid.mc.world.rayTraceBlocks(AntiVoid.mc.player.getPositionVector(), new Vec3d(AntiVoid.mc.player.posX, 0.0, AntiVoid.mc.player.posZ), false, false, false);
            if (trace != null && trace.typeOfHit == RayTraceResult.Type.BLOCK) {
                return;
            }
            AntiVoid.mc.player.setVelocity(0.0, 0.0, 0.0);
            if (AntiVoid.mc.player.getRidingEntity() != null) {
                AntiVoid.mc.player.getRidingEntity().setVelocity(0.0, 0.0, 0.0);
            }
        }
    }
}
