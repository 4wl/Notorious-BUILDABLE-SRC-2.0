// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.hack.hacks.render;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import me.gavin.notorious.util.RenderUtil;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import java.awt.Color;
import java.util.List;
import net.minecraft.util.math.BlockPos;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.ModeSetting;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.Hack;

@RegisterHack(name = "FuckedDetector", description = "ez", category = Hack.Category.Render)
public class FuckedDetector extends Hack
{
    @RegisterSetting
    public final ModeSetting mode;
    @RegisterSetting
    public final ColorSetting outlineColor;
    @RegisterSetting
    public final ColorSetting boxColor;
    @RegisterSetting
    public final BooleanSetting self;
    public BlockPos pos;
    public boolean fill;
    public boolean outline;
    public List<BlockPos> fuckedEntities;
    
    public FuckedDetector() {
        this.mode = new ModeSetting("Mode", "Both", new String[] { "Both", "Outline", "Box" });
        this.outlineColor = new ColorSetting("Outline", new Color(255, 255, 255, 255));
        this.boxColor = new ColorSetting("Box", new Color(255, 255, 255, 125));
        this.self = new BooleanSetting("Self", true);
        this.fuckedEntities = new ArrayList<BlockPos>();
    }
    
    @Override
    public String getMetaData() {
        return " [" + ChatFormatting.GRAY + this.mode.getMode() + ChatFormatting.RESET + "]";
    }
    
    @SubscribeEvent
    public void onTick(final PlayerLivingUpdateEvent event) {
        this.fuckedEntities.clear();
        for (final EntityPlayer e : FuckedDetector.mc.world.playerEntities) {
            if (e.equals((Object)FuckedDetector.mc.player) && !this.self.isEnabled()) {
                return;
            }
            this.pos = new BlockPos(e.posX, e.posY, e.posZ);
            if (!this.isFucked(e)) {
                continue;
            }
            this.fuckedEntities.add(this.pos);
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
        for (final BlockPos blockPos : this.fuckedEntities) {
            if (this.outline) {
                RenderUtil.renderOutlineBB(new AxisAlignedBB(blockPos), this.outlineColor.getAsColor());
            }
            if (this.fill) {
                RenderUtil.renderFilledBB(new AxisAlignedBB(blockPos), this.boxColor.getAsColor());
            }
        }
    }
    
    public boolean canPlaceCrystal(final BlockPos pos) {
        final Block block = FuckedDetector.mc.world.getBlockState(pos).getBlock();
        if (block == Blocks.OBSIDIAN || block == Blocks.BEDROCK) {
            final Block floor = FuckedDetector.mc.world.getBlockState(pos.add(0, 1, 0)).getBlock();
            final Block ceil = FuckedDetector.mc.world.getBlockState(pos.add(0, 2, 0)).getBlock();
            if (floor == Blocks.AIR && ceil == Blocks.AIR && FuckedDetector.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(pos.add(0, 1, 0))).isEmpty() && FuckedDetector.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(pos.add(0, 2, 0))).isEmpty()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isFucked(final EntityPlayer player) {
        final BlockPos pos = new BlockPos(player.posX, player.posY - 1.0, player.posZ);
        return this.canPlaceCrystal(pos.south()) || (this.canPlaceCrystal(pos.south().south()) && FuckedDetector.mc.world.getBlockState(pos.add(0, 1, 1)).getBlock() == Blocks.AIR) || (this.canPlaceCrystal(pos.east()) || (this.canPlaceCrystal(pos.east().east()) && FuckedDetector.mc.world.getBlockState(pos.add(1, 1, 0)).getBlock() == Blocks.AIR)) || (this.canPlaceCrystal(pos.west()) || (this.canPlaceCrystal(pos.west().west()) && FuckedDetector.mc.world.getBlockState(pos.add(-1, 1, 0)).getBlock() == Blocks.AIR)) || (this.canPlaceCrystal(pos.north()) || (this.canPlaceCrystal(pos.north().north()) && FuckedDetector.mc.world.getBlockState(pos.add(0, 1, -1)).getBlock() == Blocks.AIR));
    }
}
