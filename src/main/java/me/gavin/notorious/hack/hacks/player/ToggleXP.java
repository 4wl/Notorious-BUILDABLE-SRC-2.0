// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.hack.hacks.player;

import net.minecraft.item.Item;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import me.gavin.notorious.mixin.mixins.accessor.IMinecraftMixin;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.init.Items;
import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.Hack;

@RegisterHack(name = "ToggleXP", description = "Ez", category = Hack.Category.Player)
public class ToggleXP extends Hack
{
    @RegisterSetting
    public final BooleanSetting footXP;
    private int serverSlot;
    
    public ToggleXP() {
        this.footXP = new BooleanSetting("FootXP", true);
        this.serverSlot = -1;
    }
    
    public void onDisable() {
        ToggleXP.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(ToggleXP.mc.player.inventory.currentItem));
        this.serverSlot = -1;
    }
    
    @SubscribeEvent
    public void onUpdate(final PlayerLivingUpdateEvent event) {
        final int getSlot = this.getXPSlot();
        if (getSlot != -1) {
            if (this.serverSlot == -1) {
                ToggleXP.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(getSlot));
                this.serverSlot = getSlot;
            }
            else if (ToggleXP.mc.player.inventory.getStackInSlot(this.serverSlot).getItem() != Items.EXPERIENCE_BOTTLE) {
                this.serverSlot = -1;
            }
            else {
                if (this.footXP.isEnabled()) {
                    ToggleXP.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(ToggleXP.mc.player.rotationYaw, 90.0f, ToggleXP.mc.player.onGround));
                }
                ((IMinecraftMixin)ToggleXP.mc).setRightClickDelayTimerAccessor(0);
                ToggleXP.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            }
        }
    }
    
    private int getXPSlot() {
        final Item item = Items.EXPERIENCE_BOTTLE;
        final Minecraft mc = Minecraft.getMinecraft();
        int itemSlot = (mc.player.getHeldItemMainhand().getItem() == item) ? mc.player.inventory.currentItem : -1;
        if (itemSlot == -1) {
            for (int l = 0; l < 9; ++l) {
                if (mc.player.inventory.getStackInSlot(l).getItem() == item) {
                    itemSlot = l;
                }
            }
        }
        return itemSlot;
    }
}
