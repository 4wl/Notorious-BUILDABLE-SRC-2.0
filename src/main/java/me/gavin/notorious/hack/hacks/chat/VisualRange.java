// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.hack.hacks.chat;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.Entity;
import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import java.util.ArrayList;
import java.util.List;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.Hack;

@RegisterHack(name = "VisualRange", description = "Sends a message in chat when a player enters your range.", category = Hack.Category.Chat)
public class VisualRange extends Hack
{
    private List<String> peopleInArea;
    private List<String> peopleNearbyNew;
    private List<String> peopleToRemove;
    
    public void onEnable() {
        this.peopleInArea = new ArrayList<String>();
        this.peopleToRemove = new ArrayList<String>();
    }
    
    @SubscribeEvent
    public void onUpdate(final PlayerLivingUpdateEvent event) {
        this.peopleNearbyNew = new ArrayList<String>();
        final List<EntityPlayer> playerEntities = (List<EntityPlayer>)VisualRange.mc.world.playerEntities;
        for (final Entity e : playerEntities) {
            if (e.getName().equals(VisualRange.mc.player.getName())) {
                continue;
            }
            this.peopleNearbyNew.add(e.getName());
        }
        if (this.peopleNearbyNew.size() > 0) {
            for (final String name : this.peopleNearbyNew) {
                if (!this.peopleInArea.contains(name)) {
                    this.notorious.messageManager.sendMessage(ChatFormatting.RED + name + ChatFormatting.RESET + " has entered your Visual Range");
                    this.peopleInArea.add(name);
                }
            }
        }
        if (this.peopleInArea.size() > 0) {
            for (final String name : this.peopleInArea) {
                if (!this.peopleNearbyNew.contains(name)) {
                    this.peopleToRemove.add(name);
                    this.notorious.messageManager.sendMessage(ChatFormatting.RED + name + ChatFormatting.RESET + " has left your Visual Range");
                }
            }
            if (this.peopleToRemove.size() > 0) {
                for (final String name : this.peopleToRemove) {
                    this.peopleInArea.remove(name);
                }
                this.peopleToRemove.clear();
            }
        }
    }
}
