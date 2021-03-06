// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.hack.hacks.combat;

import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.network.play.server.SPacketSoundEffect;
import me.gavin.notorious.mixin.mixins.accessor.ICPacketUseEntityMixin;
import net.minecraft.network.play.server.SPacketSpawnObject;
import me.gavin.notorious.event.events.PacketEvent;
import net.minecraft.util.math.AxisAlignedBB;
import me.gavin.notorious.util.RenderUtil;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import me.gavin.notorious.util.rewrite.InventoryUtil;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.Blocks;
import net.minecraft.util.NonNullList;
import java.util.Iterator;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketUseEntity;
import me.gavin.notorious.util.rewrite.DamageUtil;
import me.gavin.notorious.util.MathUtil;
import java.util.Collection;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.ModeSetting;
import me.gavin.notorious.util.TickTimer;
import java.util.List;
import net.minecraft.entity.item.EntityEnderCrystal;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.player.EntityPlayer;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.Hack;

@RegisterHack(name = "AutoCrystal", description = "Automatically places and breaks crystals to destroy your enemies.", category = Hack.Category.Combat)
public class AutoCrystal extends Hack
{
    public static EntityPlayer target;
    public static boolean isPredicting;
    private BlockPos renderPosition;
    private final ConcurrentHashMap<EntityEnderCrystal, Integer> attackedCrystals;
    private final List<BlockPos> placedCrystals;
    private final TickTimer clearTimer;
    private int hitTicks;
    private int placeTicks;
    @RegisterSetting
    public final ModeSetting hit;
    @RegisterSetting
    public final NumSetting hitDelay;
    @RegisterSetting
    public final NumSetting hitRange;
    @RegisterSetting
    public final NumSetting hitWallsRange;
    @RegisterSetting
    public final NumSetting iterations;
    @RegisterSetting
    public final ModeSetting antiWeakness;
    @RegisterSetting
    public final BooleanSetting predict;
    @RegisterSetting
    public final BooleanSetting antiDesync;
    @RegisterSetting
    public final BooleanSetting antiStuck;
    @RegisterSetting
    public final NumSetting stuckAttempts;
    @RegisterSetting
    public final BooleanSetting packetHit;
    @RegisterSetting
    public final BooleanSetting place;
    @RegisterSetting
    public final NumSetting placeDelay;
    @RegisterSetting
    public final NumSetting placeRange;
    @RegisterSetting
    public final NumSetting placeWallsRange;
    @RegisterSetting
    public final BooleanSetting placeUnderBlock;
    @RegisterSetting
    public final BooleanSetting weaknessCheck;
    @RegisterSetting
    public final BooleanSetting multiPlace;
    @RegisterSetting
    public final BooleanSetting placeSwing;
    @RegisterSetting
    public final ModeSetting autoSwitch;
    @RegisterSetting
    public final ModeSetting timing;
    @RegisterSetting
    public final BooleanSetting rotation;
    @RegisterSetting
    public final BooleanSetting raytrace;
    @RegisterSetting
    public final BooleanSetting antiSuicide;
    @RegisterSetting
    public final NumSetting targetRange;
    @RegisterSetting
    public final ModeSetting swing;
    @RegisterSetting
    public final BooleanSetting silentSwing;
    @RegisterSetting
    public final NumSetting lethalMultiplier;
    @RegisterSetting
    public final NumSetting minimumDamage;
    @RegisterSetting
    public final BooleanSetting ignoreSelfDamage;
    @RegisterSetting
    public final NumSetting maxSelfDamage;
    @RegisterSetting
    public final BooleanSetting facePlace;
    @RegisterSetting
    public final BooleanSetting swordCheck;
    @RegisterSetting
    public final NumSetting facePlaceHealth;
    @RegisterSetting
    public final BooleanSetting armorBreaker;
    @RegisterSetting
    public final NumSetting armorPercent;
    @RegisterSetting
    public final BooleanSetting armorCheck;
    @RegisterSetting
    public final NumSetting armorCheckPercent;
    @RegisterSetting
    public final ModeSetting renderMode;
    @RegisterSetting
    public final ColorSetting fillColor;
    @RegisterSetting
    public final ColorSetting outlineColor;
    
    public AutoCrystal() {
        this.attackedCrystals = new ConcurrentHashMap<EntityEnderCrystal, Integer>();
        this.placedCrystals = new ArrayList<BlockPos>();
        this.clearTimer = new TickTimer();
        this.hit = new ModeSetting("Hit", "Smart", new String[] { "None", "All", "OnlyOwn", "Smart" });
        this.hitDelay = new NumSetting("HitDelay", 1.0f, 0.0f, 20.0f, 1.0f);
        this.hitRange = new NumSetting("HitRange", 5.0f, 0.0f, 6.0f, 0.25f);
        this.hitWallsRange = new NumSetting("HitWallsRange", 3.5f, 0.0f, 6.0f, 0.25f);
        this.iterations = new NumSetting("Iterations", 1.0f, 1.0f, 10.0f, 1.0f);
        this.antiWeakness = new ModeSetting("AntiWeakness", "None", new String[] { "None", "Normal", "Silent" });
        this.predict = new BooleanSetting("Predict", true);
        this.antiDesync = new BooleanSetting("AntiDesync", true);
        this.antiStuck = new BooleanSetting("AntiStuck", true);
        this.stuckAttempts = new NumSetting("StuckAttempts", 4.0f, 1.0f, 20.0f, 1.0f);
        this.packetHit = new BooleanSetting("PacketHit", false);
        this.place = new BooleanSetting("Place", true);
        this.placeDelay = new NumSetting("PlaceDelay", 0.0f, 0.0f, 20.0f, 1.0f);
        this.placeRange = new NumSetting("PlaceRange", 5.0f, 0.0f, 6.0f, 0.25f);
        this.placeWallsRange = new NumSetting("PlaceWallsRange", 3.5f, 0.0f, 6.0f, 0.25f);
        this.placeUnderBlock = new BooleanSetting("PlaceUnderBlock", false);
        this.weaknessCheck = new BooleanSetting("WeaknessCheck", true);
        this.multiPlace = new BooleanSetting("MultiPlace", true);
        this.placeSwing = new BooleanSetting("PlaceSwing", false);
        this.autoSwitch = new ModeSetting("Switch", "None", new String[] { "None", "Normal", "Silent" });
        this.timing = new ModeSetting("Timing", "Break", new String[] { "Break", "Place" });
        this.rotation = new BooleanSetting("Rotation", false);
        this.raytrace = new BooleanSetting("Raytrace", false);
        this.antiSuicide = new BooleanSetting("AntiSuicide", true);
        this.targetRange = new NumSetting("TargetRange", 15.0f, 0.0f, 30.0f, 0.5f);
        this.swing = new ModeSetting("Swing", "Mainhand", new String[] { "None", "Mainhand", "Offhand", "Both" });
        this.silentSwing = new BooleanSetting("SilentSwing", false);
        this.lethalMultiplier = new NumSetting("LethalMultiplier", 1.0f, 0.5f, 3.0f, 0.5f);
        this.minimumDamage = new NumSetting("MinimumDamage", 5.0f, 0.0f, 36.0f, 0.25f);
        this.ignoreSelfDamage = new BooleanSetting("IgnoreSelfDamage", false);
        this.maxSelfDamage = new NumSetting("MaxSelfDamage", 7.0f, 0.0f, 36.0f, 0.25f);
        this.facePlace = new BooleanSetting("FacePlace", true);
        this.swordCheck = new BooleanSetting("SwordCheck", true);
        this.facePlaceHealth = new NumSetting("FacePlaceHealth", 12.0f, 0.0f, 36.0f, 0.5f);
        this.armorBreaker = new BooleanSetting("ArmorBreaker", true);
        this.armorPercent = new NumSetting("ArmorPercent", 20.0f, 1.0f, 100.0f, 1.0f);
        this.armorCheck = new BooleanSetting("ArmorCheck", true);
        this.armorCheckPercent = new NumSetting("ArmorCheckPercent", 20.0f, 1.0f, 100.0f, 1.0f);
        this.renderMode = new ModeSetting("RenderMode", "Both", new String[] { "None", "Fill", "Outline", "Both" });
        this.fillColor = new ColorSetting("FillColor", 255, 255, 255, 255);
        this.outlineColor = new ColorSetting("OutlineColor", 255, 255, 255, 255);
    }
    
    @Override
    public String getMetaData() {
        if (AutoCrystal.target != null) {
            return " [" + ChatFormatting.GRAY + AutoCrystal.target.getDisplayNameString() + ChatFormatting.RESET + "]";
        }
        return "";
    }
    
    @SubscribeEvent
    public void onUpdate(final PlayerLivingUpdateEvent event) {
        if (AutoCrystal.mc.player == null || AutoCrystal.mc.world == null) {
            return;
        }
        this.doAutoCrystal();
        if (this.clearTimer.hasTicksPassed(5L)) {
            this.attackedCrystals.clear();
            this.placedCrystals.clear();
            this.clearTimer.reset();
        }
        ++this.hitTicks;
        ++this.placeTicks;
    }
    
    public void doAutoCrystal() {
        AutoCrystal.target = this.getTarget();
        if (AutoCrystal.target != null) {
            final String mode = this.timing.getMode();
            switch (mode) {
                case "Break": {
                    if (!this.hit.getMode().equals("None") && this.hitTicks > this.hitDelay.getValue()) {
                        this.hitCrystal();
                    }
                    if (this.place.getValue() && this.placeTicks > this.placeDelay.getValue()) {
                        this.placeCrystal();
                    }
                }
                case "Place": {
                    if (this.place.getValue() && this.placeTicks > this.placeDelay.getValue()) {
                        this.placeCrystal();
                    }
                    if (!this.hit.getMode().equals("None") && this.hitTicks > this.hitDelay.getValue()) {
                        this.hitCrystal();
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    public void hitCrystal() {
        if (AutoCrystal.target == null) {
            return;
        }
        EntityEnderCrystal targetCrystal = null;
        double maxDamage = 0.0;
        for (final Entity entity : new ArrayList<Entity>(AutoCrystal.mc.world.loadedEntityList)) {
            if (!(entity instanceof EntityEnderCrystal)) {
                continue;
            }
            final EntityEnderCrystal crystal = (EntityEnderCrystal)entity;
            if (crystal.isDead) {
                continue;
            }
            if (this.attackedCrystals.containsKey(crystal) && this.attackedCrystals.get(crystal) > this.stuckAttempts.getValue() && this.antiStuck.getValue()) {
                continue;
            }
            if (AutoCrystal.mc.player.canEntityBeSeen((Entity)crystal)) {
                if (AutoCrystal.mc.player.getDistanceSq((Entity)crystal) > MathUtil.square(this.hitRange.getValue())) {
                    continue;
                }
            }
            else if (AutoCrystal.mc.player.getDistanceSq((Entity)crystal) > MathUtil.square(this.hitWallsRange.getValue())) {
                continue;
            }
            final double targetDamage = DamageUtil.calculateDamage(crystal, AutoCrystal.target);
            final double selfDamage = this.ignoreSelfDamage.getValue() ? 0.0 : DamageUtil.calculateDamage(crystal, (EntityPlayer)AutoCrystal.mc.player);
            if (!this.hit.getMode().equals("All")) {
                if (targetDamage < this.getMinimumDamage(AutoCrystal.target) && targetDamage * this.lethalMultiplier.getValue() < AutoCrystal.target.getHealth() + AutoCrystal.target.getAbsorptionAmount()) {
                    continue;
                }
                if (selfDamage > this.maxSelfDamage.getValue()) {
                    continue;
                }
                if (AutoCrystal.mc.player.getHealth() - selfDamage <= 0.0 && this.antiSuicide.getValue()) {
                    continue;
                }
            }
            if (this.hit.getMode().equals("All")) {
                targetCrystal = crystal;
            }
            else {
                if (targetDamage <= maxDamage) {
                    continue;
                }
                maxDamage = targetDamage;
                targetCrystal = crystal;
            }
        }
        if (targetCrystal == null) {
            return;
        }
        if (this.rotation.getValue()) {
            this.notorious.rotationManager.rotateToEntity((Entity)targetCrystal);
        }
        for (int i = 0; i < this.iterations.getValue(); ++i) {
            if (this.packetHit.getValue()) {
                AutoCrystal.mc.player.connection.sendPacket((Packet)new CPacketUseEntity((Entity)targetCrystal));
            }
            else {
                AutoCrystal.mc.playerController.attackEntity((EntityPlayer)AutoCrystal.mc.player, (Entity)targetCrystal);
            }
            if (!this.swing.getMode().equals("None")) {
                if (this.silentSwing.getValue()) {
                    AutoCrystal.mc.player.connection.sendPacket((Packet)new CPacketAnimation(this.getHand()));
                }
                else {
                    AutoCrystal.mc.player.swingArm(this.getHand());
                }
            }
        }
        this.addAttackedCrystal(targetCrystal);
        this.hitTicks = 0;
    }
    
    public void placeCrystal() {
        if (AutoCrystal.target == null) {
            return;
        }
        BlockPos targetPosition = null;
        boolean silentSwitched = false;
        double maxDamage = 0.0;
        List<BlockPos> positions = NonNullList.create();
        for (final BlockPos pos : getSphere(new BlockPos(Math.floor(AutoCrystal.mc.player.posX), Math.floor(AutoCrystal.mc.player.posY), Math.floor(AutoCrystal.mc.player.posZ)), this.placeRange.getValue(), (int)this.placeRange.getValue(), false, true, 0)) {
            if (AutoCrystal.mc.world.getBlockState(pos).getBlock() == Blocks.AIR) {
                continue;
            }
            if (!canPlaceCrystal(pos, !this.multiPlace.getValue(), this.placeUnderBlock.getValue())) {
                continue;
            }
            positions.add(pos);
        }
        for (final BlockPos pos : positions) {
            if (!canSeePosition(pos) && this.raytrace.getValue()) {
                continue;
            }
            if (canSeePosition(pos)) {
                if (AutoCrystal.mc.player.getDistanceSq(pos) > this.placeRange.getValue()) {
                    continue;
                }
            }
            else if (AutoCrystal.mc.player.getDistanceSq(pos) > this.placeWallsRange.getValue()) {
                continue;
            }
            final double targetDamage = DamageUtil.calculateDamage(pos, AutoCrystal.target);
            final double selfDamage = this.ignoreSelfDamage.getValue() ? 0.0 : DamageUtil.calculateDamage(pos, (EntityPlayer)AutoCrystal.mc.player);
            if (targetDamage < this.getMinimumDamage(AutoCrystal.target) && targetDamage * this.lethalMultiplier.getValue() < AutoCrystal.target.getHealth() + AutoCrystal.target.getAbsorptionAmount()) {
                continue;
            }
            if (selfDamage > this.maxSelfDamage.getValue()) {
                continue;
            }
            if (AutoCrystal.mc.player.getHealth() - selfDamage <= 0.0 && this.antiSuicide.getValue()) {
                continue;
            }
            if (targetDamage <= maxDamage) {
                continue;
            }
            maxDamage = targetDamage;
            targetPosition = pos;
        }
        final boolean weaknessFlag = this.weaknessCheck.getValue() && AutoCrystal.mc.player.isPotionActive(MobEffects.WEAKNESS) && AutoCrystal.mc.player.getHeldItemMainhand().getItem() != Items.DIAMOND_SWORD && !this.antiWeakness.getMode().equals("Silent");
        if (targetPosition == null || weaknessFlag) {
            this.renderPosition = null;
            return;
        }
        final int slot = InventoryUtil.findItem(Items.END_CRYSTAL, 0, 9);
        final int oldSlot = AutoCrystal.mc.player.inventory.currentItem;
        if (!this.autoSwitch.getMode().equals("None") && slot != -1 && AutoCrystal.mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL && AutoCrystal.mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
            InventoryUtil.switchToSlot(slot, this.autoSwitch.getMode().equals("Silent"));
            if (this.autoSwitch.getMode().equals("Silent")) {
                silentSwitched = true;
            }
        }
        if (AutoCrystal.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL || AutoCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL || silentSwitched) {
            this.renderPosition = targetPosition;
            final RayTraceResult result = AutoCrystal.mc.world.rayTraceBlocks(new Vec3d(AutoCrystal.mc.player.posX, AutoCrystal.mc.player.posY + AutoCrystal.mc.player.getEyeHeight(), AutoCrystal.mc.player.posZ), new Vec3d(targetPosition.getX() + 0.5, targetPosition.getY() - 0.5, targetPosition.getZ() + 0.5));
            final EnumFacing facing = (result == null || result.sideHit == null) ? EnumFacing.UP : result.sideHit;
            final Vec3d hitVec = (result == null || result.hitVec == null) ? new Vec3d(0.0, 0.0, 0.0) : result.hitVec;
            if (this.rotation.getValue()) {
                this.notorious.rotationManager.rotateToPosition(targetPosition);
            }
            AutoCrystal.mc.playerController.processRightClickBlock(AutoCrystal.mc.player, AutoCrystal.mc.world, targetPosition, facing, hitVec, silentSwitched ? EnumHand.MAIN_HAND : this.getHand());
            if (this.placeSwing.getValue()) {
                AutoCrystal.mc.player.connection.sendPacket((Packet)new CPacketAnimation(this.getHand()));
            }
            this.placedCrystals.add(targetPosition);
        }
        else {
            this.renderPosition = null;
        }
        if (this.autoSwitch.getMode().equals("Silent") && silentSwitched && AutoCrystal.mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL && AutoCrystal.mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
            AutoCrystal.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(oldSlot));
        }
        this.placeTicks = 0;
    }
    
    @SubscribeEvent
    public void onRender(final RenderWorldLastEvent event) {
        boolean outline;
        boolean fill;
        if (this.renderMode.getMode().equals("Both")) {
            outline = true;
            fill = true;
        }
        else if (this.renderMode.getMode().equals("Outline")) {
            outline = true;
            fill = false;
        }
        else {
            fill = true;
            outline = false;
        }
        if (this.renderPosition != null) {
            final AxisAlignedBB bb = AutoCrystal.mc.world.getBlockState(this.renderPosition).getSelectedBoundingBox((World)AutoCrystal.mc.world, this.renderPosition);
            if (!this.renderMode.getMode().equals("None")) {
                GlStateManager.disableAlpha();
                if (fill) {
                    RenderUtil.renderFilledBB(bb, new Color(this.fillColor.getAsColor().getRed(), this.fillColor.getAsColor().getGreen(), this.fillColor.getAsColor().getBlue(), 125));
                }
                if (outline) {
                    RenderUtil.renderOutlineBB(bb, new Color(this.outlineColor.getAsColor().getRed(), this.outlineColor.getAsColor().getGreen(), this.outlineColor.getAsColor().getBlue(), (int)this.outlineColor.getAlpha().getValue()));
                }
                GlStateManager.enableAlpha();
            }
        }
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (AutoCrystal.mc.player == null || AutoCrystal.mc.world == null) {
            return;
        }
        if (event.getPacket() instanceof SPacketSpawnObject && this.predict.getValue()) {
            final SPacketSpawnObject packet = (SPacketSpawnObject)event.getPacket();
            final BlockPos position = new BlockPos(packet.getX(), packet.getY() - 1.0, packet.getZ());
            if (packet.getType() == 51 && this.placedCrystals.contains(position)) {
                AutoCrystal.isPredicting = true;
                final CPacketUseEntity packetUseEntity = new CPacketUseEntity();
                ((ICPacketUseEntityMixin)packetUseEntity).setEntityIdAccessor(packet.getEntityID());
                ((ICPacketUseEntityMixin)packetUseEntity).setActionAccessor(CPacketUseEntity.Action.ATTACK);
                AutoCrystal.mc.player.connection.sendPacket((Packet)packetUseEntity);
                if (!this.swing.getMode().equals("None")) {
                    if (this.silentSwing.getValue()) {
                        AutoCrystal.mc.player.connection.sendPacket((Packet)new CPacketAnimation(this.getHand()));
                    }
                    else {
                        AutoCrystal.mc.player.swingArm(this.getHand());
                    }
                }
                AutoCrystal.isPredicting = false;
            }
        }
        if (event.getPacket() instanceof SPacketSoundEffect && this.antiDesync.getValue()) {
            final SPacketSoundEffect packet2 = (SPacketSoundEffect)event.getPacket();
            if (packet2.getCategory() == SoundCategory.BLOCKS && packet2.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                for (final Entity entity : new ArrayList<Entity>(AutoCrystal.mc.world.loadedEntityList)) {
                    if (entity instanceof EntityEnderCrystal && entity.getDistanceSq(packet2.getX(), packet2.getY(), packet2.getZ()) <= MathUtil.square(6.0)) {
                        entity.setDead();
                    }
                }
            }
        }
    }
    
    public double getMinimumDamage(final EntityPlayer player) {
        final boolean swordFlag = this.swordCheck.getValue() && AutoCrystal.mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD;
        final boolean armorFlag = this.armorCheck.getValue() && DamageUtil.shouldBreakArmor((EntityPlayer)AutoCrystal.mc.player, this.armorCheckPercent.getValue());
        final boolean flag = swordFlag || armorFlag;
        if ((this.facePlace.getValue() && player.getHealth() + player.getAbsorptionAmount() < this.facePlaceHealth.getValue() && !flag) || (DamageUtil.shouldBreakArmor(player, this.armorPercent.getValue()) && this.armorBreaker.getValue() && !flag)) {
            return 1.0;
        }
        return this.minimumDamage.getValue();
    }
    
    private EnumHand getHand() {
        if (this.swing.getMode().equals("Mainhand")) {
            return EnumHand.MAIN_HAND;
        }
        return EnumHand.OFF_HAND;
    }
    
    private EntityPlayer getTarget() {
        EntityPlayer optimal = null;
        for (final EntityPlayer player : new ArrayList<EntityPlayer>(AutoCrystal.mc.world.playerEntities)) {
            if (player.getHealth() <= 0.0f) {
                continue;
            }
            if (player.equals((Object)AutoCrystal.mc.player)) {
                continue;
            }
            if (player.getName().equals(AutoCrystal.mc.player.getName())) {
                continue;
            }
            if (this.notorious.friend.isFriend(player.getName())) {
                continue;
            }
            if (player.getDistanceSq((Entity)AutoCrystal.mc.player) > MathUtil.square(this.targetRange.getValue())) {
                continue;
            }
            if (optimal == null) {
                optimal = player;
            }
            else if (optimal.getHealth() > player.getHealth()) {
                optimal = player;
            }
            else {
                if (AutoCrystal.mc.player.getDistance((Entity)optimal) <= AutoCrystal.mc.player.getDistance((Entity)player)) {
                    continue;
                }
                optimal = player;
            }
        }
        return optimal;
    }
    
    private void addAttackedCrystal(final EntityEnderCrystal crystal) {
        if (this.attackedCrystals.containsKey(crystal)) {
            final int value = this.attackedCrystals.get(crystal);
            this.attackedCrystals.put(crystal, value + 1);
        }
        else {
            this.attackedCrystals.put(crystal, 1);
        }
    }
    
    public static boolean canSeePosition(final BlockPos pos) {
        return AutoCrystal.mc.world.rayTraceBlocks(new Vec3d(AutoCrystal.mc.player.posX, AutoCrystal.mc.player.posY + AutoCrystal.mc.player.getEyeHeight(), AutoCrystal.mc.player.posZ), new Vec3d((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()), false, true, false) == null;
    }
    
    public static List<BlockPos> getSphere(final BlockPos loc, final float r, final int h, final boolean hollow, final boolean sphere, final int plusY) {
        final List<BlockPos> circleblocks = new ArrayList<BlockPos>();
        final int cx = loc.getX();
        final int cy = loc.getY();
        final int cz = loc.getZ();
        for (int x = cx - (int)r; x <= cx + r; ++x) {
            for (int z = cz - (int)r; z <= cz + r; ++z) {
                for (int y = sphere ? (cy - (int)r) : (cy - h); y < (sphere ? (cy + r) : ((float)(cy + h))); ++y) {
                    final double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0);
                    if (dist < r * r && (!hollow || dist >= (r - 1.0f) * (r - 1.0f))) {
                        final BlockPos l = new BlockPos(x, y + plusY, z);
                        circleblocks.add(l);
                    }
                }
            }
        }
        return circleblocks;
    }
    
    public static boolean canPlaceCrystal(final BlockPos blockPos, final boolean specialEntityCheck, final boolean placeUnderBlock) {
        final BlockPos boost = blockPos.add(0, 1, 0);
        final BlockPos boostTwo = blockPos.add(0, 2, 0);
        try {
            if (!placeUnderBlock) {
                if (AutoCrystal.mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK && AutoCrystal.mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN) {
                    return false;
                }
                if (AutoCrystal.mc.world.getBlockState(boost).getBlock() != Blocks.AIR || AutoCrystal.mc.world.getBlockState(boostTwo).getBlock() != Blocks.AIR) {
                    return false;
                }
                if (!specialEntityCheck) {
                    return AutoCrystal.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost)).isEmpty() && AutoCrystal.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boostTwo)).isEmpty();
                }
                for (Entity entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost))) {
                    if (entity instanceof EntityEnderCrystal) {
                        continue;
                    }
                    return false;
                }
                for (Entity entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boostTwo))) {
                    if (entity instanceof EntityEnderCrystal) {
                        continue;
                    }
                    return false;
                }
            }
            else {
                if (AutoCrystal.mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK && AutoCrystal.mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN) {
                    return false;
                }
                if (AutoCrystal.mc.world.getBlockState(boost).getBlock() != Blocks.AIR) {
                    return false;
                }
                if (!specialEntityCheck) {
                    return AutoCrystal.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost)).isEmpty();
                }
                for (Entity entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost))) {
                    if (entity instanceof EntityEnderCrystal) {
                        continue;
                    }
                    return false;
                }
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
        return true;
    }
    
    public void onEnable() {
        AutoCrystal.target = null;
        this.renderPosition = null;
        this.hitTicks = 0;
        this.placeTicks = 0;
    }
    
    public void onDisable() {
        AutoCrystal.target = null;
        this.renderPosition = null;
        this.hitTicks = 0;
        this.placeTicks = 0;
    }
    
    static {
        AutoCrystal.target = null;
        AutoCrystal.isPredicting = false;
    }
}
