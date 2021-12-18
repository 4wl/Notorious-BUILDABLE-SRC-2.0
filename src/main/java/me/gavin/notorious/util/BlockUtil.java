// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.util;

import java.util.Arrays;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import me.gavin.notorious.mixin.mixins.accessor.IMinecraftMixin;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockDeadBush;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.world.GameType;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumHand;
import me.gavin.notorious.Notorious;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import java.util.Iterator;
import net.minecraft.init.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.BlockPos;
import java.util.ArrayList;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.Block;
import java.util.List;
import me.gavin.notorious.stuff.IMinecraft;

public class BlockUtil implements IMinecraft
{
    public static List<Block> emptyBlocks;
    public static List<Block> actualBlocks;
    public static List<Block> rightclickableBlocks;
    public static List<Block> unSafeBlocks;
    public static Vec3d[] holeOffsets;
    
    public static ArrayList<BlockPos> getSurroundingBlocks(final int radius, final boolean motion) {
        final ArrayList<BlockPos> posList = new ArrayList<BlockPos>();
        final BlockPos playerPos = BlockUtil.mc.player.getPosition().add(0, 1, 0);
        if (motion) {
            playerPos.add(BlockUtil.mc.player.motionX, BlockUtil.mc.player.motionY, BlockUtil.mc.player.motionZ);
        }
        for (int x = -radius; x < radius; ++x) {
            for (int y = -radius; y < radius; ++y) {
                for (int z = -radius; z < radius; ++z) {
                    posList.add(new BlockPos(x, y, z).add((Vec3i)playerPos));
                }
            }
        }
        return posList;
    }
    
    public static boolean isBurrowed(final Entity entity) {
        final BlockPos entityPos = new BlockPos(MathUtil.roundValueToCenter(entity.posX), entity.posY + 0.2, MathUtil.roundValueToCenter(entity.posZ));
        return BlockUtil.mc.world.getBlockState(entityPos).getBlock() == Blocks.OBSIDIAN || BlockUtil.mc.world.getBlockState(entityPos).getBlock() == Blocks.ENDER_CHEST;
    }
    
    public static ArrayList<BlockPos> getSurroundingBlocksOtherPlayers(final int radius, final boolean motion) {
        final Iterator<Entity> iterator = BlockUtil.mc.world.loadedEntityList.iterator();
        if (iterator.hasNext()) {
            final Entity e = iterator.next();
            final ArrayList<BlockPos> posList = new ArrayList<BlockPos>();
            final BlockPos playerPos = e.getPosition().add(0, 1, 0);
            if (motion) {
                playerPos.add(e.motionX, e.motionY, e.motionZ);
            }
            for (int x = -radius; x < radius; ++x) {
                for (int y = -radius; y < radius; ++y) {
                    for (int z = -radius; z < radius; ++z) {
                        posList.add(new BlockPos(x, y, z).add((Vec3i)playerPos));
                    }
                }
            }
            return posList;
        }
        return null;
    }
    
    public static void damageBlock(final BlockPos position, final boolean packet, final boolean rotations) {
        damageBlock(position, EnumFacing.getDirectionFromEntityLiving(position, (EntityLivingBase)BlockUtil.mc.player), packet, rotations);
    }
    
    public static void damageBlock(final BlockPos position, final EnumFacing facing, final boolean packet, final boolean rotations) {
        if (rotations) {
            final float[] r = MathUtil.calculateLookAt(position.getX() + 0.5, position.getY() + 0.5, position.getZ() + 0.5, (EntityPlayer)BlockUtil.mc.player);
            Notorious.INSTANCE.rotationManager.desiredYaw = r[0];
            Notorious.INSTANCE.rotationManager.desiredPitch = r[1];
        }
        BlockUtil.mc.player.swingArm(EnumHand.MAIN_HAND);
        if (packet) {
            BlockUtil.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, position, facing));
            BlockUtil.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, position, facing));
        }
        else if (BlockUtil.mc.getConnection().getPlayerInfo(BlockUtil.mc.player.getUniqueID()).getGameType() == GameType.CREATIVE) {
            BlockUtil.mc.playerController.clickBlock(position, facing);
        }
        else {
            BlockUtil.mc.playerController.onPlayerDamageBlock(position, facing);
        }
    }
    
    public static void rotatePacket(final double x, final double y, final double z) {
        final double diffX = x - BlockUtil.mc.player.posX;
        final double diffY = y - (BlockUtil.mc.player.posY + BlockUtil.mc.player.getEyeHeight());
        final double diffZ = z - BlockUtil.mc.player.posZ;
        final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        BlockUtil.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(yaw, pitch, BlockUtil.mc.player.onGround));
    }
    
    public static int isPositionPlaceable(final BlockPos pos, final boolean rayTrace, final boolean entityCheck) {
        final Block block = BlockUtil.mc.world.getBlockState(pos).getBlock();
        if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid) && !(block instanceof BlockTallGrass) && !(block instanceof BlockFire) && !(block instanceof BlockDeadBush) && !(block instanceof BlockSnow)) {
            return 0;
        }
        if (!rayTracePlaceCheck(pos, rayTrace, 0.0f)) {
            return -1;
        }
        if (entityCheck) {
            for (Entity entity : BlockUtil.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos))) {
                if (!(entity instanceof EntityItem)) {
                    if (entity instanceof EntityXPOrb) {
                        continue;
                    }
                    return 1;
                }
            }
        }
        for (final EnumFacing side : getPossibleSides(pos)) {
            if (!canBeClicked(pos.offset(side))) {
                continue;
            }
            return 3;
        }
        return 2;
    }
    
    public static boolean placeBlock(final BlockPos pos, final EnumHand hand, final boolean rotate, final boolean packet, final boolean isSneaking) {
        boolean sneaking = false;
        final EnumFacing side = getFirstFacing(pos);
        if (side == null) {
            return isSneaking;
        }
        final BlockPos neighbour = pos.offset(side);
        final EnumFacing opposite = side.getOpposite();
        final Vec3d hitVec = new Vec3d((Vec3i)neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        final Block neighbourBlock = BlockUtil.mc.world.getBlockState(neighbour).getBlock();
        if (!BlockUtil.mc.player.isSneaking() && (BlockUtil.emptyBlocks.contains(neighbourBlock) || BlockUtil.rightclickableBlocks.contains(neighbourBlock))) {
            BlockUtil.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)BlockUtil.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            BlockUtil.mc.player.setSneaking(true);
            sneaking = true;
        }
        if (rotate) {
            RotationUtil.faceVector(hitVec, true);
        }
        rightClickBlock(neighbour, hitVec, hand, opposite, packet);
        BlockUtil.mc.player.swingArm(EnumHand.MAIN_HAND);
        ((IMinecraftMixin)BlockUtil.mc).setRightClickDelayTimerAccessor(4);
        return sneaking || isSneaking;
    }
    
    public static void rightClickBlock(final BlockPos pos, final Vec3d vec, final EnumHand hand, final EnumFacing direction, final boolean packet) {
        if (packet) {
            final float f = (float)(vec.x - pos.getX());
            final float f2 = (float)(vec.y - pos.getY());
            final float f3 = (float)(vec.z - pos.getZ());
            BlockUtil.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f2, f3));
        }
        else {
            BlockUtil.mc.playerController.processRightClickBlock(BlockUtil.mc.player, BlockUtil.mc.world, pos, direction, vec, hand);
        }
        BlockUtil.mc.player.swingArm(EnumHand.MAIN_HAND);
        ((IMinecraftMixin)BlockUtil.mc).setRightClickDelayTimerAccessor(4);
    }
    
    public static EnumFacing getFirstFacing(final BlockPos pos) {
        final Iterator<EnumFacing> iterator = getPossibleSides(pos).iterator();
        if (iterator.hasNext()) {
            final EnumFacing facing = iterator.next();
            return facing;
        }
        return null;
    }
    
    public static int isPositionPlaceable(final BlockPos pos, final boolean rayTrace) {
        return isPositionPlaceable(pos, rayTrace, true);
    }
    
    public static List<EnumFacing> getPossibleSides(final BlockPos pos) {
        final ArrayList<EnumFacing> facings = new ArrayList<EnumFacing>();
        if (BlockUtil.mc.world == null || pos == null) {
            return facings;
        }
        for (final EnumFacing side : EnumFacing.values()) {
            final BlockPos neighbour = pos.offset(side);
            final IBlockState blockState = BlockUtil.mc.world.getBlockState(neighbour);
            if (blockState != null && blockState.getBlock().canCollideCheck(blockState, false)) {
                if (!blockState.getMaterial().isReplaceable()) {
                    facings.add(side);
                }
            }
        }
        return facings;
    }
    
    public static boolean rayTracePlaceCheck(final BlockPos pos, final boolean shouldCheck, final float height) {
        return !shouldCheck || BlockUtil.mc.world.rayTraceBlocks(new Vec3d(BlockUtil.mc.player.posX, BlockUtil.mc.player.posY + BlockUtil.mc.player.getEyeHeight(), BlockUtil.mc.player.posZ), new Vec3d((double)pos.getX(), (double)(pos.getY() + height), (double)pos.getZ()), false, true, false) == null;
    }
    
    public static boolean isRightClickableBlock(final BlockPos pos) {
        return pos.equals((Object)BlockUtil.rightclickableBlocks);
    }
    
    public static boolean canBeClicked(final BlockPos pos) {
        return getBlock(pos).canCollideCheck(getState(pos), false);
    }
    
    public static BlockPos[] toBlockPos(final Vec3d[] vec3ds) {
        final BlockPos[] list = new BlockPos[vec3ds.length];
        for (int i = 0; i < vec3ds.length; ++i) {
            list[i] = new BlockPos(vec3ds[i]);
        }
        return list;
    }
    
    public static List<BlockPos> getSphere(final float radius, final boolean ignoreAir) {
        final ArrayList<BlockPos> sphere = new ArrayList<BlockPos>();
        final BlockPos pos = new BlockPos(BlockUtil.mc.player.getPositionVector());
        final int posX = pos.getX();
        final int posY = pos.getY();
        final int posZ = pos.getZ();
        final int radiuss = (int)radius;
        for (int x = posX - radiuss; x <= posX + radius; ++x) {
            for (int z = posZ - radiuss; z <= posZ + radius; ++z) {
                for (int y = posY - radiuss; y < posY + radius; ++y) {
                    if ((posX - x) * (posX - x) + (posZ - z) * (posZ - z) + (posY - y) * (posY - y) < radius * radius) {
                        final BlockPos position = new BlockPos(x, y, z);
                        if (!ignoreAir || BlockUtil.mc.world.getBlockState(position).getBlock() != Blocks.AIR) {
                            sphere.add(position);
                        }
                    }
                }
            }
        }
        return sphere;
    }
    
    private static Block getBlock(final BlockPos pos) {
        return getState(pos).getBlock();
    }
    
    private static IBlockState getState(final BlockPos pos) {
        return BlockUtil.mc.world.getBlockState(pos);
    }
    
    static {
        BlockUtil.emptyBlocks = Arrays.asList(Blocks.AIR, (Block)Blocks.FLOWING_LAVA, (Block)Blocks.LAVA, (Block)Blocks.FLOWING_WATER, (Block)Blocks.WATER, Blocks.VINE, Blocks.SNOW_LAYER, (Block)Blocks.TALLGRASS, (Block)Blocks.FIRE);
        BlockUtil.actualBlocks = Arrays.asList((Block)Blocks.GRASS, Blocks.DIRT, Blocks.END_STONE);
        BlockUtil.rightclickableBlocks = Arrays.asList((Block)Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.ENDER_CHEST, Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.SILVER_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.ANVIL, Blocks.WOODEN_BUTTON, Blocks.STONE_BUTTON, (Block)Blocks.UNPOWERED_COMPARATOR, (Block)Blocks.UNPOWERED_REPEATER, (Block)Blocks.POWERED_REPEATER, (Block)Blocks.POWERED_COMPARATOR, Blocks.OAK_FENCE_GATE, Blocks.SPRUCE_FENCE_GATE, Blocks.BIRCH_FENCE_GATE, Blocks.JUNGLE_FENCE_GATE, Blocks.DARK_OAK_FENCE_GATE, Blocks.ACACIA_FENCE_GATE, Blocks.BREWING_STAND, Blocks.DISPENSER, Blocks.DROPPER, Blocks.LEVER, Blocks.NOTEBLOCK, Blocks.JUKEBOX, (Block)Blocks.BEACON, Blocks.BED, Blocks.FURNACE, (Block)Blocks.OAK_DOOR, (Block)Blocks.SPRUCE_DOOR, (Block)Blocks.BIRCH_DOOR, (Block)Blocks.JUNGLE_DOOR, (Block)Blocks.ACACIA_DOOR, (Block)Blocks.DARK_OAK_DOOR, Blocks.CAKE, Blocks.ENCHANTING_TABLE, Blocks.DRAGON_EGG, (Block)Blocks.HOPPER, Blocks.REPEATING_COMMAND_BLOCK, Blocks.COMMAND_BLOCK, Blocks.CHAIN_COMMAND_BLOCK, Blocks.CRAFTING_TABLE);
        BlockUtil.unSafeBlocks = Arrays.asList(Blocks.OBSIDIAN, Blocks.BEDROCK, Blocks.ENDER_CHEST, Blocks.ANVIL);
        BlockUtil.holeOffsets = new Vec3d[] { new Vec3d(-1.0, 0.0, 0.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(0.0, -1.0, 0.0) };
    }
}
