// 
// Decompiled by Procyon v0.5.36
// 

package me.gavin.notorious.event.events;

import net.minecraft.entity.player.EntityPlayer;
import java.util.UUID;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ConnectionEvent extends Event
{
    private final UUID uuid;
    private final EntityPlayer entity;
    private final String name;
    
    public ConnectionEvent(final UUID uuid, final String name) {
        this.uuid = uuid;
        this.name = name;
        this.entity = null;
    }
    
    public ConnectionEvent(final EntityPlayer entity, final UUID uuid, final String name) {
        this.entity = entity;
        this.uuid = uuid;
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public UUID getUuid() {
        return this.uuid;
    }
    
    public EntityPlayer getEntity() {
        return this.entity;
    }
}
