// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.hack.hacks.movement;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.client.event.InputUpdateEvent;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.Hack;

@RegisterHack(name = "TimerSpeed", description = "finally, its garbage as allways :/", category = Hack.Category.Movement)
public class TimerSpeed extends Hack
{
    @SubscribeEvent
    public void onUpdate(final InputUpdateEvent event) {
        if (TimerSpeed.mc.player.moveForward != 1.0f || TimerSpeed.mc.player.moveStrafing != 1.0f) {}
        if (TimerSpeed.mc.player.onGround) {
            TimerSpeed.mc.player.jump();
        }
        else {
            final EntityPlayerSP player = TimerSpeed.mc.player;
            player.motionX *= 1.0;
            final EntityPlayerSP player2 = TimerSpeed.mc.player;
            player2.motionZ *= 1.0;
            if (TimerSpeed.mc.player.fallDistance > 0.4) {
                final EntityPlayerSP player3 = TimerSpeed.mc.player;
                player3.motionY -= 62.0;
            }
        }
    }
}
