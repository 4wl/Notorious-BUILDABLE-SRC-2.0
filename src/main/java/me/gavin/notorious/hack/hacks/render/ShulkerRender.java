// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.hack.hacks.render;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.util.NonNullList;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.math.Vec3d;
import java.util.Iterator;
import me.gavin.notorious.util.ProjectionUtil;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.Hack;

@RegisterHack(name = "ShulkerNametag", description = "ez", category = Hack.Category.Render)
public class ShulkerRender extends Hack
{
    @SubscribeEvent
    public void onRender(final RenderGameOverlayEvent.Text event) {
        for (final Entity e : ShulkerRender.mc.world.loadedEntityList) {
            if (e instanceof EntityItem) {
                final EntityItem item = (EntityItem)e;
                if (!(item.getItem().getItem() instanceof ItemShulkerBox)) {
                    continue;
                }
                final Vec3d projection = ProjectionUtil.toScaledScreenPos(item.getPositionVector());
                this.renderShulkerToolTip(item.getItem(), (int)projection.x - 80, (int)projection.y - 80);
            }
        }
    }
    
    public void renderShulkerToolTip(final ItemStack stack, final int x, final int y) {
        final NBTTagCompound tagCompound = stack.getTagCompound();
        final NBTTagCompound blockEntityTag;
        if (tagCompound != null && tagCompound.hasKey("BlockEntityTag", 10) && (blockEntityTag = tagCompound.getCompoundTag("BlockEntityTag")).hasKey("Items", 9)) {
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.enableDepth();
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableColorMaterial();
            GlStateManager.enableLighting();
            final NonNullList nonnulllist = NonNullList.withSize(27, (Object)ItemStack.EMPTY);
            ItemStackHelper.loadAllItems(blockEntityTag, nonnulllist);
            for (int i = 0; i < nonnulllist.size(); ++i) {
                final int iX = x + i % 9 * 18 + 8;
                final int iY = y + i / 9 * 18 + 18;
                final ItemStack itemStack = (ItemStack)nonnulllist.get(i);
                this.renderItem(itemStack, iX, iY);
            }
            GlStateManager.disableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
    
    private void renderItem(final ItemStack itemStack, final int posX, final int posY) {
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
        GlStateManager.clear(256);
        GlStateManager.enableDepth();
        GlStateManager.disableAlpha();
        ShulkerRender.mc.getRenderItem().zLevel = -150.0f;
        RenderHelper.enableStandardItemLighting();
        ShulkerRender.mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, posX, posY);
        ShulkerRender.mc.getRenderItem().renderItemOverlays(ShulkerRender.mc.fontRenderer, itemStack, posX, posY);
        RenderHelper.disableStandardItemLighting();
        ShulkerRender.mc.getRenderItem().zLevel = 0.0f;
    }
}
