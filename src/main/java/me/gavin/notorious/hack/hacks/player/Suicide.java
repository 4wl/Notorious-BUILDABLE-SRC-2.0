// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.hack.hacks.player;

import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.Hack;

@RegisterHack(name = "Suicide", description = "Automatically kills you.", category = Hack.Category.Player)
public class Suicide extends Hack
{
    public void onEnable() {
        if (Suicide.mc.player != null) {
            Suicide.mc.player.sendChatMessage("/kill");
        }
        this.toggle();
    }
}
