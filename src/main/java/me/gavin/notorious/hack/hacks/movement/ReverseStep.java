// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.hack.hacks.movement;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.Hack;

@RegisterHack(name = "ReverseStep", description = "ez", category = Hack.Category.Movement)
public class ReverseStep extends Hack
{
    @SubscribeEvent
    public void onUpdate(final PlayerLivingUpdateEvent event) {
        if (ReverseStep.mc.player != null && ReverseStep.mc.world != null && ReverseStep.mc.player.onGround && !ReverseStep.mc.player.isSneaking() && !ReverseStep.mc.player.isInWater() && !ReverseStep.mc.player.isDead && !ReverseStep.mc.player.isInLava() && !ReverseStep.mc.player.isOnLadder() && !ReverseStep.mc.player.noClip && !ReverseStep.mc.gameSettings.keyBindSneak.isKeyDown() && !ReverseStep.mc.gameSettings.keyBindJump.isKeyDown() && ReverseStep.mc.player.onGround) {
            final EntityPlayerSP player = ReverseStep.mc.player;
            --player.motionY;
        }
    }
}
