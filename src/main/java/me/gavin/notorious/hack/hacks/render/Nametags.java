// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.hack.hacks.render;

import net.minecraft.client.renderer.RenderHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import me.gavin.notorious.util.RenderUtil;
import me.gavin.notorious.util.ColorUtil;
import java.awt.Color;
import net.minecraft.item.ItemStack;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.util.ProjectionUtil;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.Hack;

@RegisterHack(name = "Nametags", description = "ez", category = Hack.Category.Render)
public class Nametags extends Hack
{
    @RegisterSetting
    public final NumSetting scale;
    @RegisterSetting
    public final ColorSetting outlineColor;
    @RegisterSetting
    public final BooleanSetting rainbow;
    @RegisterSetting
    public final BooleanSetting armor;
    @RegisterSetting
    public final BooleanSetting items;
    
    public Nametags() {
        this.scale = new NumSetting("Scale", 2.5f, 1.0f, 5.0f, 0.1f);
        this.outlineColor = new ColorSetting("OutlineColor", 120, 81, 169, 255);
        this.rainbow = new BooleanSetting("Rainbow", true);
        this.armor = new BooleanSetting("Armor", false);
        this.items = new BooleanSetting("Items", true);
    }
    
    @SubscribeEvent
    public void onRender2d(final RenderGameOverlayEvent.Text event) {
        for (final EntityPlayer player : Nametags.mc.world.playerEntities) {
            if (player.equals((Object)Nametags.mc.player)) {
                continue;
            }
            GlStateManager.pushMatrix();
            final double yAdd = player.isSneaking() ? 1.75 : 2.25;
            final double deltaX = MathHelper.clampedLerp(player.lastTickPosX, player.posX, (double)event.getPartialTicks());
            final double deltaY = MathHelper.clampedLerp(player.lastTickPosY, player.posY, (double)event.getPartialTicks());
            final double deltaZ = MathHelper.clampedLerp(player.lastTickPosZ, player.posZ, (double)event.getPartialTicks());
            final Vec3d projection = ProjectionUtil.toScaledScreenPos(new Vec3d(deltaX, deltaY, deltaZ).add(0.0, yAdd, 0.0));
            GlStateManager.translate(projection.x, projection.y, 0.0);
            GlStateManager.scale(this.scale.getValue(), this.scale.getValue(), 0.0f);
            int ping = -1;
            if (Nametags.mc.getConnection() != null) {
                ping = Nametags.mc.getConnection().getPlayerInfo(player.getUniqueID()).getResponseTime();
            }
            final double health = player.getHealth() + player.getAbsorptionAmount();
            String str = "";
            str = str + ChatFormatting.GRAY + "" + ping + "ms " + ChatFormatting.RESET;
            str += player.getName();
            str = str + " " + this.getHealthColor(health) + String.format("%.1f", health);
            if (this.armor.isEnabled()) {
                int xOffset = 1;
                for (final ItemStack stack : player.getArmorInventoryList()) {
                    if (stack == null) {
                        continue;
                    }
                    final ItemStack armourStack = stack.copy();
                    this.renderItem(armourStack, xOffset + 10, -26);
                    xOffset -= 13;
                }
            }
            Nametags.mc.player.getName();
            RenderUtil.drawBorderedRect(-((Nametags.mc.fontRenderer.getStringWidth(str) + 2) / 2.0f), (float)(-(Nametags.mc.fontRenderer.FONT_HEIGHT + 2)), (float)((Nametags.mc.fontRenderer.getStringWidth(str) + 2) / 2), 1.0f, new Color(0, 0, 0, 125).getRGB(), this.rainbow.getValue() ? ColorUtil.getRainbow(6.0f, 1.0f) : this.outlineColor.getAsColor().getRGB());
            Nametags.mc.fontRenderer.drawStringWithShadow(str, -(Nametags.mc.fontRenderer.getStringWidth(str) / 2.0f), (float)(-Nametags.mc.fontRenderer.FONT_HEIGHT), -1);
            GlStateManager.popMatrix();
        }
    }
    
    private void renderItem(final ItemStack itemStack, final int posX, final int posY) {
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
        GlStateManager.clear(256);
        GlStateManager.enableDepth();
        GlStateManager.disableAlpha();
        Nametags.mc.getRenderItem().zLevel = -150.0f;
        RenderHelper.enableStandardItemLighting();
        Nametags.mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, posX, posY);
        Nametags.mc.getRenderItem().renderItemOverlays(Nametags.mc.fontRenderer, itemStack, posX, posY);
        RenderHelper.disableStandardItemLighting();
        Nametags.mc.getRenderItem().zLevel = 0.0f;
    }
    
    private ChatFormatting getHealthColor(final double health) {
        if (health >= 15.0) {
            return ChatFormatting.GREEN;
        }
        if (health >= 10.0) {
            return ChatFormatting.YELLOW;
        }
        if (health >= 5.0) {
            return ChatFormatting.RED;
        }
        return ChatFormatting.DARK_RED;
    }
}
