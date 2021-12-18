// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.hack.hacks.movement;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.MovementInput;
import net.minecraftforge.client.event.InputUpdateEvent;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.Hack;

@RegisterHack(name = "NoSlow", description = "noslowdown for you", category = Hack.Category.Movement)
public class NoSlow extends Hack
{
    @SubscribeEvent
    public void onInput(final InputUpdateEvent event) {
        if (NoSlow.mc.player.isHandActive() && !NoSlow.mc.player.isRiding()) {
            final MovementInput movementInput = event.getMovementInput();
            movementInput.moveStrafe *= 5.0f;
            final MovementInput movementInput2 = event.getMovementInput();
            movementInput2.moveForward *= 5.0f;
        }
    }
}
