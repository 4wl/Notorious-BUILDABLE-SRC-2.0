// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.hack.hacks.misc;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.init.MobEffects;
import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.Hack;

@RegisterHack(name = "WeaknessLog", description = "ez", category = Hack.Category.Misc)
public class WeaknessLog extends Hack
{
    @RegisterSetting
    public final BooleanSetting announce;
    
    public WeaknessLog() {
        this.announce = new BooleanSetting("Announce", true);
    }
    
    @SubscribeEvent
    public void onUpdate(final PlayerLivingUpdateEvent event) {
        if (WeaknessLog.mc.player.isPotionActive(MobEffects.WEAKNESS)) {
            if (this.announce.isEnabled()) {
                WeaknessLog.mc.player.sendChatMessage("Stop weaknessing retard");
            }
            WeaknessLog.mc.getConnection().handleDisconnect(new SPacketDisconnect((ITextComponent)new TextComponentString("Ew you got weaknessed")));
            this.toggle();
        }
    }
}
