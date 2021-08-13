package gx.lrd1122.RunTasks;

import gx.lrd1122.GameStates.Game;
import gx.lrd1122.GameStates.PlayerData;
import gx.lrd1122.GameStates.PlayerState;
import gx.lrd1122.ReadConfig.LoadMap;
import gx.lrd1122.ReadConfig.LoadSpecialBlock;
import gx.lrd1122.RunningMan;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;

public class GameTask extends BukkitRunnable {
    public String colorstring(String string)
    {
        return string.replace("&", "§");
    }
    @Override
    public void run() {
        for(int c = 0; c <PlayerState.PlayerInEnd.size(); c++)
        {
            Player player = Bukkit.getPlayer(PlayerState.PlayerInEnd.get(c));
            if(player.getInventory().getItem(8).getType().equals(Material.AIR)) {
                ItemStack itemStack = new ItemStack(Material.REDSTONE);
                ItemMeta meta = itemStack.getItemMeta();
                meta.setDisplayName("§c离开游戏");
                itemStack.setItemMeta(meta);
                player.getInventory().setItem(8, itemStack);
            }
        }
        for(int i = 0; i< LoadMap.GameList.size(); i++)
        {
            Game game = LoadMap.GameList.get(i);
            if(game.getgamestate().equalsIgnoreCase("Wait")) {
                if (game.getPlayers().size() < 2) {
                    game.setCountdownnow(game.getCountdownMax());
                    for (int a = 0; a < game.getPlayers().size(); a++) {
                        Player player = Bukkit.getPlayer(game.getPlayers().get(a));
                        player.setLevel(game.getCountdownnow());
                    }
                }
                if (game.getPlayers().size() >= 2) {
                    if(game.getCountdownnow() >= 1) {
                        game.setCountdownnow(game.getCountdownnow() - 1);
                        for (int a = 0; a < game.getPlayers().size(); a++) {
                            Player player = Bukkit.getPlayer(game.getPlayers().get(a));
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 100, 3);
                            player.setLevel(game.getCountdownnow());
                        }
                    }
                    if(game.getCountdownnow() == 0)
                    {
                        game.setgamestate("InGame");
                        for (int a = 0; a < game.getPlayers().size(); a++) {
                            Player player = Bukkit.getPlayer(game.getPlayers().get(a));
                            PlayerState.ClearState(player.getName());
                            PlayerState.PlayerInGame.add(player.getName());
                            File file = new File(RunningMan.instance.getDataFolder() + "\\MapData", game.getName());
                            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                            String[] teleportloc = config.getStringList("StartLocation").get(i).split(",");
                            Location location = new Location(Bukkit.getWorld(teleportloc[0]), Double.valueOf(teleportloc[1]),Double.valueOf(teleportloc[2]),Double.valueOf(teleportloc[3]));
                            player.teleport(location);
                            game.setliveplayers(game.getPlayers());
                            player.sendActionBar("开始您的跑酷之路");
                            PlayerData.getDataByName(player.getName()).setLocation(location);
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 100, 3);
                        }
                    }
                }
            }
            if(game.getgamestate().equalsIgnoreCase("InGame"))
            {
                game.setGametime(game.getGametime()+1);
                game.setBlocktime(game.getBlocktime()+1);
                if(game.getBlocktime() >= 2)
                {
                    for(int r = 0 ; r < LoadSpecialBlock.TimerBlockList.size(); r++)
                    {
                        String[] loc = LoadSpecialBlock.TimerBlockList.get(r).split(",");
                        Location location = new Location(Bukkit.getWorld(loc[0]), Double.valueOf(loc[1]), Double.valueOf(loc[2]), Double.valueOf(loc[3]));
                        ReplaceBlock(location, Material.getMaterial(loc[6]), Material.getMaterial(loc[5]));
                    }
                    game.setBlocktime(0);
                }
                if(game.getGametime() % 60 == 0)
                {
                    game.giveRandomToolstoAll();
                    for(int e = 0; e < game.getliveplayers().size(); e++)
                    {
                        Bukkit.getPlayer(game.getliveplayers().get(e)).sendMessage(colorstring(RunningMan.Prefix + "你获得了一个道具"));
                    }
                }
            }
            if(game.getgamestate().equalsIgnoreCase("InGame") && game.getliveplayers().size() <= 1)
            {
                for(int e = 0; e < game.getliveplayers().size(); e++)
                {
                    Bukkit.getPlayer(game.getliveplayers().get(e)).sendMessage(RunningMan.Prefix + "由于玩家人数不足,游戏已自动取消");
                }
                game.setgamestate("End");
            }
            if(game.getgamestate().equalsIgnoreCase("End"))
            {
                game.StopGame();
            }
        }
    }
    public void ReplaceBlock(Location location, Material block1, Material block2)
    {
        BlockState state = location.getBlock().getState(true);
        if(location.getBlock().getType().equals(block1))
        {
            state.setType(block2);
            state.update(true);
            return;
        }
        if(location.getBlock().getType().equals(block2))
        {
            state.setType(block1);
            state.update(true);
            return;
        }
    }
}
