// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.hack.hacks.combat;

import net.minecraft.util.EnumHand;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.SettingGroup;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.Hack;

@RegisterHack(name = "KillAura", description = "Attacks entities for you", category = Hack.Category.Combat)
public class KillAura extends Hack
{
    public final SettingGroup targets;
    @RegisterSetting
    public final BooleanSetting attackDelay;
    @RegisterSetting
    public final BooleanSetting players;
    @RegisterSetting
    public final BooleanSetting animals;
    @RegisterSetting
    public final BooleanSetting mobs;
    @RegisterSetting
    public final NumSetting attackSpeed;
    @RegisterSetting
    public final NumSetting range;
    
    public KillAura() {
        this.targets = new SettingGroup("Targets");
        this.attackDelay = new BooleanSetting("1.9 Delay", true);
        this.players = new BooleanSetting("Players", true);
        this.animals = new BooleanSetting("Animals", false);
        this.mobs = new BooleanSetting("Mobs", false);
        this.attackSpeed = new NumSetting("Attack Speed", 10.0f, 2.0f, 18.0f, 1.0f);
        this.range = new NumSetting("Range", 4.0f, 1.0f, 6.0f, 0.25f);
        this.players.setGroup(this.targets);
        this.animals.setGroup(this.targets);
        this.mobs.setGroup(this.targets);
    }
    
    @Override
    public String getMetaData() {
        return " [" + ChatFormatting.GRAY + this.range.getValue() + ChatFormatting.RESET + "]";
    }
    
    @SubscribeEvent
    public void onLivingUpdate(final PlayerLivingUpdateEvent event) {
        for (final Entity entity : KillAura.mc.world.loadedEntityList) {
            if (entity.equals((Object)KillAura.mc.player)) {
                continue;
            }
            if (entity instanceof EntityPlayer && this.players.isEnabled()) {
                this.attack(entity);
            }
            if (entity instanceof EntityAnimal && this.animals.isEnabled()) {
                this.attack(entity);
            }
            if (!(entity instanceof EntityMob) || !this.mobs.isEnabled()) {
                continue;
            }
            this.attack(entity);
        }
    }
    
    private void attack(final Entity entity) {
        if (this.shouldAttack((EntityLivingBase)entity)) {
            if (this.attackDelay.isEnabled()) {
                if (KillAura.mc.player.getCooledAttackStrength(0.0f) >= 1.0f) {
                    KillAura.mc.playerController.attackEntity((EntityPlayer)KillAura.mc.player, entity);
                    KillAura.mc.player.swingArm(EnumHand.MAIN_HAND);
                }
            }
            else if (KillAura.mc.player.ticksExisted % this.attackSpeed.getValue() == 0.0) {
                KillAura.mc.playerController.attackEntity((EntityPlayer)KillAura.mc.player, entity);
                KillAura.mc.player.swingArm(EnumHand.MAIN_HAND);
            }
        }
    }
    
    private boolean shouldAttack(final EntityLivingBase entity) {
        return entity.getDistance((Entity)KillAura.mc.player) <= this.range.getValue() && entity.getHealth() > 0.0f;
    }
}
