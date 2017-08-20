package de.janhecker.parship;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ParshipInventory {

    private Main main;
    private Player player;
    private boolean isMember;
    private Profile profile;

    public ParshipInventory(Main main, Player player) {
        this.main = main;
        this.player = player;
        if (!player.hasPermission("parship")) {
            player.sendMessage("§cYou don't have the permission to create or view your profile.");
            return;
        }
        if (main.isOnline()) {
            this.isMember = main.isMember(player.getUniqueId());
            this.profile = main.getMap().get(player.getUniqueId());
        } else {
            this.isMember = main.isMember(player.getName());
            this.profile = main.getOffMap().get(player.getName());
        }
    }

    private void openInventory() {
        Inventory inv = Bukkit.createInventory(null, 3 * 9, main.getPrefix());

        ItemStack info = new ItemStack(Material.NETHER_STAR);
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.setDisplayName("&l&nInformation");
        List<String> infoLore = new ArrayList<>();
        infoLore.add("§r§7- Name:§e " + profile.getName());
        infoLore.add("§r§7- Age:§e " + profile.getAge());
        infoLore.add("§r§7- Gender:§e " + profile.getGender());
        infoLore.add("§r§7- Language:§e " + main.formatLanguage(profile.getLanguage()));
        infoLore.add("§r§7- Status:§e " + profile.getStatus().toString());
        infoLore.add("§r§7- Description:§e " + profile.getDescription());
        info.setItemMeta(infoMeta);

        player.openInventory(inv);
    }

}
