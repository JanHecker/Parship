package de.janhecker.parship;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Profile {

    private Main main;
    private File file;
    private YamlConfiguration cfg;
    private Player player;
    private UUID uuid;
    private String name, ingameName, gender;
    private int age;
    private Status status;
    private String description;
    private String language; //todo
    private long created, updated;

    public Profile(Main main, Player player, String gender, String name, int age, Status status, String description) {
        this.main = main;
        this.player = player;
        if (main.isOnline()) {
            this.uuid = player.getUniqueId();
        }
        this.name = name;
        this.ingameName = player.getName();
        this.gender = gender;
        this.age = age;
        this.status = status;
        this.description = description;
        this.language = player.spigot().getLocale();
        if (main.isOnline()) {
            this.file = new File("plugins/Parship/members/" + uuid + ".yml");
        } else {
            this.file = new File("plugins/Parship/members/" + ingameName + ".yml");
        }
    }

    public Profile(Main main, UUID uuid) {
        if (Bukkit.getPlayer(uuid) != null) {
            this.player = Bukkit.getPlayer(uuid);
        }
        this.uuid = uuid;
        this.file = new File("plugins/Parship/members/" + uuid + ".yml");
        this.cfg = YamlConfiguration.loadConfiguration(file);
        this.name = cfg.getString("name");
        this.gender = cfg.getString("gender");
        this.ingameName = cfg.getString("ingame-name");
        this.created = cfg.getLong("created");
        this.updated = cfg.getLong("last-update");
        this.age = cfg.getInt("age");
        this.description = cfg.getString("description");
        this.status = Status.valueOf(cfg.getString("status"));
        this.language = cfg.getString("language");
    }

    public Profile(Main main, String ingameName) {
        if (Bukkit.getPlayer(ingameName) != null) {
            this.player = Bukkit.getPlayer(ingameName);
        }
        this.ingameName = ingameName;
        this.file = new File("plugins/Parship/members/" + ingameName + ".yml");
        this.cfg = YamlConfiguration.loadConfiguration(file);
        this.name = cfg.getString("name");
        this.created = cfg.getLong("created");
        this.gender = cfg.getString("gender");
        this.updated = cfg.getLong("last-update");
        this.age = cfg.getInt("age");
        this.description = cfg.getString("description");
        this.status = Status.valueOf(cfg.getString("status"));
        this.language = cfg.getString("language");
    }

    public void updateOrCreate() {
        boolean isMember;
        if (main.isOnline() && !main.getMembers().contains(uuid)) {
            isMember = false;
            main.getMembers().add(uuid);
        } else if (!main.isOnline() && !main.getOffMembers().contains(ingameName)) {
            isMember = false;
            main.getOffMembers().add(ingameName);
        } else {
            isMember = true;
        }
        if (isMember) {
            cfg = YamlConfiguration.loadConfiguration(file);
        } else {
            cfg = new YamlConfiguration();
        }
        if (main.isOnline()) {
            cfg.set("uuid", uuid);
        }
        cfg.set("created", System.currentTimeMillis());
        cfg.set("last-update", System.currentTimeMillis());
        cfg.set("ingame-name", ingameName);
        cfg.set("language", language);
        cfg.set("gender", gender);
        cfg.set("age", age);
        cfg.set("name", name);
        cfg.set("description", description);
        cfg.set("status", status.toString());
        saveFile();
    }

    private void saveFile() {
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Player getPlayer() {
        return player;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getLanguage() {
        return language;
    }

    public Status getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public long getCreated() {
        return created;
    }

    public long getUpdated() {
        return updated;
    }

    public String getGender() {
        return gender;
    }
}
