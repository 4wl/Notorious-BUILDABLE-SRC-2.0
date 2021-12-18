// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.hack.hacks.player;

import net.minecraft.item.Item;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import me.gavin.notorious.mixin.mixins.accessor.IMinecraftMixin;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.init.Items;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import org.lwjgl.input.Mouse;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.Hack;

@RegisterHack(name = "MiddleClick", description = "ez", category = Hack.Category.Player)
public class MiddleClick extends Hack
{
    @RegisterSetting
    public final BooleanSetting friend;
    @RegisterSetting
    public final BooleanSetting xp;
    @RegisterSetting
    public final BooleanSetting footXP;
    @RegisterSetting
    public final BooleanSetting pearl;
    private boolean mouseHolding;
    private int serverSlot;
    
    public MiddleClick() {
        this.friend = new BooleanSetting("Friend", false);
        this.xp = new BooleanSetting("SilentXP", true);
        this.footXP = new BooleanSetting("FootXP", true);
        this.pearl = new BooleanSetting("Pearl", false);
        this.mouseHolding = false;
        this.serverSlot = -1;
    }
    
    @SubscribeEvent
    public void onMouse(final InputEvent.MouseInputEvent event) {
        if (Mouse.getEventButton() == 2) {
            if (Mouse.getEventButtonState()) {
                this.mouseHolding = true;
            }
            else {
                this.mouseHolding = false;
                if (this.xp.isEnabled()) {
                    MiddleClick.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(MiddleClick.mc.player.inventory.currentItem));
                }
                this.serverSlot = -1;
            }
        }
    }
    
    @SubscribeEvent
    public void onMouse(final PlayerLivingUpdateEvent event) {
        if (this.mouseHolding) {
            if (this.friend.isEnabled()) {
                this.mouseHolding = false;
                final RayTraceResult result = MiddleClick.mc.objectMouseOver;
                if (result == null || result.typeOfHit != RayTraceResult.Type.ENTITY || !(result.entityHit instanceof EntityPlayer)) {
                    return;
                }
                if (this.notorious.friend.isFriend(MiddleClick.mc.objectMouseOver.entityHit.getName())) {
                    this.notorious.friend.delFriend(MiddleClick.mc.objectMouseOver.entityHit.getName());
                    this.notorious.messageManager.sendMessage(ChatFormatting.RED + "Removed " + ChatFormatting.LIGHT_PURPLE + MiddleClick.mc.objectMouseOver.entityHit.getName() + ChatFormatting.WHITE + " from friends list");
                }
                else {
                    this.notorious.friend.addFriend(MiddleClick.mc.objectMouseOver.entityHit.getName());
                    this.notorious.messageManager.sendMessage(ChatFormatting.GREEN + "Added " + ChatFormatting.LIGHT_PURPLE + MiddleClick.mc.objectMouseOver.entityHit.getName() + ChatFormatting.WHITE + " to friends list");
                }
            }
            if (this.xp.isEnabled()) {
                final int getSlot = this.getXPSlot();
                if (getSlot != -1) {
                    if (this.serverSlot == -1) {
                        MiddleClick.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(getSlot));
                        this.serverSlot = getSlot;
                    }
                    else if (MiddleClick.mc.player.inventory.getStackInSlot(this.serverSlot).getItem() != Items.EXPERIENCE_BOTTLE) {
                        this.serverSlot = -1;
                    }
                    else {
                        if (this.footXP.isEnabled()) {
                            MiddleClick.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(MiddleClick.mc.player.rotationYaw, 90.0f, MiddleClick.mc.player.onGround));
                        }
                        ((IMinecraftMixin)MiddleClick.mc).setRightClickDelayTimerAccessor(0);
                        MiddleClick.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                    }
                }
            }
            if (this.pearl.isEnabled()) {
                for (int i = 0; i < 9; ++i) {
                    this.mouseHolding = false;
                    final ItemStack itemStack = MiddleClick.mc.player.inventory.getStackInSlot(i);
                    if (itemStack.getItem() == Items.ENDER_PEARL) {
                        MiddleClick.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(i));
                        MiddleClick.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                        MiddleClick.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(MiddleClick.mc.player.inventory.currentItem));
                    }
                }
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
