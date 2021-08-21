package gx.lrd1122.PlayerControl;

import gx.lrd1122.GameStates.Game;
import gx.lrd1122.GameStates.PlayerState;
import gx.lrd1122.RunningMan;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.List;

import static gx.lrd1122.RunningMan.Prefix;

public class PlayerJoinGame {
    private static String ColorString(String s)
    {
        return s.replace("&", "§");
    }
    public static void JoinGame(Player player, Game game)
    {
        if(game == null)
        {
            player.sendMessage(ColorString(Prefix + RunningMan.MessageConfig.getString("MapNotFound").replace("<Map>", game.getName().replace(".yml", ""))));
        }
        else if(!game.getgamestate().equalsIgnoreCase("Wait"))
        {
            player.sendMessage(ColorString(Prefix + RunningMan.MessageConfig.getString("GameHasStart").replace("<Map>",game.getName().replace(".yml", ""))));
        }
        else if(game.getPlayers().size() >= game.getMaxplayers())
        {
            player.sendMessage(ColorString(RunningMan.MessageConfig.getString("GameHasFull").replace("<Map>", game.getName().replace(".yml", ""))));
        }
        else if(!PlayerState.PlayerInFree.contains(player.getName()))
        {
            player.sendMessage(ColorString(Prefix + RunningMan.MessageConfig.getString("PlayerHasJoin")));
        }
        else {
            List<String> players = game.getPlayers();
            players.add(player.getName());
            game.setPlayers(players);
            for (int c = 0; c < game.getPlayers().size(); c++) {
                Bukkit.getPlayer(game.getPlayers().get(c)).sendMessage(ColorString(Prefix + RunningMan.MessageConfig.getString("PlayerJoinGame").replace("<player>", player.getName()).replace("<number>", String.valueOf(game.getPlayers().size()))));
            }
            File file = new File(RunningMan.instance.getDataFolder() + "\\MapData", game.getName());
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            String[] lobbyloc = config.getString("LobbyLocation").split(",");
            Location location = new Location(Bukkit.getWorld(lobbyloc[0]), Double.valueOf(lobbyloc[1]), Double.valueOf(lobbyloc[2]), Double.valueOf(lobbyloc[3]));
            player.teleport(location);
            PlayerState.RemovePlayerInFree(player);
            PlayerState.PlayerInWait.add(player.getName());
            player.getInventory().clear();
            player.updateInventory();
            if(player.hasPermission("rman.forcestart"))
            {
                ItemStack itemStack = new ItemStack(Material.DIAMOND);
                ItemMeta meta = itemStack.getItemMeta();
                meta.setDisplayName("§b减少倒计时");
                itemStack.setItemMeta(meta);
                player.getInventory().setItem(4, itemStack);
            }
        }
    }
}
