// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.hack.hacks.combat;

import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import me.gavin.notorius.util.InventoryUtil;
import net.minecraft.init.Items;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.Hack;

@RegisterHack(name = "GhostGap", description = "or how to get kicked for packets", category = Hack.Category.Combat)
public class GhostGap extends Hack
{
    int startingHand;
    
    @Override
    public void onUpdate() {
        final int gapHand = InventoryUtil.findItemInHotbar(Items.GOLDEN_APPLE);
        if (InventoryUtil.findItemInHotbar(Items.GOLDEN_APPLE) != -1) {
            GhostGap.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(gapHand));
            GhostGap.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        }
    }
    
    public void onEnable() {
        this.startingHand = GhostGap.mc.player.inventory.currentItem;
    }
}
