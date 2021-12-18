// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.util.rewrite;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.item.ItemBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import me.gavin.notorious.util.Instance;

public class InventoryUtil implements Instance
{
    public static int findItem(final Item item, final int minimum, final int maximum) {
        for (int i = minimum; i <= maximum; ++i) {
            final ItemStack stack = InventoryUtil.mc.player.inventory.getStackInSlot(i);
            if (stack.getItem() == item) {
                return i;
            }
        }
        return -1;
    }
    
    public static int findBlock(final Block block, final int minimum, final int maximum) {
        for (int i = minimum; i <= maximum; ++i) {
            final ItemStack stack = InventoryUtil.mc.player.inventory.getStackInSlot(i);
            if (stack.getItem() instanceof ItemBlock) {
                final ItemBlock item = (ItemBlock)stack.getItem();
                if (item.getBlock() == block) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    public static void switchToSlot(final int slot, final boolean silent) {
        InventoryUtil.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(slot));
        if (!silent) {
            InventoryUtil.mc.player.inventory.currentItem = slot;
        }
        if (InventoryUtil.mc.player.connection.getNetworkManager().isChannelOpen()) {
            InventoryUtil.mc.player.connection.getNetworkManager().processReceivedPackets();
        }
        else {
            InventoryUtil.mc.player.connection.getNetworkManager().handleDisconnection();
        }
    }
    
    public static void moveItemToSlot(final Integer startSlot, final Integer endSlot) {
        InventoryUtil.mc.playerController.windowClick(InventoryUtil.mc.player.inventoryContainer.windowId, (int)startSlot, 0, ClickType.PICKUP, (EntityPlayer)InventoryUtil.mc.player);
        InventoryUtil.mc.playerController.windowClick(InventoryUtil.mc.player.inventoryContainer.windowId, (int)endSlot, 0, ClickType.PICKUP, (EntityPlayer)InventoryUtil.mc.player);
        InventoryUtil.mc.playerController.windowClick(InventoryUtil.mc.player.inventoryContainer.windowId, (int)startSlot, 0, ClickType.PICKUP, (EntityPlayer)InventoryUtil.mc.player);
    }
}
