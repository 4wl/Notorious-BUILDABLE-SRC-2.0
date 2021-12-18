// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.hack.hacks.movement;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.client.event.InputUpdateEvent;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.ModeSetting;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.Hack;

@RegisterHack(name = "Speed", description = "floor gang speed (beta) ", category = Hack.Category.Movement)
public class FloorGang extends Hack
{
    @RegisterSetting
    public final ModeSetting speedMode;
    
    public FloorGang() {
        this.speedMode = new ModeSetting("SpeedMode", "Bhop", new String[] { "Bhop", "Ground", "TP" });
    }
    
    @SubscribeEvent
    public void onUpdate(final InputUpdateEvent event) {
        if (this.speedMode.getMode().equals("Bhop")) {
            if ((FloorGang.mc.player.moveForward != 0.0f || FloorGang.mc.player.moveStrafing != 0.0f) && !FloorGang.mc.player.isSneaking() && FloorGang.mc.player.onGround) {
                FloorGang.mc.player.jump();
                final EntityPlayerSP player = FloorGang.mc.player;
                player.motionX *= 1.4;
                final EntityPlayerSP player2 = FloorGang.mc.player;
                player2.motionY *= 0.99;
                final EntityPlayerSP player3 = FloorGang.mc.player;
                player3.motionZ *= 1.4;
            }
            if (this.speedMode.getMode().equals("Ground") && FloorGang.mc.player.onGround && FloorGang.mc.gameSettings.keyBindForward.isKeyDown()) {
                final double speedValue = 0.21;
                final double yaw = Math.toRadians(FloorGang.mc.player.rotationYaw);
                final double x = -Math.sin(yaw) * speedValue;
                final double z = Math.cos(yaw) * speedValue;
                FloorGang.mc.player.motionX = x;
                FloorGang.mc.player.motionZ = z;
                FloorGang.mc.player.jump();
            }
        }
    }
}
