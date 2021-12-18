// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.util;

import java.util.Iterator;
import me.gavin.notorious.hack.hacks.chat.PopCounter;
import me.gavin.notorious.hack.hacks.misc.FakePlayer;
import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import me.gavin.notorious.event.events.TotemPopEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.network.play.server.SPacketEntityStatus;
import me.gavin.notorious.event.events.PacketEvent;
import net.minecraftforge.common.MinecraftForge;
import java.util.HashMap;
import me.gavin.notorious.Notorious;
import java.util.Map;
import me.gavin.notorious.stuff.IMinecraft;

public class TotemPopListener implements IMinecraft
{
    public final Map<String, Integer> popMap;
    private final Notorious notorious;
    
    public TotemPopListener() {
        this.popMap = new HashMap<String, Integer>();
        this.notorious = Notorious.INSTANCE;
        MinecraftForge.EVENT_BUS.register((Object)this);
    }
    
    @SubscribeEvent
    public void onPacket(final PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketEntityStatus) {
            final SPacketEntityStatus packet = (SPacketEntityStatus)event.getPacket();
            if (packet.getOpCode() == 35) {
                final Entity entity = packet.getEntity((World)TotemPopListener.mc.world);
                if (entity instanceof EntityPlayer) {
                    if (entity.equals((Object)TotemPopListener.mc.player)) {
                        return;
                    }
                    final EntityPlayer player = (EntityPlayer)entity;
                    this.handlePop(player);
                }
            }
        }
    }
    
    public void handlePop(final EntityPlayer player) {
        if (!this.popMap.containsKey(player.getName())) {
            MinecraftForge.EVENT_BUS.post((Event)new TotemPopEvent(player.getName(), 1, player.getEntityId()));
            this.popMap.put(player.getName(), 1);
        }
        else {
            this.popMap.put(player.getName(), this.popMap.get(player.getName()) + 1);
            MinecraftForge.EVENT_BUS.post((Event)new TotemPopEvent(player.getName(), this.popMap.get(player.getName()), player.getEntityId()));
        }
    }
    
    @SubscribeEvent
    public void onTick(final PlayerLivingUpdateEvent event) {
        for (final EntityPlayer player : TotemPopListener.mc.world.playerEntities) {
            if (player == this.notorious.hackManager.getHack(FakePlayer.class).fakePlayer) {
                continue;
            }
            if (player == TotemPopListener.mc.player || !this.popMap.containsKey(player.getName()) || (!player.isDead && player.isEntityAlive() && player.getHealth() > 0.0f)) {
                continue;
            }
            this.notorious.hackManager.getHack(PopCounter.class).onDeath(player.getName(), this.popMap.get(player.getName()), player.getEntityId());
            this.popMap.remove(player.getName());
        }
    }
}
