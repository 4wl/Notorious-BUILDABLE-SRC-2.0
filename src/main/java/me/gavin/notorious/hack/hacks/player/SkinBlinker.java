// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.hack.hacks.player;

import net.minecraft.entity.player.EnumPlayerModelParts;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.Hack;

@RegisterHack(name = "SkinBlinker", description = "lmfao, its useless!", category = Hack.Category.Player)
public class SkinBlinker extends Hack
{
    static EnumPlayerModelParts[] PARTS_HORIZONTAL;
    
    @Override
    public void onUpdate() {
        int delay = SkinBlinker.mc.player.ticksExisted % (SkinBlinker.PARTS_HORIZONTAL.length * 2);
        boolean renderLayer = false;
        if (delay >= SkinBlinker.PARTS_HORIZONTAL.length) {
            renderLayer = true;
            delay -= SkinBlinker.PARTS_HORIZONTAL.length;
        }
        SkinBlinker.mc.gameSettings.setModelPartEnabled(SkinBlinker.PARTS_HORIZONTAL[delay], renderLayer);
    }
    
    static {
        SkinBlinker.PARTS_HORIZONTAL = new EnumPlayerModelParts[] { EnumPlayerModelParts.LEFT_SLEEVE, EnumPlayerModelParts.JACKET, EnumPlayerModelParts.HAT, EnumPlayerModelParts.LEFT_PANTS_LEG, EnumPlayerModelParts.RIGHT_PANTS_LEG, EnumPlayerModelParts.RIGHT_SLEEVE };
    }
}
