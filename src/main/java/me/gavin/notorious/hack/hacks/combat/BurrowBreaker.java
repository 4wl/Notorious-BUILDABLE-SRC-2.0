// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.hack.hacks.combat;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import me.gavin.notorious.util.BlockUtil;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.init.Blocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.math.BlockPos;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.Hack;

@RegisterHack(name = "BurrowBreaker", description = "ez", category = Hack.Category.Combat)
public class BurrowBreaker extends Hack
{
    @RegisterSetting
    public final BooleanSetting packet;
    @RegisterSetting
    public final BooleanSetting rotate;
    private boolean isBreaking;
    public BlockPos pos;
    public List<BlockPos> burrowedEntities;
    
    public BurrowBreaker() {
        this.packet = new BooleanSetting("Packet", false);
        this.rotate = new BooleanSetting("Rotate", true);
        this.isBreaking = false;
        this.burrowedEntities = new ArrayList<BlockPos>();
    }
    
    @SubscribeEvent
    public void onUpdate(final PlayerLivingUpdateEvent event) {
        this.burrowedEntities.clear();
        for (final Entity e : BurrowBreaker.mc.world.loadedEntityList) {
            if (e instanceof EntityPlayer) {
                if (e.equals((Object)BurrowBreaker.mc.player)) {
                    return;
                }
                this.pos = new BlockPos(e.posX, e.posY + 0.2, e.posZ);
                if (BurrowBreaker.mc.world.getBlockState(this.pos).getBlock() == Blocks.OBSIDIAN || BurrowBreaker.mc.world.getBlockState(this.pos).getBlock() == Blocks.ENDER_CHEST) {
                    this.burrowedEntities.add(this.pos);
                }
                if (this.burrowedEntities.contains(this.pos) && BurrowBreaker.mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe && !this.isBreaking) {
                    BlockUtil.damageBlock(this.pos, this.packet.getValue(), this.rotate.getValue());
                    this.isBreaking = true;
                }
                if (BurrowBreaker.mc.world.getBlockState(this.pos).getBlock() != Blocks.AIR) {
                    continue;
                }
                this.isBreaking = false;
            }
        }
    }
}
