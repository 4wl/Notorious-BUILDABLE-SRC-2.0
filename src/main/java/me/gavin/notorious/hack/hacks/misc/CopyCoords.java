// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.hack.hacks.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.Hack;

@RegisterHack(name = "CopyCoords", description = "ez", category = Hack.Category.Misc)
public class CopyCoords extends Hack
{
    @Override
    protected void onEnable() {
        if (CopyCoords.mc.player != null && CopyCoords.mc.world != null) {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(CopyCoords.mc.player.getPosition().toString()), null);
            this.notorious.messageManager.sendMessage("Copied Coords " + ChatFormatting.GRAY + "[" + ChatFormatting.GREEN + "X:" + CopyCoords.mc.player.getPosition().getX() + " Y:" + CopyCoords.mc.player.getPosition().getY() + " Z:" + CopyCoords.mc.player.getPosition().getZ() + ChatFormatting.GRAY + "]" + ChatFormatting.RESET + " to clipboard");
        }
        else {
            this.notorious.messageManager.sendError("Unable to copy coords?");
        }
        this.disable();
    }
}
