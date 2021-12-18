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

@RegisterHack(name = "AutoTotem", description = "stay safe", category = Hack.Category.Combat)
public class AutoTotem extends Hack
{
    @Override
    public void onUpdate() {
        if (AutoTotem.mc.player.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND).getItem() == Items.TOTEM_OF_UNDYING) {
            return;
        }
        final int slot = this.getItemSlot();
        if (slot != -1) {
            AutoTotem.mc.playerController.windowClick(AutoTotem.mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, (EntityPlayer)AutoTotem.mc.player);
            AutoTotem.mc.playerController.windowClick(AutoTotem.mc.player.inventoryContainer.windowId, 45, 0, ClickType.PICKUP, (EntityPlayer)AutoTotem.mc.player);
            AutoTotem.mc.playerController.windowClick(AutoTotem.mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, (EntityPlayer)AutoTotem.mc.player);
            AutoTotem.mc.playerController.updateController();
        }
    }
    
    private int getItemSlot() {
        for (int i = 0; i < 36; ++i) {
            final Item item = AutoTotem.mc.player.inventory.getStackInSlot(i).getItem();
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
