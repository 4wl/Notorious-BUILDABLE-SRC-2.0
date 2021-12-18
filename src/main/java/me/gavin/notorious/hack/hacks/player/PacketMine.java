// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.hack.hacks.player;

import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import me.gavin.notorious.util.RenderUtil;
import net.minecraft.util.math.AxisAlignedBB;
import me.gavin.notorious.mixin.mixins.accessor.IRenderGlobal;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import java.awt.Color;
import net.minecraft.util.math.BlockPos;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.ModeSetting;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.Hack;

@RegisterHack(name = "PacketMine", description = "ez", category = Hack.Category.Player)
public class PacketMine extends Hack
{
    @RegisterSetting
    public final ModeSetting mode;
    @RegisterSetting
    public final ColorSetting outlineColor;
    @RegisterSetting
    public final ColorSetting boxColor;
    @RegisterSetting
    public final BooleanSetting fade;
    private BlockPos renderBlock;
    public boolean fill;
    public boolean outline;
    
    public PacketMine() {
        this.mode = new ModeSetting("Mode", "Both", new String[] { "Both", "Outline", "Box" });
        this.outlineColor = new ColorSetting("Outline", new Color(117, 0, 255, 255));
        this.boxColor = new ColorSetting("Box", new Color(117, 0, 255, 65));
        this.fade = new BooleanSetting("Fade", true);
    }
    
    @SubscribeEvent
    public void onEvent(final PlayerInteractEvent.LeftClickBlock event) {
        if (this.canBreak(event.getPos())) {
            PacketMine.mc.player.swingArm(EnumHand.MAIN_HAND);
            PacketMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.getPos(), event.getFace()));
            PacketMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPos(), event.getFace()));
            if (this.renderBlock == null) {
                this.renderBlock = event.getPos();
            }
        }
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (PacketMine.mc.player == null || PacketMine.mc.world == null) {
            return;
        }
        if (this.renderBlock != null && PacketMine.mc.world.getBlockState(this.renderBlock).getBlock() == Blocks.AIR) {
            this.renderBlock = null;
        }
    }
    
    @SubscribeEvent
    public void onRender(final RenderWorldLastEvent event) {
        if (this.mode.getMode().equals("Both")) {
            this.outline = true;
            this.fill = true;
        }
        else if (this.mode.getMode().equals("Outline")) {
            this.outline = true;
            this.fill = false;
        }
        else {
            this.fill = true;
            this.outline = false;
        }
        ((IRenderGlobal)PacketMine.mc.renderGlobal).getDamagedBlocks().forEach((integer, destroyBlockProgress) -> {
            if (this.renderBlock != null) {
                AxisAlignedBB bb = new AxisAlignedBB(renderBlock);
                if (this.fade.isEnabled()) {
                    bb = bb.shrink((3.0 - destroyBlockProgress.getPartialBlockDamage() / 2.6666666666666665) / 9.0);
                }
                if (this.outline) {
                    RenderUtil.renderOutlineBB(bb, new Color(255, 255, 255));
                }
                if (this.fill) {
                    RenderUtil.renderFilledBB(bb, new Color(255, 255, 255, 255));
                }
            }
        });
    }
    
    private boolean canBreak(final BlockPos pos) {
        final IBlockState blockState = PacketMine.mc.world.getBlockState(pos);
        final Block block = blockState.getBlock();
        return PacketMine.mc.world.getBlockState(pos).getBlock() != Blocks.AIR && block.getBlockHardness(blockState, (World)PacketMine.mc.world, pos) != -1.0f;
    }
}
