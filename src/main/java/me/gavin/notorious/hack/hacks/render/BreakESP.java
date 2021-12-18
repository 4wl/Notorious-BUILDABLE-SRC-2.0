// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.hack.hacks.render;

import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.math.AxisAlignedBB;
import java.awt.Color;
import me.gavin.notorious.util.RenderUtil;
import net.minecraft.world.World;
import me.gavin.notorious.util.ColorUtil;
import me.gavin.notorious.mixin.mixins.accessor.IRenderGlobal;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.ModeSetting;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.Hack;

@RegisterHack(name = "BreakESP", description = "shows break progress", category = Hack.Category.Render)
public class BreakESP extends Hack
{
    @RegisterSetting
    public final ModeSetting mode;
    @RegisterSetting
    public final ColorSetting outlineColor;
    @RegisterSetting
    public final ColorSetting boxColor;
    @RegisterSetting
    public final BooleanSetting rainbow;
    @RegisterSetting
    public final NumSetting saturation;
    @RegisterSetting
    public final NumSetting time;
    @RegisterSetting
    public final NumSetting range;
    @RegisterSetting
    public final BooleanSetting fade;
    boolean outline;
    boolean fill;
    
    public BreakESP() {
        this.mode = new ModeSetting("Mode", "Outline", new String[] { "Both", "Outline", "Box" });
        this.outlineColor = new ColorSetting("OutlineColor", 255, 255, 255, 125);
        this.boxColor = new ColorSetting("BoxColor", 255, 255, 255, 125);
        this.rainbow = new BooleanSetting("Rainbow", true);
        this.saturation = new NumSetting("Saturation", 0.6f, 0.1f, 1.0f, 0.1f);
        this.time = new NumSetting("RainbowLength", 8.0f, 1.0f, 15.0f, 1.0f);
        this.range = new NumSetting("Range", 15.0f, 1.0f, 20.0f, 1.0f);
        this.fade = new BooleanSetting("Fade", true);
        this.outline = false;
        this.fill = false;
    }
    
    @Override
    public String getMetaData() {
        return " [" + ChatFormatting.GRAY + this.mode.getMode() + ChatFormatting.RESET + "]";
    }
    
    @SubscribeEvent
    public void onRender(final RenderWorldLastEvent event) {
        ((IRenderGlobal)BreakESP.mc.renderGlobal).getDamagedBlocks().forEach((integer, destroyBlockProgress) -> {
            Color rainbowColor = ColorUtil.colorRainbow((int) time.getValue(), saturation.getValue(), 1f);
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
            if (destroyBlockProgress.getPosition().getDistance((int)BreakESP.mc.player.posX, (int)BreakESP.mc.player.posY, (int)BreakESP.mc.player.posZ) <= this.range.getValue()) {
                AxisAlignedBB pos = mc.world.getBlockState(destroyBlockProgress.getPosition()).getSelectedBoundingBox(mc.world, destroyBlockProgress.getPosition());
                pos = BreakESP.mc.world.getBlockState(destroyBlockProgress.getPosition()).getSelectedBoundingBox((World)BreakESP.mc.world, destroyBlockProgress.getPosition());
                if (this.fade.isEnabled()) {
                    pos = pos.shrink((3.0 - destroyBlockProgress.getPartialBlockDamage() / 2.6666666666666665) / 9.0);
                }
                if (this.outline) {
                    if (this.rainbow.isEnabled()) {
                        RenderUtil.renderOutlineBB(pos, rainbowColor);
                    }
                    else {
                        RenderUtil.renderOutlineBB(pos, this.outlineColor.getAsColor());
                    }
                }
                if (this.fill) {
                    if (this.rainbow.isEnabled()) {
                        RenderUtil.renderFilledBB(pos, rainbowColor);
                    }
                    else {
                        RenderUtil.renderFilledBB(pos, this.boxColor.getAsColor());
                    }
                }
            }
        });
    }
}
