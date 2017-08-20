package de.janhecker.parship;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class ParshipListener implements Listener {

    private Main main;
    private String name;

    public ParshipListener(Main main) {
        this.main = main;
        this.name = main.getEntityName();
    }

    @EventHandler
    public void interact(PlayerInteractAtEntityEvent event) {
        Entity entity = event.getRightClicked();
        Player player = event.getPlayer();
        if (entity.getType() != EntityType.WITCH) return;
        if (!entity.getCustomName().equalsIgnoreCase(main.getEntityName())) return;
        if (player.isSneaking()) return;
        new ParshipInventory(main, player);
    }

    @EventHandler
    public void damage(EntityDamageByEntityEvent event) {
        if ((event.getEntity().getType() == EntityType.WITCH && event.getEntity().getCustomName().equalsIgnoreCase(name)) ||
                event.getDamager().getType() == EntityType.WITCH && event.getEntity().getCustomName().equalsIgnoreCase(name)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void splash(PotionSplashEvent event) {
        if (event.getEntity().getType() == EntityType.WITCH
                && event.getEntity().getCustomName().equalsIgnoreCase(main.getEntityName())) {
            event.setCancelled(true);
        }
    }

}
