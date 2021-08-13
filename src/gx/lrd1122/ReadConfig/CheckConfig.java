package gx.lrd1122.ReadConfig;

import gx.lrd1122.RunningMan;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class CheckConfig {

    public static void CheckPluginConfig()
    {
        File file = new File(RunningMan.instance.getDataFolder(), "config.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if(config.get("CountDown") == null)
        {
            config.set("CountDown", "60");
        }
        if(config.get("MainLobby") == null)
        {
            config.set("MainLobby", "world,0,100,0");
        }
        try
        {
            config.save(file);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public static void CheckPluginMessage()
    {
        YamlConfiguration message = YamlConfiguration.loadConfiguration(new File(RunningMan.instance.getDataFolder(), "message.yml"));
        if(message.get("Prefix") == null)
        {
            message.set("Prefix", "&7[RunningMan]");
        }
        if(message.get("NoBuild") == null)
        {
            message.set("NoBuild", "&c你无法放置方块");
        }
        if(message.get("NoBreak") == null)
        {
            message.set("NoBreak", "&c你无法破坏方块");
        }
        if(message.get("DeleteBlockSuccess") == null)
        {
            message.set("DeleteBlockSuccess", "&a你成功的移除了方块上的特殊效果");
        }
        if(message.get("MapExist") == null)
        {
            message.set("MapExist", "&c地图已存在");
        }
        if(message.get("CreateMap") == null)
        {
            message.set("CreateMap", "&a你创建了一张新的地图 <Map>");
        }
        if(message.get("MapNotFound") == null)
        {
            message.set("MapNotFound", "&c没有找到地图 <Map>");
        }
        if(message.get("PlayerJoinGame") == null)
        {
            message.set("PlayerJoinGame", "玩家 <player> 加入了游戏, 目前人数 <number> 人");
        }
        if(message.get("NotInGame") == null)
        {
            message.set("NotInGame", "&c你不在游戏中!");
        }
        if(message.get("ExitGame") == null)
        {
            message.set("ExitGame", "&a你退出了游戏!");
        }
        if(message.get("GameHasStart") == null)
        {
            message.set("GameHasStart", "&c游戏已经开始，你无法再加入游戏!");
        }
        if(message.get("GameHasFull") == null)
        {
            message.set("GameHasFull", "&c游戏人数已满");
        }
        try
        {
            message.save(new File(RunningMan.instance.getDataFolder(), "message.yml"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
