package gx.lrd1122.ReadConfig;

import gx.lrd1122.GameStates.Game;
import gx.lrd1122.RunningMan;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LoadMap {
    public static List<String> MapList = new ArrayList<>();
    public static List<Game> GameList = new ArrayList<>();
    public static void LoadMapConfig(boolean sendLog) {
        JavaPlugin plugin = RunningMan.instance;
        File file = new File(plugin.getDataFolder(), "MapData");
        if (!file.exists()) {
            file.mkdir();
            if(sendLog) {
                plugin.getLogger().info("正在创建MapData文件夹");
            }
        }
        if (file.exists()) {
            if(sendLog) {
                plugin.getLogger().info("已找到MapData文件夹 √");
            }
        }
        File[] MapFiles = file.listFiles();
        if(MapFiles.length>0) {
            List<Game> list = new ArrayList<>();
            List<String> maps = new ArrayList<>();
            for (int i = 0; i < MapFiles.length; i++) {
                if(sendLog) {
                    plugin.getLogger().info("正在加载地图: " + MapFiles[i].getName());
                }
                FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(MapFiles[i]);
                maps.add(MapFiles[i].getName().replace(".yml", ""));
                Game game = new Game();
                game.setGametime(0);
                game.setBlocktime(0);
                game.setName(MapFiles[i].getName());
                game.setCountdownMax(RunningMan.Config.getInt("CountDown"));
                game.setgamestate("Wait");
                game.setCountdownnow(RunningMan.Config.getInt("CountDown"));
                game.setPlayers(new ArrayList<>());
                game.setliveplayers(new ArrayList<>());
                game.setTouchDieBlock(new ArrayList<>());
                game.setTimerblocklist(new ArrayList<>());
                game.setMaxplayers(0);
                if(fileConfiguration.getString("EndLocation") != null) {
                    String[] loc = fileConfiguration.getString("EndLocation").split(",");
                    Location location = new Location(Bukkit.getWorld(loc[0]), Double.valueOf(loc[1]), Double.valueOf(loc[2]), Double.valueOf(loc[3]));
                    game.setEndlocation(location);
                }
                for(int e = 0; e < fileConfiguration.getStringList("TouchDieBlock").size(); e++)
                {
                    String string = fileConfiguration.getStringList("TouchDieBlock").get(e);
                    game.addTouchDieBlock(string);
                }
                list.add(game);
            }
            MapList = maps;
            GameList = list;
        }
    }


}
