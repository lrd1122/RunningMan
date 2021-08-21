package gx.lrd1122.ReadConfig;

import gx.lrd1122.RunningMan;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class LoadConfig {
    public static void LoadPluginMessage()
    {
        JavaPlugin plugin = RunningMan.instance;
        File file = new File(plugin.getDataFolder(), "message.yml");
        if(!file.exists()) {
            plugin.saveResource("message.yml", true);
            plugin.getLogger().info("正在创建message文件");
        }
        if(file.exists())
        {
            plugin.getLogger().info("已找到message文件 √");
        }
        CheckConfig.CheckPluginMessage();
        RunningMan.MessageConfig = YamlConfiguration.loadConfiguration(file);
    }
    public static void LoadPluginConfig() {
        JavaPlugin plugin = RunningMan.instance;
        File file = new File(plugin.getDataFolder(), "config.yml");
        if (!file.exists()) {
            plugin.saveResource("config.yml", true);
            plugin.getLogger().info("正在创建config文件");
        }
        if (file.exists()) {
            plugin.getLogger().info("已找到config文件 √");
        }
        CheckConfig.CheckPluginConfig();
        RunningMan.Config = YamlConfiguration.loadConfiguration(file);
    }
}
