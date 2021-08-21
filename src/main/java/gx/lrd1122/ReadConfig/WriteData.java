package gx.lrd1122.ReadConfig;

import gx.lrd1122.RunningMan;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WriteData {
    public static void WriteStringData(String Map, String ValueKey, String Value)
    {
        Plugin plugin = RunningMan.instance;
        File file = new File(plugin.getDataFolder() + "\\MapData", Map + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        FileConfiguration fileConfiguration = config;
        fileConfiguration.set(ValueKey, Value);
        try {
            fileConfiguration.save(new File(plugin.getDataFolder() + "\\MapData", Map + ".yml"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void WriteStringListData(String Map, String ValueKey, String Value)
    {
        Plugin plugin = RunningMan.instance;
        File file = new File(plugin.getDataFolder() + "\\MapData", Map + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        FileConfiguration fileConfiguration = config;
        if(fileConfiguration.getStringList(ValueKey) == null) {
            List<String> strings = new ArrayList<>();
            strings.add(Value);
            fileConfiguration.set(ValueKey, strings);
        }
        else{
            List<String> strings = fileConfiguration.getStringList(ValueKey);
            strings.add(Value);
            fileConfiguration.set(ValueKey, strings);
        }
        try {
            fileConfiguration.save(new File(plugin.getDataFolder() + "\\MapData", Map + ".yml"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void RemoveStringListData(String Map, String ValueKey, int value)
    {
        Plugin plugin = RunningMan.instance;
        File file = new File(plugin.getDataFolder() + "\\MapData", Map + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        FileConfiguration fileConfiguration = config;
            List<String> strings = fileConfiguration.getStringList(ValueKey);
            strings.remove(value);
            fileConfiguration.set(ValueKey, strings);
        try {
            fileConfiguration.save(new File(plugin.getDataFolder() + "\\MapData", Map + ".yml"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void RemoveStringListData(String Map, String ValueKey, Location location1, Player player)
    {
        Plugin plugin = RunningMan.instance;
        File file = new File(plugin.getDataFolder() + "\\MapData", Map + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        FileConfiguration fileConfiguration = config;
        List<String> strings = fileConfiguration.getStringList(ValueKey);
        for(int i =0 ; i < strings.size(); i ++) {
            String[] name = strings.get(i).split(",");
            Location location2 = new Location(Bukkit.getWorld(name[0]), Double.valueOf(name[1]), Double.valueOf(name[2]), Double.valueOf(name[3]));
            if (location1.getBlock().getLocation().toVector().toString().equalsIgnoreCase(location2.getBlock().getLocation().toVector().toString())) {
                player.sendMessage(RunningMan.Prefix + RunningMan.MessageConfig.getString("DeleteBlockSuccess"));
                strings.remove(i);
            }
        }

        fileConfiguration.set(ValueKey, strings);
        try {
            fileConfiguration.save(new File(plugin.getDataFolder() + "\\MapData", Map + ".yml"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
