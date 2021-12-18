// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.hack.hacks.chat;

import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.Hack;

@RegisterHack(name = "AutoSong", description = "spams chat with lyrics", category = Hack.Category.Chat)
public class AutoSong extends Hack
{
    public void onEnable() {
        if (AutoSong.mc.player != null) {
            AutoSong.mc.player.sendChatMessage("Never gona give you up!");
        }
        AutoSong.mc.player.sendChatMessage("Never gona let you down!");
        AutoSong.mc.player.sendChatMessage("Never gona run around!");
        AutoSong.mc.player.sendChatMessage("And Desert You!");
        AutoSong.mc.player.sendChatMessage("And stay Mad!");
        this.toggle();
    }
}
