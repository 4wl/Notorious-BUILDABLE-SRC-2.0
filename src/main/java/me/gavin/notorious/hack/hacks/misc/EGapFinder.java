// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.hack.hacks.misc;

import net.minecraft.item.ItemStack;
import java.util.Iterator;
import net.minecraft.entity.item.EntityMinecartContainer;
import net.minecraft.entity.Entity;
import net.minecraft.enchantment.Enchantment;
import java.util.Map;
import java.util.ArrayList;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.player.EntityPlayer;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.Hack;

@RegisterHack(name = "EGapFinder", description = "ez", category = Hack.Category.Misc)
public class EGapFinder extends Hack
{
    @RegisterSetting
    public final BooleanSetting egaps;
    @RegisterSetting
    public final BooleanSetting crapples;
    @RegisterSetting
    public final BooleanSetting mending;
    @RegisterSetting
    public final BooleanSetting prot;
    @RegisterSetting
    public final BooleanSetting bprot;
    @RegisterSetting
    public final BooleanSetting feather;
    @RegisterSetting
    public final BooleanSetting unb;
    @RegisterSetting
    public final BooleanSetting effi;
    @RegisterSetting
    public final BooleanSetting sharp;
    @RegisterSetting
    public final BooleanSetting books;
    
    public EGapFinder() {
        this.egaps = new BooleanSetting("EGaps", true);
        this.crapples = new BooleanSetting("Crapples", true);
        this.mending = new BooleanSetting("Mending", true);
        this.prot = new BooleanSetting("Protection", true);
        this.bprot = new BooleanSetting("BProtection", true);
        this.feather = new BooleanSetting("Feather", true);
        this.unb = new BooleanSetting("Unbreaking", true);
        this.effi = new BooleanSetting("Efficiency", true);
        this.sharp = new BooleanSetting("Sharp", true);
        this.books = new BooleanSetting("AllBooks", true);
    }
    
    @Override
    public void onTick() {
        EntityPlayer entityPlayer = null;
        if (!EGapFinder.mc.world.loadedTileEntityList.isEmpty()) {
            entityPlayer = EGapFinder.mc.world.playerEntities.get(0);
            for (final TileEntity tileEntity : EGapFinder.mc.world.loadedTileEntityList) {
                if (tileEntity instanceof TileEntityChest) {
                    final TileEntityChest chest = (TileEntityChest)tileEntity;
                    chest.fillWithLoot(entityPlayer);
                    for (byte b = 0; b < chest.getSizeInventory(); ++b) {
                        final ItemStack itemStack = chest.getStackInSlot((int)b);
                        if (itemStack.getItem() == Items.GOLDEN_APPLE) {
                            if (itemStack.getItemDamage() == 1) {
                                if (this.egaps.isEnabled()) {
                                    this.notorious.messageManager.sendMessage("Dungeon Chest with god gapple at: " + chest.getPos().getX() + " " + chest.getPos().getY() + " " + chest.getPos().getZ());
                                }
                            }
                            else if (itemStack.getItemDamage() != 1 && this.crapples.isEnabled()) {
                                this.notorious.messageManager.sendMessage("Dungeon Chest with crapple at: " + chest.getPos().getX() + " " + chest.getPos().getY() + " " + chest.getPos().getZ());
                            }
                        }
                        if (itemStack.getItem() == Items.ENCHANTED_BOOK && EnchantmentHelper.getEnchantments(itemStack) != null) {
                            final ArrayList<String> arrayList = new ArrayList<String>();
                            for (final Map.Entry entry : EnchantmentHelper.getEnchantments(itemStack).entrySet()) {
                                Enchantment enchantment = (Enchantment)entry.getKey();
                                Integer integer = (Integer)entry.getValue();
                                arrayList.add(enchantment.getTranslatedName((int)integer));
                            }
                            String str = "";
                            for (final String str2 : arrayList) {
                                if (arrayList.indexOf(str2) != arrayList.size() - 1) {
                                    str = String.valueOf(new StringBuilder().append(str).append(str2).append(", "));
                                }
                                else {
                                    str = String.valueOf(new StringBuilder().append(str).append(str2));
                                }
                            }
                            if (this.mending.isEnabled() && str.contains("Mending")) {
                                this.notorious.messageManager.sendMessage("Dungeon Chest with Mending at: " + chest.getPos().getX() + " " + chest.getPos().getY() + " " + chest.getPos().getZ());
                            }
                            if (this.prot.isEnabled() && str.contains("Protection IV")) {
                                this.notorious.messageManager.sendMessage("Dungeon Chest with Prot 4 at: " + chest.getPos().getX() + " " + chest.getPos().getY() + " " + chest.getPos().getZ());
                            }
                            if (this.bprot.isEnabled() && str.contains("Blast Protection IV")) {
                                this.notorious.messageManager.sendMessage("Dungeon Chest with Blast Prot 4 at: " + chest.getPos().getX() + " " + chest.getPos().getY() + " " + chest.getPos().getZ());
                            }
                            if (this.unb.isEnabled() && str.contains("Unbreaking III")) {
                                this.notorious.messageManager.sendMessage("Dungeon Chest with Unbreaking 3 at: " + chest.getPos().getX() + " " + chest.getPos().getY() + " " + chest.getPos().getZ());
                            }
                            if (this.feather.isEnabled() && str.contains("Feather Falling IV")) {
                                this.notorious.messageManager.sendMessage("Dungeon Chest with Feather Falling 4 at: " + chest.getPos().getX() + " " + chest.getPos().getY() + " " + chest.getPos().getZ());
                            }
                            if (this.effi.isEnabled() && (str.contains("Efficiency IV") || str.contains("Efficiency V"))) {
                                this.notorious.messageManager.sendMessage("Dungeon Chest with Efficiency 4/5 at: " + chest.getPos().getX() + " " + chest.getPos().getY() + " " + chest.getPos().getZ());
                            }
                            if (this.sharp.isEnabled() && (str.contains("Sharpness IV") || str.contains("Sharpness V"))) {
                                this.notorious.messageManager.sendMessage("Dungeon Chest with Sharpness 4/5 at: " + chest.getPos().getX() + " " + chest.getPos().getY() + " " + chest.getPos().getZ());
                            }
                            if (this.books.isEnabled()) {
                                this.notorious.messageManager.sendMessage("Dungeon Chest with Enchanted Books at: " + chest.getPos().getX() + " " + chest.getPos().getY() + " " + chest.getPos().getZ());
                            }
                        }
                    }
                }
            }
            for (final Entity entity : EGapFinder.mc.world.loadedEntityList) {
                if (entity instanceof EntityMinecartContainer) {
                    final EntityMinecartContainer minecart = (EntityMinecartContainer)entity;
                    if (minecart.getLootTable() == null) {
                        continue;
                    }
                    minecart.addLoot(entityPlayer);
                    for (byte b = 0; b < minecart.itemHandler.getSlots(); ++b) {
                        final ItemStack itemStack = minecart.itemHandler.getStackInSlot((int)b);
                        if (itemStack.getItem() == Items.GOLDEN_APPLE) {
                            if (itemStack.getItemDamage() == 1) {
                                if (this.egaps.isEnabled()) {
                                    this.notorious.messageManager.sendMessage("Minecart with God Gapple at: " + minecart.posX + " " + minecart.posY + " " + minecart.posZ);
                                }
                            }
                            else if (itemStack.getItemDamage() != 1 && this.crapples.isEnabled()) {
                                this.notorious.messageManager.sendMessage("Minecart with Crapple at: " + minecart.posX + " " + minecart.posY + " " + minecart.posZ);
                            }
                        }
                        if (itemStack.getItem() == Items.ENCHANTED_BOOK && this.books.isEnabled() && EnchantmentHelper.getEnchantments(itemStack) != null) {
                            final ArrayList<String> arrayList = new ArrayList<String>();
                            for (final Map.Entry entry : EnchantmentHelper.getEnchantments(itemStack).entrySet()) {
                                Enchantment enchantment = (Enchantment)entry.getKey();
                                Integer integer = (Integer)entry.getValue();
                                arrayList.add(enchantment.getTranslatedName((int)integer));
                            }
                            String str = "";
                            for (final String str2 : arrayList) {
                                if (arrayList.indexOf(str2) != arrayList.size() - 1) {
                                    str = String.valueOf(new StringBuilder().append(str).append(str2).append(", "));
                                }
                                else {
                                    str = String.valueOf(new StringBuilder().append(str).append(str2));
                                }
                            }
                            if (this.mending.isEnabled() && str.contains("Mending")) {
                                this.notorious.messageManager.sendMessage("Minecart with Mending at: " + minecart.posX + " " + minecart.posY + " " + minecart.posZ);
                            }
                            if (this.prot.isEnabled() && str.contains("Protection IV")) {
                                this.notorious.messageManager.sendMessage("Minecart with Protection 4 at: " + minecart.posX + " " + minecart.posY + " " + minecart.posZ);
                            }
                            if (this.bprot.isEnabled() && str.contains("Blast Protection IV")) {
                                this.notorious.messageManager.sendMessage("Minecart with Blast Protection 4 at: " + minecart.posX + " " + minecart.posY + " " + minecart.posZ);
                            }
                            if (this.unb.isEnabled() && str.contains("Unbreaking III")) {
                                this.notorious.messageManager.sendMessage("Minecart with Unbreaking 3 at: " + minecart.posX + " " + minecart.posY + " " + minecart.posZ);
                            }
                            if (this.feather.isEnabled() && str.contains("Feather Falling IV")) {
                                this.notorious.messageManager.sendMessage("Minecart with Feather Falling 4 at: " + minecart.posX + " " + minecart.posY + " " + minecart.posZ);
                            }
                            if (this.effi.isEnabled() && (str.contains("Efficiency IV") || str.contains("Efficiency V"))) {
                                this.notorious.messageManager.sendMessage("Minecart with Efficiency 4/5 at: " + minecart.posX + " " + minecart.posY + " " + minecart.posZ);
                            }
                            if (this.sharp.isEnabled() && (str.contains("Sharpness IV") || str.contains("Sharpness V"))) {
                                this.notorious.messageManager.sendMessage("Minecart with Sharpness 4/5 at: " + minecart.posX + " " + minecart.posY + " " + minecart.posZ);
                            }
                            if (this.books.isEnabled()) {
                                this.notorious.messageManager.sendMessage("Minecart with Enchanted Book at: " + minecart.posX + " " + minecart.posY + " " + minecart.posZ);
                            }
                        }
                    }
                }
            }
        }
    }
}
