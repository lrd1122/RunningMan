package gx.lrd1122.ReadConfig;

import gx.lrd1122.GameStates.Game;
import gx.lrd1122.GameStates.PlayerData;
import gx.lrd1122.RunningMan;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoadPlayerData {
    public static List<PlayerData> playerdatalist = new ArrayList<>();
    public static void LoadPlayerData(boolean sendLogger)
    {
        JavaPlugin plugin = RunningMan.instance;
        File file = new File(plugin.getDataFolder(), "PlayerData");
        if (!file.exists()) {
            file.mkdir();
            if(sendLogger) {
                plugin.getLogger().info("正在创建PlayerData文件夹");
            }
        }
        if (file.exists()) {
            if(sendLogger) {
                plugin.getLogger().info("已找到PlayerData文件夹 √");
            }
        }
        File[] DataFiles = file.listFiles();
        if(DataFiles.length>0) {
            for (int i = 0; i < DataFiles.length; i++) {
                PlayerData data = new PlayerData();
                data.setName(DataFiles[i].getName().replace(".yml", ""));
                YamlConfiguration config = YamlConfiguration.loadConfiguration(DataFiles[i]);
                String[] locationname = config.getString("RespawnLocation").split(",");
                Location location = new Location(Bukkit.getWorld(locationname[0]), Double.valueOf(locationname[1]), Double.valueOf(locationname[2]), Double.valueOf(locationname[3]));
                data.setLocation(location);
                playerdatalist.add(data);
            }
        }
    }
    public static void AddPlayerData(Player player)
    {
        JavaPlugin plugin = RunningMan.instance;
        File file = new File(plugin.getDataFolder(), "PlayerData");
        File[] DataFiles = file.listFiles();
                File playerfile = new File(plugin.getDataFolder() + "\\PlayerData", player.getName() + ".yml");
                if(!playerfile.exists())
                {
                    try {
                        playerfile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
        }
    }
    public static void SetPlayerDefaultData(Player player, Location location) {
        JavaPlugin plugin = RunningMan.instance;
        File file = new File(plugin.getDataFolder(), "PlayerData");
        File[] DataFiles = file.listFiles();
        File playerfile = new File(plugin.getDataFolder() + "\\PlayerData", player.getName() + ".yml");
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(playerfile);
        String save = location.getWorld().getName() + "," + location.toBlockLocation().toVector();
        configuration.set("RespawnLocation", save);
        try {
            configuration.save(playerfile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
