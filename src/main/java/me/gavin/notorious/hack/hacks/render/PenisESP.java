// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.hack.hacks.render;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import me.gavin.notorious.util.RenderUtil;
import net.minecraft.client.renderer.RenderHelper;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.Hack;

@RegisterHack(name = "PenisESP", description = "ESP for your penis.", category = Hack.Category.Render)
public class PenisESP extends Hack
{
    private float pspin;
    private float pcumsize;
    private float pamount;
    
    public void onEnable() {
        this.pspin = 0.0f;
        this.pcumsize = 0.0f;
        this.pamount = 0.0f;
    }
    
    @SubscribeEvent
    public void render(final RenderWorldLastEvent event) {
        for (final Object o : PenisESP.mc.world.loadedEntityList) {
            if (o instanceof EntityPlayer) {
                final EntityPlayer player = (EntityPlayer)o;
                final double x2 = player.lastTickPosX + (player.posX - player.lastTickPosX) * PenisESP.mc.getRenderPartialTicks();
                final double x3 = x2 - PenisESP.mc.getRenderManager().viewerPosX;
                final double y2 = player.lastTickPosY + (player.posY - player.lastTickPosY) * PenisESP.mc.getRenderPartialTicks();
                final double y3 = y2 - PenisESP.mc.getRenderManager().viewerPosY;
                final double z2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * PenisESP.mc.getRenderPartialTicks();
                final double z3 = z2 - PenisESP.mc.getRenderManager().viewerPosZ;
                GL11.glPushMatrix();
                RenderHelper.disableStandardItemLighting();
                RenderUtil.drawPenis(player, x3, y3, z3, this.pspin, this.pcumsize, this.pamount);
                RenderHelper.enableStandardItemLighting();
                GL11.glPopMatrix();
            }
        }
    }
}
