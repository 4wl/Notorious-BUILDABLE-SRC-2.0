// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.hack.hacks.combat;

import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.Packet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.item.ItemBow;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.Hack;

@RegisterHack(name = "BowSpam", description = "bow go brrrr", category = Hack.Category.Combat)
public class BowSpam extends Hack
{
    @Override
    public void onUpdate() {
        if (BowSpam.mc.player != null && BowSpam.mc.world != null && BowSpam.mc.player.getHeldItemMainhand().getItem() instanceof ItemBow && BowSpam.mc.player.isHandActive() && BowSpam.mc.player.getItemInUseMaxCount() >= 3) {
            BowSpam.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, BowSpam.mc.player.getHorizontalFacing()));
            BowSpam.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(BowSpam.mc.player.getActiveHand()));
            BowSpam.mc.player.stopActiveHand();
        }
    }
}
