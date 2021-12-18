// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.hack.hacks.world;

import me.gavin.notorious.util.RenderUtil;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import java.util.Iterator;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.block.BlockShulkerBox;
import me.gavin.notorious.util.BlockUtil;
import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.util.NColor;
import net.minecraft.util.math.BlockPos;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.Hack;

@RegisterHack(name = "ShulkerJew", description = "Automatically breaks shulkers.", category = Hack.Category.World)
public class ShulkerJew extends Hack
{
    @RegisterSetting
    public final NumSetting range;
    @RegisterSetting
    public final ColorSetting boxColor;
    @RegisterSetting
    public final ColorSetting outlineColor;
    private BlockPos targetedBlock;
    
    public ShulkerJew() {
        this.range = new NumSetting("Range", 5.0f, 0.0f, 6.0f, 0.5f);
        this.boxColor = new ColorSetting("Box", new NColor(255, 255, 255, 125));
        this.outlineColor = new ColorSetting("Outline", new NColor(255, 255, 255, 255));
        this.targetedBlock = null;
    }
    
    @Override
    public String getMetaData() {
        return " [" + ChatFormatting.GRAY + this.range.getValue() + ChatFormatting.RESET + "]";
    }
    
    @SubscribeEvent
    public void onLivingUpdate(final PlayerLivingUpdateEvent event) {
        if (this.targetedBlock == null) {
            for (final BlockPos pos : BlockUtil.getSurroundingBlocks(5, true)) {
                final Block block = ShulkerJew.mc.world.getBlockState(pos).getBlock();
                if (block instanceof BlockShulkerBox && ShulkerJew.mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe) {
                    this.targetedBlock = pos;
                    break;
                }
            }
        }
        else {
            if (ShulkerJew.mc.world.getBlockState(this.targetedBlock).getBlock() == Blocks.AIR) {
                this.targetedBlock = null;
                return;
            }
            if (this.targetedBlock.getDistance(ShulkerJew.mc.player.getPosition().getX(), ShulkerJew.mc.player.getPosition().getY(), ShulkerJew.mc.player.getPosition().getZ()) > this.range.getValue()) {
                this.targetedBlock = null;
                return;
            }
            BlockUtil.damageBlock(this.targetedBlock, false, true);
        }
    }
    
    @SubscribeEvent
    public void onRender(final RenderWorldLastEvent event) {
        if (this.targetedBlock != null) {
            final AxisAlignedBB bb = new AxisAlignedBB(this.targetedBlock);
            RenderUtil.renderFilledBB(bb, this.boxColor.getAsColor());
            RenderUtil.renderOutlineBB(bb, this.outlineColor.getAsColor());
        }
    }
}
