package gx.lrd1122.EditMap;

import gx.lrd1122.ReadConfig.WriteData;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class SetLobbyLocation{
    public static void SetLobbyLocation(String Map, Location location)
    {
        String locworld = location.getWorld().getName();
        Vector getvector = location.getBlock().getLocation().toVector();
        WriteData.WriteStringData(Map, "LobbyLocation", locworld + "," + getvector);
    }
}
