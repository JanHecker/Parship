package de.janhecker.parship;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Main extends JavaPlugin {

    private String prefix;
    private String entityName;
    private boolean online;
    private Entity entity;
    private File memberFile, file;
    private YamlConfiguration memberCfg, cfg;
    private ArrayList<UUID> members;
    private ArrayList<String> offMembers;
    private HashMap<UUID, Profile> map;
    private HashMap<String, Profile> offMap;
    private List<String> disabledWorlds;
    private Location spawn;

    @Override
    public void onEnable() {
        this.online = getServer().getOnlineMode();
        this.initCfg();
        this.initMembers();
        this.initMaps();
        this.loadListener();
        ParshipEntity parshipEntity = new ParshipEntity(this);
        this.entity = parshipEntity.getEntity();
        for (Player player : Bukkit.getOnlinePlayers()) {
            new Profile(this, player, "male", "Jan Hecker", 16, Status.LESBIAN, "bim single.").updateOrCreate();
        }
    }

    @Override
    public void onDisable() {
        if (entity != null) entity.remove();
        if (online) {
            if (!members.isEmpty()) memberCfg.set("members", members);
        } else {
            if (!offMembers.isEmpty()) memberCfg.set("members", offMembers);
        }
        saveMemberCfg();
    }

    public String getPrefix() {
        return prefix;
    }

    private void loadListener() {
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new ParshipListener(this), this);
    }

    public boolean isOnline() {
        return online;
    }

    private void initMaps() {
        if (online) {
            this.map = new HashMap<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                map.put(player.getUniqueId(), new Profile(this, player.getUniqueId()));
            }
        } else {
            this.offMap = new HashMap<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                offMap.put(player.getName(), new Profile(this, player.getName()));
            }
        }
    }

    private void initCfg() {
        file = new File("plugins/Parship/config.yml");
        if (file.isFile()) {
            cfg = YamlConfiguration.loadConfiguration(file);
        } else {
            cfg = new YamlConfiguration();
            cfg.options().header("Parship (Copyright 2017 - Jan Hecker)");
            cfg.set("prefix", "&7[&cParship&7]");
            cfg.set("entity-name", "&4- &c&lPARSHIP &r&4-");
            cfg.set("entity-spawn", new Location(Bukkit.getWorlds().get(0), -30, 94, 76, 0F, 0F));
            List<String> disabledWorlds = new ArrayList<>();
            disabledWorlds.add("world_nether");
            disabledWorlds.add("world_the_end");
            cfg.set("disabled-worlds", disabledWorlds);
            saveCfg();
        }
        this.prefix = getString("prefix") + " ";
        this.spawn = (Location) cfg.get("entity-spawn");
        this.disabledWorlds = cfg.getStringList("disabled-worlds");
        this.entityName = getString("entity-name");
    }

    public String getString(String path) {
        String tmp = cfg.getString(path);
        tmp = tmp.replace('&', '§');
        return tmp;
    }

    public void saveCfg() {
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initMembers() {
        if (online) {
            this.members = new ArrayList<>();
        } else {
            this.offMembers = new ArrayList<>();
        }
        this.memberFile = new File("plugins/Parship/members.yml");
        if (memberFile.isFile()) {
            this.memberCfg = YamlConfiguration.loadConfiguration(memberFile);
        } else {
            this.memberCfg = new YamlConfiguration();
            this.saveMemberCfg();
        }
    }

    public ArrayList<UUID> getMembers() {
        return members;
    }

    public ArrayList<String> getOffMembers() {
        return offMembers;
    }

    public YamlConfiguration getMemberCfg() {
        return memberCfg;
    }

    public Location getSpawn() {
        return spawn;
    }

    public List<String> getDisabledWorlds() {
        return disabledWorlds;
    }

    public String getEntityName() {
        return entityName;
    }

    public Entity getEntity() {
        return entity;
    }

    private void saveMemberCfg() {
        try {
            memberCfg.save(memberFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isMember(UUID uuid) {
        return members.contains(uuid) || memberCfg.getStringList("members").contains(uuid.toString());
    }

    public boolean isMember(String ingameName) {
        return offMembers.contains(ingameName) || memberCfg.getStringList("members").contains(ingameName);
    }

    public HashMap<UUID, Profile> getMap() {
        return map;
    }

    public HashMap<String, Profile> getOffMap() {
        return offMap;
    }

    public String formatLanguage(String language) {
        switch (language.toLowerCase()) {
            case "en_us":
                return "English";
            default:
                return "Other";
        }
    }

}
