// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.hack.hacks.movement;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.Hack;

@RegisterHack(name = "AutoHop", description = "ching chong autohop", category = Hack.Category.Movement)
public class AutoHop extends Hack
{
    @SubscribeEvent
    public void onUpdate(final PlayerLivingUpdateEvent event) {
        if (AutoHop.mc.player.onGround && !AutoHop.mc.player.isSneaking() && !AutoHop.mc.player.isInLava() && !AutoHop.mc.player.isInWater() && !AutoHop.mc.player.isOnLadder() && !AutoHop.mc.player.noClip && !AutoHop.mc.gameSettings.keyBindSneak.isKeyDown() && !AutoHop.mc.gameSettings.keyBindJump.isKeyDown()) {
            AutoHop.mc.player.jump();
        }
    }
}
