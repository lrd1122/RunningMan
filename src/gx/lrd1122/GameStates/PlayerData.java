package gx.lrd1122.GameStates;

import gx.lrd1122.ReadConfig.LoadPlayerData;
import org.bukkit.Location;
import org.bukkit.entity.Player;


public class PlayerData {
    private String name;
    private Location location;
    public PlayerData()
    {

    }
    public PlayerData(String name,Location location)
    {
        this.location = location;
        this.name = name;
    }
    public String getName()
    {
        return this.name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public void setLocation(Location location)
    {
        this.location = location;
    }
    public Location getLocation()
    {
        return this.location;
    }
    public static PlayerData getDataByName(String name)
    {
        for(int i = 0 ; i< LoadPlayerData.playerdatalist.size(); i++)
        {
            if(LoadPlayerData.playerdatalist.get(i).getName().equalsIgnoreCase(name))
            {
                return LoadPlayerData.playerdatalist.get(i);
            }
        }
        return null;
    }
}
