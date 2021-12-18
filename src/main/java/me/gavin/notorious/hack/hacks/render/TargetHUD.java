// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.hack.hacks.render;

import net.minecraft.client.renderer.RenderHelper;
import me.gavin.notorious.util.MathUtil;
import java.util.Objects;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import me.gavin.notorious.util.ColorUtil;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import net.minecraft.entity.Entity;
import java.util.Comparator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import me.gavin.notorious.hack.hacks.combat.AutoCrystal;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.Hack;

@RegisterHack(name = "TargetHUD", description = "ez", category = Hack.Category.Render)
public class TargetHUD extends Hack
{
    @RegisterSetting
    public final NumSetting x;
    @RegisterSetting
    public final NumSetting y;
    @RegisterSetting
    public final NumSetting range;
    @RegisterSetting
    public final BooleanSetting autoCrystalTarget;
    @RegisterSetting
    public final BooleanSetting friendSkip;
    @RegisterSetting
    public final BooleanSetting rainbowLine;
    public String nameString;
    
    public TargetHUD() {
        this.x = new NumSetting("X", 2.0f, 0.1f, 1000.0f, 0.1f);
        this.y = new NumSetting("Y", 2.0f, 0.1f, 600.0f, 0.1f);
        this.range = new NumSetting("Range", 4.0f, 1.0f, 6.0f, 1.0f);
        this.autoCrystalTarget = new BooleanSetting("AutoCrystalTarget", true);
        this.friendSkip = new BooleanSetting("FriendSkip", false);
        this.rainbowLine = new BooleanSetting("RainbowLine", true);
    }
    
    @Override
    public String getMetaData() {
        if (this.nameString != null) {
            return " [" + ChatFormatting.GRAY + this.nameString + ChatFormatting.RESET + "]";
        }
        return "";
    }
    
    @SubscribeEvent
    public void onUpdate(final RenderGameOverlayEvent.Text event) {
        EntityPlayer entityPlayer;
        if (this.autoCrystalTarget.isEnabled()) {
            entityPlayer = AutoCrystal.target;
        }
        else {
            entityPlayer = (EntityPlayer)TargetHUD.mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityPlayer).filter(entity -> entity != TargetHUD.mc.player).map(entity -> entity).min(Comparator.comparing(c -> TargetHUD.mc.player.getDistance(c))).orElse(null);
        }
        if (entityPlayer != null) {
            if (this.friendSkip.isEnabled() && this.notorious.friend.isFriend(entityPlayer.getName())) {
                return;
            }
            if (entityPlayer.getDistance((Entity)TargetHUD.mc.player) <= this.range.getValue()) {
                this.nameString = entityPlayer.getDisplayNameString();
                Gui.drawRect((int)this.x.getValue(), (int)this.y.getValue(), (int)this.x.getValue() + 190, (int)this.y.getValue() + 50, new Color(0, 0, 0, 255).getRGB());
                if (this.rainbowLine.isEnabled()) {
                    Gui.drawRect((int)this.x.getValue(), (int)this.y.getValue(), (int)this.x.getValue() + 190, (int)this.y.getValue() + 1, ColorUtil.getRainbow(6.0f, 1.0f));
                }
                TargetHUD.mc.fontRenderer.drawStringWithShadow(entityPlayer.getName(), this.x.getValue() + 5.0f, this.y.getValue() + 5.0f, this.notorious.friend.isFriend(entityPlayer.getName()) ? new Color(0, 255, 234).getRGB() : -1);
                final int healthInt = (int)(entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount());
                Color healthColor = null;
                if (healthInt > 19) {
                    healthColor = new Color(0, 255, 0, 255);
                }
                else if (healthInt > 10) {
                    healthColor = new Color(255, 255, 0, 255);
                }
                else if (healthInt > 0) {
                    healthColor = new Color(255, 0, 0, 255);
                }
                else {
                    healthColor = new Color(0, 0, 0, 255);
                }
                TargetHUD.mc.fontRenderer.drawStringWithShadow("HP:" + healthInt, this.x.getValue() + 5.0f, this.y.getValue() + 15.0f, healthColor.getRGB());
                GlStateManager.color(1.0f, 1.0f, 1.0f);
                GuiInventory.drawEntityOnScreen((int)this.x.getValue() + 115, (int)this.y.getValue() + 48, 20, 0.0f, 0.0f, (EntityLivingBase)entityPlayer);
                Color pingColor = null;
                if (this.getPing(entityPlayer) < 49) {
                    pingColor = new Color(0, 255, 0, 255);
                }
                else if (this.getPing(entityPlayer) < 99) {
                    pingColor = new Color(255, 255, 0, 255);
                }
                else if (this.getPing(entityPlayer) >= 100) {
                    pingColor = new Color(255, 0, 0, 255);
                }
                else {
                    pingColor = new Color(0, 0, 0, 255);
                }
                TargetHUD.mc.fontRenderer.drawStringWithShadow("Ping:" + this.getPing(entityPlayer), this.x.getValue() + 5.0f, this.y.getValue() + 25.0f, pingColor.getRGB());
                String fuckedDetector = "";
                Color fuckedColor;
                if (this.isFucked(entityPlayer)) {
                    fuckedDetector = "FUCKED";
                    fuckedColor = new Color(0, 255, 0, 255);
                }
                else if (!this.isFucked(entityPlayer)) {
                    fuckedDetector = "SAFE";
                    fuckedColor = new Color(255, 0, 0, 255);
                }
                else {
                    fuckedColor = new Color(0, 0, 0, 255);
                }
                TargetHUD.mc.fontRenderer.drawStringWithShadow(fuckedDetector, this.x.getValue() + 5.0f, this.y.getValue() + 35.0f, fuckedColor.getRGB());
                final ItemStack itemStack = new ItemStack(Items.TOTEM_OF_UNDYING);
                this.renderItem(itemStack, (int)this.x.getValue() + 142, (int)this.y.getValue() + 26);
                String pops = "0";
                if (this.notorious.popListener.popMap.get(entityPlayer.getName()) != null) {
                    pops = this.notorious.popListener.popMap.get(entityPlayer.getName()).toString();
                }
                TargetHUD.mc.fontRenderer.drawStringWithShadow(pops, this.x.getValue() + 158.0f, this.y.getValue() + 32.0f, -1);
                int yOffset = 10;
                for (final ItemStack stack : entityPlayer.getArmorInventoryList()) {
                    if (stack == null) {
                        continue;
                    }
                    final ItemStack armourStack = stack.copy();
                    this.renderItem(armourStack, (int)this.x.getValue() + 125, yOffset + (int)this.y.getValue() + 26);
                    yOffset -= 13;
                }
                final ArrayList<Block> surroundblocks = this.getSurroundBlocks((EntityLivingBase)entityPlayer);
                this.renderItem(new ItemStack((Block)surroundblocks.get(0)), (int)this.x.getValue() + 73, (int)this.y.getValue() + 10);
                this.renderItem(new ItemStack((Block)surroundblocks.get(1)), (int)this.x.getValue() + 61, (int)this.y.getValue() + 21);
                this.renderItem(new ItemStack((Block)surroundblocks.get(2)), (int)this.x.getValue() + 73, (int)this.y.getValue() + 32);
                this.renderItem(new ItemStack((Block)surroundblocks.get(3)), (int)this.x.getValue() + 85, (int)this.y.getValue() + 21);
                final ItemStack mainHand = new ItemStack(entityPlayer.getHeldItemMainhand().getItem());
                final ItemStack offHand = new ItemStack(entityPlayer.getHeldItemOffhand().getItem());
                this.renderItem(mainHand, (int)this.x.getValue() + 160, (int)this.y.getValue() + 5);
                this.renderItem(offHand, (int)this.x.getValue() + 142, (int)this.y.getValue() + 5);
            }
        }
    }
    
    public boolean isFucked(final EntityPlayer player) {
        final BlockPos pos = new BlockPos(player.posX, player.posY - 1.0, player.posZ);
        return this.canPlaceCrystal(pos.south()) || (this.canPlaceCrystal(pos.south().south()) && TargetHUD.mc.world.getBlockState(pos.add(0, 1, 1)).getBlock() == Blocks.AIR) || (this.canPlaceCrystal(pos.east()) || (this.canPlaceCrystal(pos.east().east()) && TargetHUD.mc.world.getBlockState(pos.add(1, 1, 0)).getBlock() == Blocks.AIR)) || (this.canPlaceCrystal(pos.west()) || (this.canPlaceCrystal(pos.west().west()) && TargetHUD.mc.world.getBlockState(pos.add(-1, 1, 0)).getBlock() == Blocks.AIR)) || (this.canPlaceCrystal(pos.north()) || (this.canPlaceCrystal(pos.north().north()) && TargetHUD.mc.world.getBlockState(pos.add(0, 1, -1)).getBlock() == Blocks.AIR));
    }
    
    public boolean canPlaceCrystal(final BlockPos pos) {
        final Block block = TargetHUD.mc.world.getBlockState(pos).getBlock();
        if (block == Blocks.OBSIDIAN || block == Blocks.BEDROCK) {
            final Block floor = TargetHUD.mc.world.getBlockState(pos.add(0, 1, 0)).getBlock();
            final Block ceil = TargetHUD.mc.world.getBlockState(pos.add(0, 2, 0)).getBlock();
            if (floor == Blocks.AIR && ceil == Blocks.AIR && TargetHUD.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(pos.add(0, 1, 0))).isEmpty() && TargetHUD.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(pos.add(0, 2, 0))).isEmpty()) {
                return true;
            }
        }
        return false;
    }
    
    public int getPing(final EntityPlayer player) {
        int ping = 0;
        try {
            ping = MathUtil.clamp(Objects.requireNonNull(TargetHUD.mc.getConnection()).getPlayerInfo(player.getUniqueID()).getResponseTime(), 1, 99999);
        }
        catch (NullPointerException ex) {}
        return ping;
    }
    
    private void renderItem(final ItemStack itemStack, final int posX, final int posY) {
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
        GlStateManager.clear(256);
        GlStateManager.enableDepth();
        GlStateManager.disableAlpha();
        TargetHUD.mc.getRenderItem().zLevel = -150.0f;
        RenderHelper.enableStandardItemLighting();
        TargetHUD.mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, posX, posY);
        TargetHUD.mc.getRenderItem().renderItemOverlays(TargetHUD.mc.fontRenderer, itemStack, posX, posY);
        RenderHelper.disableStandardItemLighting();
        TargetHUD.mc.getRenderItem().zLevel = 0.0f;
    }
    
    private ArrayList<Block> getSurroundBlocks(final EntityLivingBase e) {
        final ArrayList<Block> surroundblocks = new ArrayList<Block>();
        final BlockPos entityblock = new BlockPos(Math.floor(e.posX), Math.floor(e.posY), Math.floor(e.posZ));
        surroundblocks.add(TargetHUD.mc.world.getBlockState(entityblock.north()).getBlock());
        surroundblocks.add(TargetHUD.mc.world.getBlockState(entityblock.east()).getBlock());
        surroundblocks.add(TargetHUD.mc.world.getBlockState(entityblock.south()).getBlock());
        surroundblocks.add(TargetHUD.mc.world.getBlockState(entityblock.west()).getBlock());
        return surroundblocks;
    }
}
