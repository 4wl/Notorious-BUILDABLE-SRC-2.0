// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.util.rewrite;

import java.util.Iterator;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import java.util.Objects;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.MathHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.util.CombatRules;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.Explosion;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.item.EntityEnderCrystal;
import me.gavin.notorious.stuff.IMinecraft;

public class DamageUtil implements IMinecraft
{
    public static float calculateDamage(final EntityEnderCrystal crystal, final EntityPlayer player) {
        return calculateDamage(crystal.posX, crystal.posY, crystal.posZ, (Entity)player);
    }
    
    public static float calculateDamage(final BlockPos position, final EntityPlayer player) {
        return calculateDamage(position.getX() + 0.5, position.getY() + 1.0, position.getZ() + 0.5, (Entity)player);
    }
    
    public static float calculateDamage(final double posX, final double posY, final double posZ, final Entity entity) {
        final float doubleSize = 12.0f;
        final double size = entity.getDistance(posX, posY, posZ) / doubleSize;
        final Vec3d vec3d = new Vec3d(posX, posY, posZ);
        final double blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        final double value = (1.0 - size) * blockDensity;
        final float damage = (float)(int)((value * value + value) / 2.0 * 7.0 * doubleSize + 1.0);
        double finalDamage = 1.0;
        if (entity instanceof EntityLivingBase) {
            finalDamage = getBlastReduction((EntityLivingBase)entity, getMultipliedDamage(damage), new Explosion((World)DamageUtil.mc.world, (Entity)null, posX, posY, posZ, 6.0f, false, true));
        }
        return (float)finalDamage;
    }
    
    public static float getBlastReduction(final EntityLivingBase entity, float damage, final Explosion explosion) {
        if (entity instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer)entity;
            final DamageSource source = DamageSource.causeExplosionDamage(explosion);
            damage = CombatRules.getDamageAfterAbsorb(damage, (float)player.getTotalArmorValue(), (float)player.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            damage *= 1.0f - MathHelper.clamp((float)EnchantmentHelper.getEnchantmentModifierDamage(player.getArmorInventoryList(), source), 0.0f, 20.0f) / 25.0f;
            if (entity.isPotionActive((Potion)Objects.requireNonNull(Potion.getPotionById(11)))) {
                damage -= damage / 4.0f;
            }
            return damage;
        }
        damage = CombatRules.getDamageAfterAbsorb(damage, (float)entity.getTotalArmorValue(), (float)entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        return damage;
    }
    
    private static float getMultipliedDamage(final float damage) {
        return damage * ((DamageUtil.mc.world.getDifficulty().getId() == 0) ? 0.0f : ((DamageUtil.mc.world.getDifficulty().getId() == 2) ? 1.0f : ((DamageUtil.mc.world.getDifficulty().getId() == 1) ? 0.5f : 1.5f)));
    }
    
    public static boolean shouldBreakArmor(final EntityPlayer player, final float targetPercent) {
        for (final ItemStack stack : player.getArmorInventoryList()) {
            if (stack == null || stack.getItem() == Items.AIR) {
                return true;
            }
            final float armorPercent = (stack.getMaxDamage() - stack.getItemDamage()) / (float)stack.getMaxDamage() * 100.0f;
            if (targetPercent >= armorPercent && stack.getCount() < 2) {
                return true;
            }
        }
        return false;
    }
    
    public static float getDamageInPercent(final ItemStack stack) {
        final float green = (stack.getMaxDamage() - (float)stack.getItemDamage()) / stack.getMaxDamage();
        final float red = 1.0f - green;
        return (float)(100 - (int)(red * 100.0f));
    }
    
    public static int getRoundedDamage(final ItemStack stack) {
        return (int)getDamageInPercent(stack);
    }
}
