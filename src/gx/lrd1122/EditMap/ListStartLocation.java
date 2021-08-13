package gx.lrd1122.EditMap;

import gx.lrd1122.RunningMan;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.List;

public class ListStartLocation {
    public static List<String> ListStartLocation(String Map)
    {
        Plugin plugin = RunningMan.instance;
        File file = new File(plugin.getDataFolder() + "\\MapData", Map + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        return config.getStringList("StartLocation");
    }
}
