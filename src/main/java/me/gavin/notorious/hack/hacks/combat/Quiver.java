// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.hack.hacks.combat;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.item.ItemBow;
import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.Hack;

@RegisterHack(name = "Quiver", description = "Automatically places a totem in your offhand.", category = Hack.Category.Combat)
public class Quiver extends Hack
{
    @RegisterSetting
    public final NumSetting tickDelay;
    
    public Quiver() {
        this.tickDelay = new NumSetting("HoldTime", 3.0f, 0.0f, 8.0f, 0.5f);
    }
    
    @Override
    public String getMetaData() {
        return " [" + ChatFormatting.GRAY + this.tickDelay.getValue() + ChatFormatting.RESET + "]";
    }
    
    @SubscribeEvent
    public void onUpdate(final PlayerLivingUpdateEvent event) {
        if (Quiver.mc.player.getHeldItemMainhand().getItem() instanceof ItemBow && Quiver.mc.player.getItemInUseMaxCount() >= this.tickDelay.getValue()) {
            Quiver.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(Quiver.mc.player.cameraYaw, -90.0f, true));
            Quiver.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem());
        }
    }
}
