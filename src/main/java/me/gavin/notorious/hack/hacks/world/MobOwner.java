// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.hack.hacks.world;

import net.minecraft.client.renderer.GlStateManager;
import me.gavin.notorious.util.ProjectionUtil;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import me.gavin.notorious.util.UUIDResolver;
import net.minecraft.entity.Entity;
import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import net.minecraft.entity.passive.EntityTameable;
import java.util.HashMap;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.Hack;

@RegisterHack(name = "MobOwner", description = "Shows you who owns the mob", category = Hack.Category.World)
public class MobOwner extends Hack
{
    private final HashMap<EntityTameable, String> resolvedEntities;
    
    public MobOwner() {
        this.resolvedEntities = new HashMap<EntityTameable, String>();
    }

    @SubscribeEvent
    public void onTick(PlayerLivingUpdateEvent event) {
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityTameable) {
                final EntityTameable tameable = (EntityTameable) entity;
                if (tameable.getOwnerId() != null) {
                    final String ownerUUID = tameable.getOwnerId().toString();
                    if (!resolvedEntities.containsKey(tameable)) {
                        resolvedEntities.put(tameable, null);
                        new Thread(() -> resolvedEntities.put(tameable, UUIDResolver.resolveName(ownerUUID))).start();
                    }
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onRender(final RenderGameOverlayEvent.Text event) {
        for (final Entity entity : MobOwner.mc.world.loadedEntityList) {
            if (entity instanceof EntityTameable) {
                final EntityTameable entityTameable = (EntityTameable)entity;
                if (entityTameable.getOwnerId() == null || !this.resolvedEntities.containsKey(entityTameable)) {
                    continue;
                }
                String s;
                if (this.resolvedEntities.get(entityTameable) != null) {
                    s = this.resolvedEntities.get(entityTameable);
                }
                else {
                    s = entityTameable.getOwnerId().toString();
                }
                final double lerpX = MathHelper.clampedLerp(entityTameable.lastTickPosX, entityTameable.posX, (double)event.getPartialTicks());
                final double lerpY = MathHelper.clampedLerp(entityTameable.lastTickPosY, entityTameable.posY, (double)event.getPartialTicks());
                final double lerpZ = MathHelper.clampedLerp(entityTameable.lastTickPosZ, entityTameable.posZ, (double)event.getPartialTicks());
                final Vec3d projection = ProjectionUtil.toScaledScreenPos(new Vec3d(lerpX, lerpY + entityTameable.height, lerpZ));
                GlStateManager.pushMatrix();
                GlStateManager.translate(projection.x, projection.y, 0.0);
                GlStateManager.scale(0.85f, 0.85f, 0.0f);
                MobOwner.mc.fontRenderer.drawStringWithShadow(s, -(MobOwner.mc.fontRenderer.getStringWidth(s) / 2.0f), (float)(-MobOwner.mc.fontRenderer.FONT_HEIGHT), -1);
                GlStateManager.popMatrix();
            }
        }
    }
}
