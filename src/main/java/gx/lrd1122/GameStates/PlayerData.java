package gx.lrd1122.GameStates;

import gx.lrd1122.ReadConfig.LoadPlayerData;
import org.bukkit.Location;


public class PlayerData {
    private String name;
    private Location location;
    private int wins;
    private int loses;
    private int dies;
    private int deathingame;
    private boolean showdeathingame;
    public PlayerData()
    {

    }
    public PlayerData(String name,Location location,int wins,int deathingame,boolean showdeathingame, int loses
    ,int dies)
    {
        this.location = location;
        this.name = name;
        this.wins = wins;
        this.deathingame = deathingame;
        this.showdeathingame = showdeathingame;
        this.loses = loses;
        this.dies = dies;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getDeathingame() {
        return deathingame;
    }

    public void setDeathingame(int deathingame) {
        this.deathingame = deathingame;
    }

    public void setShowdeathingame(boolean showdeathingame) {
        this.showdeathingame = showdeathingame;
    }

    public boolean isShowdeathingame() {
        return showdeathingame;
    }

    public int getDies() {
        return dies;
    }

    public void setDies(int dies) {
        this.dies = dies;
    }

    public int getLoses() {
        return loses;
    }

    public void setLoses(int loses) {
        this.loses = loses;
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
