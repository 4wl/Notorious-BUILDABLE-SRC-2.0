// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.gui.setting;

import org.lwjgl.input.Keyboard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import me.gavin.notorious.util.ColorUtil;
import me.gavin.notorious.hack.hacks.client.ClickGUI;
import me.gavin.notorious.Notorious;
import me.gavin.notorious.hack.hacks.client.Font;
import me.gavin.notorious.gui.api.Bindable;
import me.gavin.notorious.gui.api.SettingComponent;

public class KeybindComponent extends SettingComponent
{
    private final Bindable setting;
    private boolean listening;
    
    public KeybindComponent(final Bindable setting, final int x, final int y, final int width, final int height) {
        super(x, y, width, height);
        this.listening = false;
        this.setting = setting;
    }
    
    @Override
    public void render(final int mouseX, final int mouseY, final float partialTicks) {
        final Font font = Notorious.INSTANCE.hackManager.getHack(Font.class);
        final float time = Notorious.INSTANCE.hackManager.getHack(ClickGUI.class).length.getValue();
        final float saturation = Notorious.INSTANCE.hackManager.getHack(ClickGUI.class).saturation.getValue();
        int color;
        if (Notorious.INSTANCE.hackManager.getHack(ClickGUI.class).colorMode.getMode().equals("Rainbow")) {
            color = ColorUtil.getRainbow(time, saturation);
        }
        else {
            color = Notorious.INSTANCE.hackManager.getHack(ClickGUI.class).guiColor.getAsColor().getRGB();
        }
        Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, new Color(0, 0, 0, (int)Notorious.INSTANCE.hackManager.getHack(ClickGUI.class).backgroundAlpha.getValue()).getRGB());
        Gui.drawRect(this.x, this.y, this.x + 2, this.y + this.height, color);
        if (this.listening) {
            if (font.isEnabled()) {
                Notorious.INSTANCE.fontRenderer.drawStringWithShadow("Bind: Listening...", this.x + 9.0f, this.y + 3.0f, Color.WHITE);
            }
            else {
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("Bind: Listening...", this.x + 9.0f, this.y + 1.0f, new Color(255, 255, 255).getRGB());
            }
        }
        else if (font.isEnabled()) {
            Notorious.INSTANCE.fontRenderer.drawStringWithShadow("Bind: <" + Keyboard.getKeyName(this.setting.getBind()) + ">", this.x + 9.0f, this.y + 3.0f, Color.WHITE);
        }
        else {
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("Bind: <" + Keyboard.getKeyName(this.setting.getBind()) + ">", this.x + 9.0f, this.y + 1.0f, new Color(255, 255, 255).getRGB());
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (this.isMouseInside(mouseX, mouseY) && mouseButton == 0) {
            this.listening = !this.listening;
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int mouseButton) {
    }
    
    @Override
    public void keyTyped(final char keyChar, final int keyCode) {
        if (this.listening) {
            this.listening = false;
            if (keyCode == 211 || keyCode == 14) {
                this.setting.setBind(0);
                return;
            }
            this.setting.setBind(keyCode);
        }
    }
    
    @Override
    public int getTotalHeight() {
        return this.height;
    }
}
