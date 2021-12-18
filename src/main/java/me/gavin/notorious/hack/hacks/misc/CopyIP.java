// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.hack.hacks.misc;

import net.minecraft.client.multiplayer.ServerData;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.Hack;

@RegisterHack(name = "CopyIP", description = "Copies the current server IP to clipboard", category = Hack.Category.Misc)
public class CopyIP extends Hack
{
    @Override
    protected void onEnable() {
        if (CopyIP.mc.getConnection() != null && CopyIP.mc.getCurrentServerData() != null && CopyIP.mc.getCurrentServerData().serverIP != null) {
            final ServerData data = CopyIP.mc.getCurrentServerData();
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(data.serverIP), null);
            this.notorious.messageManager.sendMessage("Copied IP " + ChatFormatting.GRAY + "[" + ChatFormatting.GREEN + data.serverIP + ChatFormatting.GRAY + "]" + ChatFormatting.RESET + " to clipboard");
        }
        else {
            this.notorious.messageManager.sendError("Unable to copy server IP.");
        }
        this.disable();
    }
}
