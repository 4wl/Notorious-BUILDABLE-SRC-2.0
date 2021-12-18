// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.hack.hacks.render;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.Hack;

@RegisterHack(name = "HellenKeller", description = "", category = Hack.Category.Render)
public class HellenKeller extends Hack
{
    @Override
    public String getMetaData() {
        return " [" + ChatFormatting.GRAY + "Blind" + ChatFormatting.RESET + "]";
    }
    
    @SubscribeEvent
    public void onRender(final RenderGameOverlayEvent.Chat event) {
        GlStateManager.pushMatrix();
        Gui.drawRect(0, 0, HellenKeller.mc.displayWidth, HellenKeller.mc.displayHeight, new Color(0, 0, 0, 255).getRGB());
        GlStateManager.popMatrix();
    }
}
