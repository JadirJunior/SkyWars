package com.jadir.skywars.skywars.listeners;

import com.jadir.skywars.skywars.SkyWars;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class EntitySpawn implements Listener {

    @EventHandler
    public void onSpawn(EntitySpawnEvent e) {
        if (SkyWars.getInstance().isSingleServerMode())  {
            if (e.getEntityType() != EntityType.PLAYER && e.getEntityType() != EntityType.DROPPED_ITEM && e.getEntityType() != EntityType.PRIMED_TNT) {
                e.setCancelled(true);
            }
        }
    }
}
