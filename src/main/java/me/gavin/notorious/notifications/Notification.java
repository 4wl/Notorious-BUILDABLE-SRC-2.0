// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.notifications;

import net.minecraft.client.gui.Gui;
import java.awt.Color;
import me.gavin.notorious.Notorious;
import net.minecraft.client.gui.ScaledResolution;
import me.gavin.notorious.stuff.IMinecraft;

public class Notification implements IMinecraft
{
    public String title;
    public String message;
    public NotificationType type;
    public long start;
    
    public Notification(final String title, final String message, final NotificationType type) {
        this.title = title;
        this.message = message;
        this.type = type;
    }
    
    public void show() {
        this.start = System.currentTimeMillis();
    }
    
    public void draw() {
        final int width = Notification.mc.fontRenderer.getStringWidth(this.message) + 2;
        final ScaledResolution sr = new ScaledResolution(Notification.mc);
        if (Notorious.INSTANCE.hackManager.getHack(me.gavin.notorious.hack.hacks.client.Notification.class).style.getMode().equals("Skeet")) {
            Gui.drawRect(sr.getScaledWidth() - width, sr.getScaledHeight() - 40, sr.getScaledWidth(), sr.getScaledHeight() - 5, new Color(0, 0, 0, 255).getRGB());
            Gui.drawRect(sr.getScaledWidth() - width, sr.getScaledHeight() - 40, sr.getScaledWidth() - width + 5, sr.getScaledHeight() - 5, this.type.color.getRGB());
        }
        else {
            Gui.drawRect(sr.getScaledWidth() - width, sr.getScaledHeight() - 20, sr.getScaledWidth(), sr.getScaledHeight() - 5, 2130706432);
        }
        if (Notorious.INSTANCE.hackManager.getHack(me.gavin.notorious.hack.hacks.client.Notification.class).style.getMode().equals("Skeet")) {
            Notification.mc.fontRenderer.drawString(this.title, sr.getScaledWidth() - width + 8, sr.getScaledHeight() - 2 - 35, -1);
            Notification.mc.fontRenderer.drawString(this.message, sr.getScaledWidth() - width + 8, sr.getScaledHeight() - 15, -1);
        }
        else {
            Notification.mc.fontRenderer.drawString(this.message, sr.getScaledWidth() - width + 4, sr.getScaledHeight() - 15, -1);
        }
    }
    
    public long timeLeft() {
        return System.currentTimeMillis() - this.start;
    }
    
    public boolean expired() {
        return this.timeLeft() > 2000L;
    }
}
