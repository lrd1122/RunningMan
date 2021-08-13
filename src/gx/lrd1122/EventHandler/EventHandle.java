package gx.lrd1122.EventHandler;

import gx.lrd1122.GameStates.Game;
import gx.lrd1122.GameStates.PlayerState;
import gx.lrd1122.ReadConfig.LoadMap;
import gx.lrd1122.ReadConfig.LoadPlayerData;
import gx.lrd1122.RunningMan;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;

import java.util.List;

public class EventHandle implements Listener {
    public String ColorString(String string)
    {
        return string.replace("&", "§");
    }
    @EventHandler
    public void PlayerJoinGame(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        PlayerState.PlayerInFree.add(player.getName());
        LoadPlayerData.AddPlayerData(player);
        String[] stringloc = RunningMan.Config.getString("MainLobby").split(",");
        Location location = new Location(Bukkit.getWorld(stringloc[0]), Double.valueOf(stringloc[1]), Double.valueOf(stringloc[2]), Double.valueOf(stringloc[3]));
        LoadPlayerData.SetPlayerDefaultData(player, location);
        player.teleport(location);
        LoadPlayerData.LoadPlayerData(false);
    }
    @EventHandler
    public void PlayerHungryEvent(FoodLevelChangeEvent event)
    {
        event.setFoodLevel(10);
    }
    @EventHandler
    public void PlayerQuitGame(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();
        Game game = Game.getGameByName(player.getName());
        if(game != null)
        {
            List<String> players = game.getPlayers();
            for(int i = 0; i < players.size(); i ++)
            {
                if(players.get(i).equalsIgnoreCase(player.getName()))
                {
                    players.remove(i);
                }
            }
            game.setPlayers(players);
            if(game.getliveplayers().size() > 0 && game.getgamestate().equalsIgnoreCase("InGame"))
            {
                game.removeliveplayers(player.getName());
                for(int i = 0; i<game.getliveplayers().size(); i++)
                {
                    Bukkit.getPlayer(game.getliveplayers().get(i)).sendMessage(ColorString(RunningMan.Prefix + "&7离开了游戏, 剩余 &b" + game.getliveplayers().size() + "&7 人"));
                }
            }
        }
        PlayerState.ClearState(player.getName());
    }
    @EventHandler
    public void PlayerBreakBlock(BlockBreakEvent event)
    {
        if(!PlayerState.PlayerInEdit.contains(event.getPlayer().getName())) {
            event.getPlayer().sendMessage(ColorString(RunningMan.Prefix + "你破坏个几把"));
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void PlayerPlaceBlock(BlockPlaceEvent event)
    {
        if(!PlayerState.PlayerInEdit.contains(event.getPlayer().getName())) {
            event.getPlayer().sendMessage(ColorString(RunningMan.Prefix + "你放置个几把"));
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void PlayerDamage(EntityDamageByEntityEvent event)
    {
        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (PlayerState.GetPlayerState(player.getName()).equals("Free") || PlayerState.GetPlayerState(player.getName()).equals("Wait"))
            {
                event.setDamage(0);
                player.setMaximumNoDamageTicks(0);
                player.setNoDamageTicks(0);
                Vector vector = event.getDamager().getLocation().getDirection();
                vector.setY(0.2);
                player.setVelocity(vector);
            }
            else
            {
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void PlayerGetDamage(EntityDamageEvent event)
    {
        event.setDamage(0);
    }
}
