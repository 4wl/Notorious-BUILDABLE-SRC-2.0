// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.hack.hacks.combat;

import net.minecraft.item.Item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.Hack;

@RegisterHack(name = "Offhand", description = "why i coded silent switch?", category = Hack.Category.Combat)
public class Offhand extends Hack
{
    @Override
    public void onUpdate() {
        if (Offhand.mc.player.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND).getItem() == Items.END_CRYSTAL) {
            return;
        }
        final int slot = this.getItemSlot();
        if (slot != -1) {
            Offhand.mc.playerController.windowClick(Offhand.mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, (EntityPlayer)Offhand.mc.player);
            Offhand.mc.playerController.windowClick(Offhand.mc.player.inventoryContainer.windowId, 45, 0, ClickType.PICKUP, (EntityPlayer)Offhand.mc.player);
            Offhand.mc.playerController.windowClick(Offhand.mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, (EntityPlayer)Offhand.mc.player);
            Offhand.mc.playerController.updateController();
        }
    }
    
    private int getItemSlot() {
        for (int i = 0; i < 36; ++i) {
            final Item item = Offhand.mc.player.inventory.getStackInSlot(i).getItem();
            if (item == Items.TOTEM_OF_UNDYING) {
                if (i < 9) {
                    i += 36;
                }
                return i;
            }
        }
        return -1;
    }
}
