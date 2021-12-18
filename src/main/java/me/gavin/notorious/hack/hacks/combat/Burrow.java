// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.hack.hacks.combat;

import net.minecraft.block.state.IBlockState;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import me.gavin.notorious.util.BlockUtil;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import me.gavin.notorious.util.rewrite.InventoryUtil;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.Hack;

@RegisterHack(name = "Burrow", description = "ez", category = Hack.Category.Combat)
public class Burrow extends Hack
{
    @RegisterSetting
    private final NumSetting height;
    @RegisterSetting
    private final BooleanSetting preferEChests;
    @RegisterSetting
    private final BooleanSetting lessPackets;
    private static Burrow INSTANCE;
    public BlockPos startPos;
    private int obbySlot;
    
    public Burrow() {
        this.height = new NumSetting("Height", 5.0f, -5.0f, 5.0f, 1.0f);
        this.preferEChests = new BooleanSetting("PreferEChests", true);
        this.lessPackets = new BooleanSetting("ConservePackets", false);
        this.obbySlot = -1;
        Burrow.INSTANCE = this;
    }
    
    public static Burrow getInstance() {
        return Burrow.INSTANCE;
    }
    
    public void onEnable() {
        if (Burrow.mc.player != null && Burrow.mc.world != null) {
            this.toggle();
            return;
        }
        this.obbySlot = InventoryUtil.findBlock(Blocks.OBSIDIAN, 0, 9);
        final int eChestSlot = InventoryUtil.findBlock(Blocks.ENDER_CHEST, 0, 9);
        if ((this.preferEChests.getValue() || this.obbySlot == -1) && eChestSlot != -1) {
            this.obbySlot = eChestSlot;
        }
        else {
            this.obbySlot = InventoryUtil.findBlock(Blocks.OBSIDIAN, 0, 9);
            if (this.obbySlot == -1) {
                this.notorious.messageManager.sendError("Toggling no blocks to place with.");
                this.disable();
            }
        }
    }
    
    @Override
    public void onUpdate() {
        if (Burrow.mc.player != null && Burrow.mc.world != null) {
            return;
        }
        final int startSlot = Burrow.mc.player.inventory.currentItem;
        Burrow.mc.getConnection().sendPacket((Packet)new CPacketHeldItemChange(this.obbySlot));
        this.startPos = new BlockPos(Burrow.mc.player.getPositionVector());
        if (this.lessPackets.getValue()) {
            Burrow.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + 0.45, Burrow.mc.player.posZ, true));
            Burrow.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + 0.79, Burrow.mc.player.posZ, true));
            Burrow.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + 1.1, Burrow.mc.player.posZ, true));
        }
        else {
            Burrow.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + 0.41, Burrow.mc.player.posZ, true));
            Burrow.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + 0.75, Burrow.mc.player.posZ, true));
            Burrow.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + 1.0, Burrow.mc.player.posZ, true));
            Burrow.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + 1.16, Burrow.mc.player.posZ, true));
        }
        final boolean onEChest = Burrow.mc.world.getBlockState(new BlockPos(Burrow.mc.player.getPositionVector())).getBlock() == Blocks.ENDER_CHEST;
        if (this.placeBlock(onEChest ? this.startPos.up() : this.startPos) && !Burrow.mc.player.capabilities.isCreativeMode) {
            Burrow.mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + this.height.getValue(), Burrow.mc.player.posZ, false));
        }
        if (startSlot != -1) {
            Burrow.mc.getConnection().sendPacket((Packet)new CPacketHeldItemChange(startSlot));
        }
        this.disable();
    }
    
    public boolean placeBlock(final BlockPos pos) {
        if (!BlockUtil.mc.world.getBlockState(pos).getBlock().isReplaceable((IBlockAccess)BlockUtil.mc.world, pos)) {
            return false;
        }
        final boolean alreadySneaking = BlockUtil.mc.player.isSneaking();
        for (int i = 0; i < 6; ++i) {
            final EnumFacing side = EnumFacing.values()[i];
            final IBlockState offsetState = BlockUtil.mc.world.getBlockState(pos.offset(side));
            if (offsetState.getBlock().canCollideCheck(offsetState, false)) {
                if (!offsetState.getMaterial().isReplaceable()) {
                    final boolean activated = offsetState.getBlock().onBlockActivated((World)BlockUtil.mc.world, pos, BlockUtil.mc.world.getBlockState(pos), (EntityPlayer)BlockUtil.mc.player, EnumHand.MAIN_HAND, side, 0.5f, 0.5f, 0.5f);
                    if (activated && !alreadySneaking) {
                        Burrow.mc.getConnection().sendPacket((Packet)new CPacketEntityAction((Entity)BlockUtil.mc.player, CPacketEntityAction.Action.START_SNEAKING));
                    }
                    Burrow.mc.getConnection().sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(pos.offset(side), side.getOpposite(), EnumHand.MAIN_HAND, 0.5f, 0.5f, 0.5f));
                    Burrow.mc.player.swingArm(EnumHand.MAIN_HAND);
                    if (activated) {
                        if (!alreadySneaking) {
                            Burrow.mc.getConnection().sendPacket((Packet)new CPacketEntityAction((Entity)BlockUtil.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                        }
                    }
                }
            }
        }
        return true;
    }
}
