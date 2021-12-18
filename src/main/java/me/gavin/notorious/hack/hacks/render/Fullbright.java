// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.hack.hacks.render;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.Hack;

@RegisterHack(name = "Fullbright", description = "Makes it fully bright", category = Hack.Category.Render)
public class Fullbright extends Hack
{
    private final List<Float> previousLevels;
    
    public Fullbright() {
        this.previousLevels = new ArrayList<Float>();
    }
    
    public void onEnable() {
        if (Fullbright.mc.player != null && Fullbright.mc.world != null) {
            final float[] table = Fullbright.mc.world.provider.getLightBrightnessTable();
            if (Fullbright.mc.world.provider != null) {
                for (final float f : table) {
                    this.previousLevels.add(f);
                }
                Arrays.fill(table, 1.0f);
            }
        }
        else {
            this.toggle();
        }
    }
    
    public void onDisable() {
        if (Fullbright.mc.player != null && Fullbright.mc.world != null) {
            final float[] table = Fullbright.mc.world.provider.getLightBrightnessTable();
            for (int i = 0; i < table.length; ++i) {
                table[i] = this.previousLevels.get(i);
            }
            this.previousLevels.clear();
        }
    }
}
