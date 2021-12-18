// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.hack.hacks.misc;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import net.minecraft.init.SoundEvents;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraftforge.event.entity.living.LivingEvent;
import java.util.HashSet;
import net.minecraft.entity.Entity;
import java.util.Set;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.Hack;

@RegisterHack(name = "GhastNotifier", description = "", category = Hack.Category.Misc)
public class GhastNotifier extends Hack
{
    @RegisterSetting
    public final BooleanSetting playSound;
    @RegisterSetting
    public final BooleanSetting glow;
    private Set<Entity> ghasts;
    
    public GhastNotifier() {
        this.playSound = new BooleanSetting("PlaySound", true);
        this.glow = new BooleanSetting("Glow", true);
        this.ghasts = new HashSet<Entity>();
    }
    
    public void onEnable() {
        if (!this.ghasts.isEmpty()) {
            this.ghasts.clear();
        }
    }
    
    @SubscribeEvent
    public void onUpdate(final LivingEvent.LivingUpdateEvent event) {
        for (final Entity e : GhastNotifier.mc.world.loadedEntityList) {
            if (e instanceof EntityGhast) {
                if (this.ghasts.contains(e)) {
                    continue;
                }
                this.notorious.messageManager.sendMessage("Ghast detected at X: " + ChatFormatting.RED + ChatFormatting.BOLD + e.getPosition().getX() + ChatFormatting.RESET + " Y: " + ChatFormatting.RED + ChatFormatting.BOLD + e.getPosition().getY() + ChatFormatting.RESET + " Z: " + ChatFormatting.RED + ChatFormatting.BOLD + e.getPosition().getZ() + ChatFormatting.RESET + "!");
                this.ghasts.add(e);
                if (this.playSound.isEnabled()) {
                    GhastNotifier.mc.player.playSound(SoundEvents.BLOCK_ANVIL_DESTROY, 1.0f, 1.0f);
                }
                if (!this.glow.isEnabled()) {
                    continue;
                }
                e.setGlowing(true);
            }
        }
    }
}
