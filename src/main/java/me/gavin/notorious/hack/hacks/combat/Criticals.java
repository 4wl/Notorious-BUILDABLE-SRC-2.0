// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.hack.hacks.combat;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.world.World;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.network.play.client.CPacketUseEntity;
import me.gavin.notorious.event.events.PacketEvent;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.ModeSetting;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.Hack;

@RegisterHack(name = "Criticals", description = "Makes every one of your hits a critical.", category = Hack.Category.Combat)
public class Criticals extends Hack
{
    @RegisterSetting
    public final ModeSetting mode;
    
    public Criticals() {
        this.mode = new ModeSetting("Mode", "Packet", new String[] { "Packet", "FakeJump", "Jump", "MiniJump" });
    }
    
    @Override
    public String getMetaData() {
        return " [" + ChatFormatting.GRAY + this.mode.getMode() + ChatFormatting.RESET + "]";
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketUseEntity) {
            final CPacketUseEntity packet = (CPacketUseEntity)event.getPacket();
            if (packet.getAction() == CPacketUseEntity.Action.ATTACK && !(packet.getEntityFromWorld((World)Criticals.mc.world) instanceof EntityEnderCrystal) && !AutoCrystal.isPredicting) {
                if (!Criticals.mc.player.onGround) {
                    return;
                }
                if (Criticals.mc.player.isInWater() || Criticals.mc.player.isInLava()) {
                    return;
                }
                if (this.mode.getMode().equals("Packet")) {
                    Criticals.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 0.0625, Criticals.mc.player.posZ, true));
                    Criticals.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
                    Criticals.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 1.1E-5, Criticals.mc.player.posZ, false));
                    Criticals.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
                }
                else if (this.mode.getMode().equals("FakeJump")) {
                    Criticals.mc.player.motionY = 0.10000000149011612;
                    Criticals.mc.player.fallDistance = 0.1f;
                    Criticals.mc.player.onGround = false;
                }
                else {
                    Criticals.mc.player.jump();
                    if (this.mode.getMode().equals("MiniJump")) {
                        final EntityPlayerSP player = Criticals.mc.player;
                        player.motionY /= 2.0;
                    }
                }
            }
        }
    }
}
