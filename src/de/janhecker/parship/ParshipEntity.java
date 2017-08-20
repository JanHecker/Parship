package de.janhecker.parship;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class ParshipEntity {

    private Entity entity;

    public ParshipEntity(Main main) {
        World world = main.getSpawn().getWorld();
        for (Entity entity : world.getEntities()) {
//            Validate.notNull(entity.getCustomName(), "entity.getCustomName is null");
//            Validate.notNull(main.getEntityName(), "main entityName is null");
            if (entity.getCustomName() == null) continue;
            if (entity.getCustomName().equalsIgnoreCase(main.getEntityName())) {
                entity.remove();
            }
        }
        entity = world.spawnEntity(main.getSpawn(), EntityType.WITCH);
        entity.setCustomNameVisible(true);
        entity.setCustomName(main.getEntityName());
        entity.setFireTicks(0);
        // T
        Bukkit.getScheduler().runTaskTimer(main, () -> entity.teleport(main.getSpawn()), 10, 10);
    }

    public Entity getEntity() {
        return entity;
    }
}
