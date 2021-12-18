// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.hack.hacks.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.util.DiscordUtil;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.Hack;

@RegisterHack(name = "DiscordRPC", description = "ez", category = Hack.Category.Client)
public class DiscordRPC extends Hack
{
    public void onEnable() {
        if (DiscordRPC.mc.world != null && DiscordRPC.mc.player != null) {
            DiscordUtil.startRPC();
            this.notorious.messageManager.sendMessage("Starting " + ChatFormatting.GREEN + ChatFormatting.BOLD + "RPC" + ChatFormatting.RESET + "!");
        }
        else {
            this.toggle();
        }
    }
    
    public void onDisable() {
        if (DiscordRPC.mc.world != null && DiscordRPC.mc.player != null) {
            DiscordUtil.stopRPC();
            this.notorious.messageManager.sendMessage("Stopping " + ChatFormatting.RED + ChatFormatting.BOLD + "RPC" + ChatFormatting.RESET + "!");
        }
    }
}
