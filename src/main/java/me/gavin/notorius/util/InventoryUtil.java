// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorius.util;


import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class InventoryUtil
{
    private static final Minecraft mc;
    
    public static int findBlockInHotbar(final Block blockToFind) {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = InventoryUtil.mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY) {
                if (stack.getItem() instanceof ItemBlock) {
                    final Block block = ((ItemBlock)stack.getItem()).getBlock();
                    if (block.equals(blockToFind)) {
                        slot = i;
                        break;
                    }
                }
            }
        }
        return slot;
    }
    
    public static int findBlockInHotbarObiEchestRandom() {
        final int slot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = InventoryUtil.mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY) {
                if (stack.getItem() instanceof ItemBlock) {
                    final Block block = ((ItemBlock)stack.getItem()).getBlock();
                    if (block.equals(Blocks.OBSIDIAN)) {
                        return i;
                    }
                }
            }
        }
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = InventoryUtil.mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY) {
                if (stack.getItem() instanceof ItemBlock) {
                    final Block block = ((ItemBlock)stack.getItem()).getBlock();
                    if (block.equals(Blocks.ENDER_CHEST)) {
                        return i;
                    }
                }
            }
        }
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = InventoryUtil.mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock) {
                final Block block = ((ItemBlock)stack.getItem()).getBlock();
                return i;
            }
        }
        return slot;
    }
    
    public static int findItemInHotbar(final Item itemToFind) {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = InventoryUtil.mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY) {
                if (stack.getItem() instanceof Item) {
                    final Item item = stack.getItem();
                    if (item.equals(itemToFind)) {
                        slot = i;
                        break;
                    }
                }
            }
        }
        return slot;
    }
    
    public static int getItems(final Item i) {
        return InventoryUtil.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == i).mapToInt(ItemStack::getMaxStackSize).sum() + InventoryUtil.mc.player.inventory.offHandInventory.stream().filter(itemStack -> itemStack.getItem() == i).mapToInt(ItemStack::getMaxStackSize).sum();
    }
    
    public static int getBlocks(final Block block) {
        return getItems(Item.getItemFromBlock(block));
    }
    
    static {
        mc = Minecraft.getMinecraft();
    }
}
