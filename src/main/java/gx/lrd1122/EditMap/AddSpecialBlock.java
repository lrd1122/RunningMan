package gx.lrd1122.EditMap;

import gx.lrd1122.ReadConfig.WriteData;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class AddSpecialBlock {
    public static void AddSpecialBlock(String Map, Location location, String Argument)
    {
        String locworld = location.getWorld().getName();
        Vector getvector = location.getBlock().getLocation().toVector();
        WriteData.WriteStringListData(Map, "SpecialBlocks", locworld + "," + getvector + "," + Argument);
    }
}
