package gx.lrd1122.EventHandler;

import gx.lrd1122.GameStates.Game;
import gx.lrd1122.GameStates.PlayerData;
import gx.lrd1122.GameStates.PlayerState;
import gx.lrd1122.ReadConfig.GetConfig;
import gx.lrd1122.ReadConfig.LoadPlayerData;
import gx.lrd1122.RunningMan;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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
        player.getInventory().clear();
        player.updateInventory();
        PlayerState.PlayerInFree.add(player.getName());
        LoadPlayerData.AddPlayerData(player);
        String[] stringloc = RunningMan.Config.getString("MainLobby").split(",");
        Location location = new Location(Bukkit.getWorld(stringloc[0]), Double.valueOf(stringloc[1]), Double.valueOf(stringloc[2]), Double.valueOf(stringloc[3]));
        LoadPlayerData.SetPlayerDefaultData(player, location);
        player.teleport(location);
        LoadPlayerData.LoadPlayerData(false);
        ItemStack item = new ItemStack(Material.CHEST);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§a加入游戏");
        item.setItemMeta(meta);
        player.getInventory().setItem(0, item);
        player.updateInventory();
        PlayerData.getDataByName(player.getName()).setDeathingame(0);
    }
    @EventHandler
    public void PlayerHungryEvent(FoodLevelChangeEvent event)
    {
        if(RunningMan.Config.getBoolean("LockHunger")) {
            event.setFoodLevel(20);
        }
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
            if(game.getgamestate().equalsIgnoreCase("InGame") && game.getPlayers().size() == game.getliveplayers().size() && game.getPlayers().size() == 1) {
                for (int e = 0; e < game.getPlayers().size(); e++) {
                    Bukkit.getPlayer(game.getPlayers().get(e)).sendMessage(ColorString(RunningMan.Prefix + "&c由于玩家人数不足,游戏已自动取消"));
                }
            }
        }
        PlayerState.ClearState(player.getName());
    }
    @EventHandler
    public void WeatherChange(WeatherChangeEvent event)
    {
        if(RunningMan.Config.getBoolean("NoRain"))
        {
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void PlayerBreakBlock(BlockBreakEvent event)
    {
        if(!PlayerState.PlayerInEdit.contains(event.getPlayer().getName())) {
            event.getPlayer().sendMessage(ColorString(RunningMan.Prefix + RunningMan.MessageConfig.getString("NoBreak")));
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void PlayerPlaceBlock(BlockPlaceEvent event)
    {
        if(!PlayerState.PlayerInEdit.contains(event.getPlayer().getName())) {
            event.getPlayer().sendMessage(ColorString(RunningMan.Prefix + RunningMan.MessageConfig.getString("NoBuild")));
            event.setCancelled(true);
        }
    }
    @EventHandler(priority = EventPriority.LOW)
    public void PlayerDamage(EntityDamageByEntityEvent event)
    {
        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (PlayerState.GetPlayerState(player.getName()).equals("Free") || PlayerState.GetPlayerState(player.getName()).equals("Wait")) {
                if (RunningMan.Config.getBoolean("PVPInLobby")) {
                    event.setDamage(0);
                    player.setMaximumNoDamageTicks(0);
                    player.setNoDamageTicks(0);
                    Vector vector = event.getDamager().getLocation().getDirection();
                    vector.setY(0.2);
                    player.setVelocity(vector);
                }
                if(!RunningMan.Config.getBoolean("PVPInLobby"))
                {
                    event.setCancelled(true);
                }
            }
            else
            {
                if(!RunningMan.Config.getBoolean("PVPInGame")) {
                    event.setCancelled(true);
                }
            }
        }
    }
    @EventHandler
    public void getDamage(EntityDamageEvent event)
    {
        if(RunningMan.Config.getBoolean("RemoveDamage")) {
            event.setDamage(0);
        }
    }
}
