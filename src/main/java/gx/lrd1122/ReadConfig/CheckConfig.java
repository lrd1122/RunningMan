package gx.lrd1122.ReadConfig;

import gx.lrd1122.RunningMan;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class CheckConfig {

    public static void CheckPluginConfig()
    {
        File file = new File(RunningMan.instance.getDataFolder(), "config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        if(config.get("CountDown") == null)
        {
            config.set("CountDown", "60");
        }
        if(config.get("MainLobby") == null)
        {
            config.set("MainLobby", "world,0,100,0");
        }
        if(config.get("PVPInLobby") == null)
        {
            config.set("PVPInLobby", false);
        }
        if(config.get("PVPInGame") == null)
        {
            config.set("PVPInGame", false);
        }
        if(config.get("RemoveDamage") == null)
        {
            config.set("RemoveDamage", true);
        }
        if(config.get("LockHunger") == null)
        {
            config.set("LockHunger", true);
        }
        if(config.get("LockHunger") == null)
        {
            config.set("LockHunger", true);
        }
        if(config.get("Bungeecord") == null)
        {
            config.set("Bungeecord", false);
        }
        if(config.get("FallBackServer") == null)
        {
            config.set("FallBackServer", "lobby");
        }
        if(config.get("AutoJoin") == null)
        {
            config.set("AutoJoin", "nether");
        }
        if(config.get("GameTools") == null)
        {
            config.createSection("GameTools");
        }
        ConfigurationSection section = config.getConfigurationSection("GameTools");
        if(section.get("PunishStick") == null)
        {
            section.set("PunishStick", true);
        }
        if(section.get("PunishBow") == null)
        {
            section.set("PunishBow", true);
        }
        if(section.get("SpeedCarrot") == null)
        {
            section.set("SpeedCarrot", true);
        }
        if(section.get("RottenFlesh") == null)
        {
            section.set("RottenFlesh", true);
        }
        try {
            config.save(file);
        }
        catch (IOException e)
        {

        }

    }
    public static void CheckPluginMessage()
    {
        try {

            YamlConfiguration message = YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(new File(RunningMan.instance.getDataFolder(), "message.yml")), StandardCharsets.UTF_8));
            if (message.get("Prefix") == null) {
                message.set("Prefix", "&7[RunningMan]");
            }
            if (message.get("NoBuild") == null) {
                message.set("NoBuild", "&c?????????????????????");
            }
            if (message.get("NoBreak") == null) {
                message.set("NoBreak", "&c?????????????????????");
            }
            if (message.get("DeleteBlockSuccess") == null) {
                message.set("DeleteBlockSuccess", "&a?????????????????????????????????????????????");
            }
            if (message.get("MapExist") == null) {
                message.set("MapExist", "&c???????????????");
            }
            if (message.get("CreateMap") == null) {
                message.set("CreateMap", "&a?????????????????????????????? <Map>");
            }
            if (message.get("MapNotFound") == null) {
                message.set("MapNotFound", "&c?????????????????? <Map>");
            }
            if (message.get("PlayerJoinGame") == null) {
                message.set("PlayerJoinGame", "?????? <player> ???????????????, ???????????? <number> ???");
            }
            if (message.get("PlayerHasJoin") == null) {
                message.set("PlayerHasJoin", "&c????????????????????????!");
            }
            if (message.get("NotInGame") == null) {
                message.set("NotInGame", "&c??????????????????!");
            }
            if (message.get("ExitGame") == null) {
                message.set("ExitGame", "&a??????????????????!");
            }
            if (message.get("DeleteMap") == null) {
                message.set("DeleteMap", "&a?????????????????? &b<Map>&a!");
            }
            if (message.get("GameHasStart") == null) {
                message.set("GameHasStart", "&c?????????????????????????????????????????????!");
            }
            if (message.get("GameHasFull") == null) {
                message.set("GameHasFull", "&c??????????????????");
            }
            if (message.get("ShowDeathInGameOpen") == null) {
                message.set("ShowDeathInGameOpen", "&a???????????????????????????");
            }
            if (message.get("ShowDeathInGameClose") == null) {
                message.set("ShowDeathInGameClose", "&a???????????????????????????");
            }
            if (message.get("GameTools") == null) {
                message.createSection("GameTools");
            }
            ConfigurationSection section = message.getConfigurationSection("GameTools");
            if (section.get("UsePunishStick") == null) {
                section.set("UsePunishStick", "&a????????????<player>");
            }
            if (section.get("GetPunishStick") == null) {
                section.set("GetPunishStick", "&a??????????????????");
            }
            if (section.get("UseSpeedCarrot") == null) {
                section.set("UseSpeedCarrot", "&a?????????????????????????????????");
            }
            if (section.get("UseRottenFlesh") == null) {
                section.set("UseRottenFlesh", "&a?????????????????????");
            }
            if (section.get("GetRottenFlesh") == null) {
                section.set("GetRottenFlesh", "&c???????????????????????????");
            }
                message.save(new File(RunningMan.instance.getDataFolder(), "message.yml"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
