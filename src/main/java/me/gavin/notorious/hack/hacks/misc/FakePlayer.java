// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.hack.hacks.misc;

import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.util.math.MathHelper;
import me.gavin.notorious.util.rewrite.DamageUtil;
import net.minecraft.network.play.server.SPacketExplosion;
import me.gavin.notorious.event.events.PacketEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.setting.ModeSetting;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.Hack;

@RegisterHack(name = "FakePlayer", description = "Spawns a fake player", category = Hack.Category.Misc)
public class FakePlayer extends Hack
{
    @RegisterSetting
    private final BooleanSetting pops;
    @RegisterSetting
    private final BooleanSetting totemPopSound;
    @RegisterSetting
    private final BooleanSetting totemPopParticle;
    @RegisterSetting
    private final BooleanSetting move;
    @RegisterSetting
    private final ModeSetting mode;
    @RegisterSetting
    private final NumSetting chaseX;
    @RegisterSetting
    private final NumSetting chaseY;
    @RegisterSetting
    private final NumSetting chaseZ;
    public EntityOtherPlayerMP fakePlayer;
    
    public FakePlayer() {
        this.pops = new BooleanSetting("TotemPops", true);
        this.totemPopSound = new BooleanSetting("TotemPopSound", true);
        this.totemPopParticle = new BooleanSetting("TotemPopParticle", true);
        this.move = new BooleanSetting("Move", true);
        this.mode = new ModeSetting("MovementMode", "Static", new String[] { "Static", "Chase" });
        this.chaseX = new NumSetting("ChaseX", 4.0f, 1.0f, 120.0f, 0.1f);
        this.chaseY = new NumSetting("ChaseY", 2.0f, 1.0f, 120.0f, 0.1f);
        this.chaseZ = new NumSetting("ChaseZ", 2.0f, 1.0f, 120.0f, 0.1f);
    }
    
    @Override
    public String getMetaData() {
        return " [" + ChatFormatting.GRAY + this.mode.getMode() + ChatFormatting.RESET + "]";
    }
    
    @Override
    protected void onEnable() {
        if (FakePlayer.mc.world == null || FakePlayer.mc.player == null) {
            this.disable();
        }
        else {
            final UUID playerUUID = FakePlayer.mc.player.getUniqueID();
            (this.fakePlayer = new EntityOtherPlayerMP((World)FakePlayer.mc.world, new GameProfile(UUID.fromString(playerUUID.toString()), FakePlayer.mc.player.getDisplayNameString()))).copyLocationAndAnglesFrom((Entity)FakePlayer.mc.player);
            this.fakePlayer.inventory.copyInventory(FakePlayer.mc.player.inventory);
            FakePlayer.mc.world.addEntityToWorld(-7777, (Entity)this.fakePlayer);
            this.notorious.messageManager.sendMessage("Added a " + ChatFormatting.GREEN + "fake player" + ChatFormatting.RESET + " to your world.");
        }
    }
    
    @SubscribeEvent
    public void onTick(final PlayerLivingUpdateEvent event) {
        if (this.pops.getValue() && this.fakePlayer != null) {
            fakePlayer.inventory.offHandInventory.set(0, new ItemStack(Items.TOTEM_OF_UNDYING));
            if (this.fakePlayer.getHealth() <= 0.0f) {
                this.fakePop((Entity)this.fakePlayer);
                this.fakePlayer.setHealth(20.0f);
                this.notorious.popListener.handlePop((EntityPlayer)this.fakePlayer);
            }
        }
        if (this.move.isEnabled() && !this.mode.getMode().equals("Chase")) {
            this.travel(this.fakePlayer.moveStrafing, this.fakePlayer.moveVertical, this.fakePlayer.moveForward);
        }
    }
    
    @SubscribeEvent
    public void onRender(final RenderWorldLastEvent event) {
        if (this.mode.getMode().equals("Chase")) {
            this.fakePlayer.posX = FakePlayer.mc.player.posX + this.chaseX.getValue();
            this.fakePlayer.posY = this.chaseY.getValue();
            this.fakePlayer.posZ = FakePlayer.mc.player.posZ + this.chaseZ.getValue();
        }
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (this.fakePlayer == null) {
            return;
        }
        if (event.getPacket() instanceof SPacketExplosion) {
            final SPacketExplosion explosion = (SPacketExplosion)event.getPacket();
            if (this.fakePlayer.getDistance(explosion.getX(), explosion.getY(), explosion.getZ()) <= 15.0) {
                final double damage = DamageUtil.calculateDamage(explosion.getX(), explosion.getY(), explosion.getZ(), (Entity)this.fakePlayer);
                if (damage > 0.0 && this.pops.isEnabled()) {
                    this.fakePlayer.setHealth((float)(this.fakePlayer.getHealth() - MathHelper.clamp(damage, 0.0, 999.0)));
                }
            }
        }
    }
    
    public void travel(final float strafe, final float vertical, final float forward) {
        final double d0 = this.fakePlayer.posY;
        float f1 = 0.8f;
        float f2 = 0.02f;
        float f3 = (float)EnchantmentHelper.getDepthStriderModifier((EntityLivingBase)this.fakePlayer);
        if (f3 > 3.0f) {
            f3 = 3.0f;
        }
        if (!this.fakePlayer.onGround) {
            f3 *= 0.5f;
        }
        if (f3 > 0.0f) {
            f1 += (0.54600006f - f1) * f3 / 3.0f;
            f2 += (this.fakePlayer.getAIMoveSpeed() - f2) * f3 / 4.0f;
        }
        this.fakePlayer.moveRelative(strafe, vertical, forward, f2);
        this.fakePlayer.move(MoverType.SELF, this.fakePlayer.motionX, this.fakePlayer.motionY, this.fakePlayer.motionZ);
        final EntityOtherPlayerMP fakePlayer = this.fakePlayer;
        fakePlayer.motionX *= f1;
        final EntityOtherPlayerMP fakePlayer2 = this.fakePlayer;
        fakePlayer2.motionY *= 0.800000011920929;
        final EntityOtherPlayerMP fakePlayer3 = this.fakePlayer;
        fakePlayer3.motionZ *= f1;
        if (!this.fakePlayer.hasNoGravity()) {
            final EntityOtherPlayerMP fakePlayer4 = this.fakePlayer;
            fakePlayer4.motionY -= 0.02;
        }
        if (this.fakePlayer.collidedHorizontally && this.fakePlayer.isOffsetPositionInLiquid(this.fakePlayer.motionX, this.fakePlayer.motionY + 0.6000000238418579 - this.fakePlayer.posY + d0, this.fakePlayer.motionZ)) {
            this.fakePlayer.motionY = 0.30000001192092896;
        }
    }
    
    @Override
    protected void onDisable() {
        if (this.fakePlayer != null && FakePlayer.mc.world != null) {
            FakePlayer.mc.world.removeEntityFromWorld(-7777);
            this.notorious.messageManager.sendMessage("Removed a " + ChatFormatting.RED + "fake player" + ChatFormatting.RESET + " from your world.");
            this.fakePlayer = null;
        }
    }
    
    private void fakePop(final Entity entity) {
        if (this.totemPopParticle.isEnabled()) {
            FakePlayer.mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.TOTEM, 30);
        }
        if (this.totemPopSound.isEnabled()) {
            FakePlayer.mc.world.playSound(entity.posX, entity.posY, entity.posZ, SoundEvents.ITEM_TOTEM_USE, entity.getSoundCategory(), 1.0f, 1.0f, false);
        }
    }
}
