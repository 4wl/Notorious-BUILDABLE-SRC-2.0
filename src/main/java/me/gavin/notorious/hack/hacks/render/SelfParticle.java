// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.hack.hacks.render;

import me.gavin.notorious.stuff.IMinecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.Hack;

@RegisterHack(name = "SelfParticle", description = "ez", category = Hack.Category.Render)
public class SelfParticle extends Hack
{
    Thread thread;
    
    @SubscribeEvent
    public void tick(final TickEvent.ClientTickEvent event) {
        if (SelfParticle.mc.world == null || SelfParticle.mc.player == null) {
            return;
        }
        if (this.thread != null) {
            try {
                this.thread.start();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void onEnable() {
        (this.thread = new Thread(TSelfParticle.getInstance(this))).start();
    }
    
    public void onDisable() {
        this.thread.stop();
    }
    
    private static class TSelfParticle implements Runnable
    {
        private static TSelfParticle instance;
        private SelfParticle selfParticle;
        
        public static TSelfParticle getInstance(final SelfParticle selfParticle) {
            if (TSelfParticle.instance == null) {
                TSelfParticle.instance = new TSelfParticle();
                TSelfParticle.instance.selfParticle = selfParticle;
            }
            return TSelfParticle.instance;
        }
        
        @Override
        public void run() {
            final int x = (int)(Math.random() * 5.0 + 0.0);
            final int y = (int)(Math.random() * 3.0 + 1.0);
            final int z = (int)(Math.random() * 5.0 - 1.0);
            final int particleId = (int)(Math.random() * 47.0 + 1.0);
            if (particleId != 1 && particleId != 2 && particleId != 41) {
                IMinecraft.mc.effectRenderer.spawnEffectParticle(particleId, IMinecraft.mc.player.posX + 1.5 + -x, IMinecraft.mc.player.posY + y, IMinecraft.mc.player.posZ + 1.5 + -z, 0.0, 0.5, 0.0, new int[] { 10 });
            }
        }
    }
}
