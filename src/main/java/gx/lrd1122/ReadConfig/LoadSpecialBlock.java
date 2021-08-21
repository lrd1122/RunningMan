package gx.lrd1122.ReadConfig;

import gx.lrd1122.GameStates.Game;
import gx.lrd1122.RunningMan;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LoadSpecialBlock {
    public static List<String> SpecialBlockList = new ArrayList<>();
    public static List<String> EndBlockList = new ArrayList<>();
    public static List<String> TimerBlockList = new ArrayList<>();

    public static void LoadSpecialBlockList()
    {
        JavaPlugin plugin = RunningMan.instance;
        File file = new File(plugin.getDataFolder(), "MapData");
        File[] MapFiles = file.listFiles();
        List<String> list = new ArrayList<>();
        if(MapFiles.length>0) {
            for (int i = 0; i < MapFiles.length; i++) {
                YamlConfiguration config = YamlConfiguration.loadConfiguration(MapFiles[i]);
                List<String> strings = config.getStringList("SpecialBlocks");
                for(int d = 0 ; d<strings.size(); d++)
                {
                    list.add(strings.get(d));
                }
            }
        }
        SpecialBlockList = list;
    }
    public static void LoadEndBlockList()
    {
        JavaPlugin plugin = RunningMan.instance;
        File file = new File(plugin.getDataFolder(), "MapData");
        File[] MapFiles = file.listFiles();
        List<String> list = new ArrayList<>();
        if(MapFiles.length>0) {
            for (int i = 0; i < MapFiles.length; i++) {
                YamlConfiguration config = YamlConfiguration.loadConfiguration(MapFiles[i]);
                String string = config.getString("EndLocation");
                list.add(string);
            }
        }
        EndBlockList = list;
    }
    public static void LoadTimerBlockList()
    {
        JavaPlugin plugin = RunningMan.instance;
        File file = new File(plugin.getDataFolder(), "MapData");
        File[] MapFiles = file.listFiles();
        if(MapFiles.length>0) {
            for (int i = 0; i < MapFiles.length; i++) {
                YamlConfiguration config = YamlConfiguration.loadConfiguration(MapFiles[i]);
                List<String> strings = config.getStringList("SpecialBlocks");
                for(int d = 0 ; d<strings.size(); d++)
                {
                    if(strings.get(d).contains("timer"))
                    {
                        Game game = Game.getGameByGameName(MapFiles[i].getName());
                        game.addtimerblock(strings.get(d));
                    }
                }
            }
        }
    }
}
