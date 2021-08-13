package gx.lrd1122.EditMap;

import gx.lrd1122.ReadConfig.WriteData;
import gx.lrd1122.RunningMan;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;

public class SetStartLocation {
    public static void AddStartLocation(String Map, Location location)
    {
        String locworld = location.getWorld().getName();
        Vector getvector = location.toBlockLocation().toVector();
        WriteData.WriteStringListData(Map, "StartLocation", locworld + "," + getvector);
    }
}
