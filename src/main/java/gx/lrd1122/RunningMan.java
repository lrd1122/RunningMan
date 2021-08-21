package gx.lrd1122;

import gx.lrd1122.EventHandler.BlockCheckEvent;
import gx.lrd1122.EventHandler.EventHandle;
import gx.lrd1122.EventHandler.ToolsInGame;
import gx.lrd1122.Lobby.LobbyMenu;
import gx.lrd1122.Lobby.LobbyMenuListen;
import gx.lrd1122.ReadConfig.*;
import gx.lrd1122.RunTasks.GameTask;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class RunningMan extends JavaPlugin{
    public static JavaPlugin instance;
    public static YamlConfiguration Config;
    public static YamlConfiguration MessageConfig;
    public static String Prefix;

    @Override
    public void onEnable() {
        instance=this;
        LoadConfig.LoadPluginConfig();
        LoadConfig.LoadPluginMessage();
        LoadMap.LoadMapConfig(true);
        LoadSpecialBlock.LoadSpecialBlockList();
        LoadSpecialBlock.LoadEndBlockList();
        LoadSpecialBlock.LoadTimerBlockList();
        LoadTools.LoadAvailableTools();
        LoadPlayerData.LoadPlayerData(true);
        Prefix = MessageConfig.getString("Prefix");
        Bukkit.getPluginManager().registerEvents(new EventHandle(), this);
        Bukkit.getPluginManager().registerEvents(new BlockCheckEvent(), this);
        Bukkit.getPluginManager().registerEvents(new ToolsInGame(), this);
        Bukkit.getPluginManager().registerEvents(new LobbyMenuListen(), this);
        Bukkit.getPluginCommand("runningman").setExecutor(new EditCommand());
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        BukkitTask gametask = new GameTask().runTaskTimer(this, 1L, 20L);
        getLogger().info("插件已成功加载,Bug反馈+QQ1794325461");
    }
    @Override
    public void onDisable() {
        getLogger().info("插件已成功卸载,Bug反馈+QQ1794325461");
    }
}
