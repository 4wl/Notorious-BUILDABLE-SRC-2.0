// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.hack.hacks.chat;

import net.minecraft.item.ItemStack;
import me.gavin.notorious.util.ColorUtil;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.Hack;

@RegisterHack(name = "ArmorNotify", description = "ez", category = Hack.Category.Chat)
public class ArmorNotify extends Hack
{
    @RegisterSetting
    public final NumSetting x;
    @RegisterSetting
    public final NumSetting y;
    @RegisterSetting
    public final BooleanSetting rainbow;
    boolean hasAnnounced;
    
    public ArmorNotify() {
        this.x = new NumSetting("X", 2.0f, 0.1f, 1000.0f, 0.1f);
        this.y = new NumSetting("Y", 2.0f, 0.1f, 600.0f, 0.1f);
        this.rainbow = new BooleanSetting("Rainbow", true);
        this.hasAnnounced = false;
    }
    
    @SubscribeEvent
    public void onChat(final PlayerLivingUpdateEvent event) {
        for (final Entity e : ArmorNotify.mc.world.loadedEntityList) {
            final boolean armorDurability = this.getArmorDurability();
            if (e.equals((Object)ArmorNotify.mc.player)) {
                return;
            }
            if (!(e instanceof EntityPlayer) || !this.notorious.friend.isFriend(e.getName())) {
                continue;
            }
            if (armorDurability && !this.hasAnnounced) {
                ArmorNotify.mc.player.sendChatMessage("/msg " + ((EntityPlayer)e).getDisplayNameString() + " Hey bro you need to mend your armor :o");
                this.hasAnnounced = true;
            }
            if (!this.armorAboveSeventyFive()) {
                continue;
            }
            this.hasAnnounced = false;
        }
    }
    
    @SubscribeEvent
    public void onUpdate(final RenderGameOverlayEvent.Text event) {
        final boolean armorDurability = this.getArmorDurability();
        if (armorDurability) {
            ArmorNotify.mc.fontRenderer.drawStringWithShadow("Armor is below 50%", this.x.getValue(), this.y.getValue(), this.rainbow.getValue() ? ColorUtil.getRainbow(6.0f, 1.0f) : -1);
        }
    }
    
    private boolean getArmorDurability() {
        for (final ItemStack itemStack : ArmorNotify.mc.player.inventory.armorInventory) {
            if (itemStack.getMaxDamage() / 2 < itemStack.getItemDamage()) {
                return true;
            }
        }
        return false;
    }
    
    private boolean armorAboveSeventyFive() {
        for (final ItemStack itemStack : ArmorNotify.mc.player.inventory.armorInventory) {
            if (itemStack.getMaxDamage() / 3 < itemStack.getMaxDamage()) {
                return true;
            }
        }
        return false;
    }
}
