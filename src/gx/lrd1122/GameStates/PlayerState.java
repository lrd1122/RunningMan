package gx.lrd1122.GameStates;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerState {
    public static List<String> PlayerInFree = new ArrayList<>();
    public static List<String> PlayerInGame = new ArrayList<>();
    public static List<String> PlayerInEnd = new ArrayList<>();
    public static List<String> PlayerInWait = new ArrayList<>();
    public static List<String> PlayerInEdit = new ArrayList<>();
    public static void RemovePlayerInFree(Player player)
    {
        String name = player.getName();
        PlayerInFree.remove(name);
    }
    public static void RemovePlayerInGame(Player player)
    {
        String name = player.getName();
        PlayerInGame.remove(name);
    }
    public static void RemovePlayerInEnd(Player player)
    {
        String name = player.getName();
        PlayerInEnd.remove(name);
    }
    public static void RemovePlayerInWait(Player player)
    {
        String name = player.getName();
        PlayerInWait.remove(name);
    }
    public static String GetPlayerState(String PlayerName)
    {
        if(PlayerInFree.contains(PlayerName))
        {
            return "Free";
        }
        if(PlayerInEnd.contains(PlayerName))
        {
            return "Die";
        }
        if(PlayerInWait.contains(PlayerName))
        {
            return "Wait";
        }
        if(PlayerInGame.contains(PlayerName))
        {
            return "Game";
        }
        return "none";
    }
    public static void ClearState(String name) {
        try {
            PlayerInFree.remove(name);
            PlayerInGame.remove(name);
            PlayerInWait.remove(name);
            PlayerInEnd.remove(name);
        } catch (Exception e) {
        }
    }
}
