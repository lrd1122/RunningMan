package gx.lrd1122.RunTasks;

import gx.lrd1122.GameStates.Game;
import gx.lrd1122.GameStates.PlayerData;
import gx.lrd1122.GameStates.PlayerState;
import gx.lrd1122.ReadConfig.LoadMap;
import gx.lrd1122.RegisterEvent.GameEndEvent;
import gx.lrd1122.RegisterEvent.GameStartEvent;
import gx.lrd1122.RunningMan;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockState;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GameTask extends BukkitRunnable {
    public static List<Game> WaitForEndGames = new ArrayList<>();
    public String colorstring(String string)
    {
        return string.replace("&", "§");
    }
    @Override
    public void run() {
        if(WaitForEndGames.size() > 0) {
            for (int i = 0; i < WaitForEndGames.size(); i++) {
                Game game = WaitForEndGames.get(i);
                int count = game.getWaitforendcount()-1;
                game.setWaitforendcount(count);
                if(game.getWaitforendcount() == 60)
                {
                    for(int c = 0 ; c < game.getPlayers().size(); c++)
                    {
                        Bukkit.getPlayer(game.getPlayers().get(c)).sendMessage(colorstring(RunningMan.Prefix + "&a游戏将在 &b60&a 秒后结束"));
                    }
                }
                if(game.getWaitforendcount() == 0)
                {
                    WaitForEndGames.remove(game);
                    game.StopGame(game.getWinners());
                }
            }
        }
        for(int c = 0; c < PlayerState.PlayerInEnd.size(); c++)
        {
            Player player = Bukkit.getPlayer(PlayerState.PlayerInEnd.get(c));
            if(player.getInventory().getItem(8) == null) {
                ItemStack itemStack = new ItemStack(Material.REDSTONE);
                ItemMeta meta = itemStack.getItemMeta();
                meta.setDisplayName("§c离开游戏");
                itemStack.setItemMeta(meta);
                player.getInventory().setItem(8, itemStack);
                player.updateInventory();
            }
        }
        for(int c = 0; c < PlayerState.PlayerInGame.size(); c++)
        {
            Player player = Bukkit.getPlayer(PlayerState.PlayerInGame.get(c));
            if(player.getInventory().getItem(2) == null) {
                ItemStack itemStack = new ItemStack(Material.NAME_TAG);
                ItemMeta meta = itemStack.getItemMeta();
                meta.setDisplayName("§c显示死亡数");
                itemStack.setItemMeta(meta);
                player.getInventory().setItem(2, itemStack);
                player.updateInventory();
            }
            if(player.getInventory().getItem(3) == null) {
                ItemStack itemStack = new ItemStack(Material.BED);
                ItemMeta meta = itemStack.getItemMeta();
                meta.setDisplayName("§a返回复活点");
                itemStack.setItemMeta(meta);
                player.getInventory().setItem(3, itemStack);
                player.updateInventory();
            }
        }
        for(int c = 0; c < PlayerState.PlayerInWait.size(); c++)
        {
            Player player = Bukkit.getPlayer(PlayerState.PlayerInWait.get(c));
            if(player.getInventory().getItem(8) == null) {
                ItemStack itemStack = new ItemStack(Material.REDSTONE);
                ItemMeta meta = itemStack.getItemMeta();
                meta.setDisplayName("§c离开游戏");
                itemStack.setItemMeta(meta);
                player.getInventory().setItem(8, itemStack);
                player.updateInventory();
            }
        }
        for(int i = 0; i< LoadMap.GameList.size(); i++)
        {
            Game game = LoadMap.GameList.get(i);
            if(Game.getGameByGameName(game.getName()).getgamestate().equalsIgnoreCase("End"))
            {
                GameEndEvent endEvent = new GameEndEvent(game);
                Bukkit.getServer().getPluginManager().callEvent(endEvent);
                if(!endEvent.isCancelled()) {
                    Game.getGameByGameName(game.getName()).StopGame();
                }
            }
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
                            try
                            {
                                player.playSound(player.getLocation(), Sound.valueOf("NOTE_BASS"), 100, 3);
                            }
                            catch (Exception e)
                            {
                                player.playSound(player.getLocation(), Sound.valueOf("BLOCK_NOTE_BASS"), 100, 3);
                            }
                            player.setLevel(game.getCountdownnow());
                        }
                    }
                    if(game.getCountdownnow() == 0 && game.getPlayers().size() > 1)
                    {
                        GameStartEvent gameStartEvent = new GameStartEvent(game);
                        Bukkit.getServer().getPluginManager().callEvent(gameStartEvent);
                        if(!gameStartEvent.isCancelled()) {
                            game.setgamestate("InGame");
                            for(int w = 0 ; w <game.getPlayers().size(); w++) {
                                game.addliveplayers(game.getPlayers().get(w));
                            }
                            for (int a = 0; a < game.getPlayers().size(); a++) {
                                Player player = Bukkit.getPlayer(game.getPlayers().get(a));
                                if(game.getCommandinstart().size() > 0) {
                                    for (int f = 0; f < game.getCommandinstart().size(); f++) {
                                        if(!player.isOp())
                                        {
                                            player.setOp(true);
                                            player.performCommand(game.getCommandinstart().get(f).replace("<player>", player.getName()));
                                            player.setOp(false);
                                        }
                                        else{
                                            player.performCommand(game.getCommandinstart().get(f));
                                        }
                                    }
                                }
                                player.getInventory().clear();
                                player.updateInventory();
                                PlayerState.ClearState(player.getName());
                                PlayerState.PlayerInGame.add(player.getName());
                                File file = new File(RunningMan.instance.getDataFolder() + "\\MapData", game.getName());
                                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                                String[] teleportloc = config.getStringList("StartLocation").get(i).split(",");
                                Location location = new Location(Bukkit.getWorld(teleportloc[0]), Double.valueOf(teleportloc[1]), Double.valueOf(teleportloc[2]), Double.valueOf(teleportloc[3]));
                                player.teleport(location);
                                PlayerData.getDataByName(player.getName()).setLocation(location);
                                try
                                {
                                    player.playSound(player.getLocation(), Sound.valueOf("LEVEL_UP"), 100, 3);
                                }
                                catch (Exception e){
                                    player.playSound(player.getLocation(), Sound.valueOf("ENTITY_PLAYER_LEVELUP"), 100, 3);
                                }
                            }
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
                    for(int r = 0 ; r < game.getTimerblocklist().size(); r++)
                    {
                        String[] loc = game.getTimerblocklist().get(r).split(",");
                        Location location = new Location(Bukkit.getWorld(loc[0]), Double.valueOf(loc[1]), Double.valueOf(loc[2]), Double.valueOf(loc[3]));
                        ReplaceBlock(location, Material.getMaterial(loc[6]), Material.getMaterial(loc[5]));
                    }
                    game.setBlocktime(0);
                }
                if(game.getGametime() % 180 == 0)
                {
                    game.giveRandomToolstoAll();
                    for(int e = 0; e < game.getliveplayers().size(); e++)
                    {
                        Bukkit.getPlayer(game.getliveplayers().get(e)).sendMessage(colorstring(RunningMan.Prefix + "你获得了一个道具"));
                    }
                }
                for(int e = 0; e < game.getliveplayers().size(); e++) {
                    if (PlayerData.getDataByName(game.getliveplayers().get(e)).isShowdeathingame()) {
                        String s = colorstring(RunningMan.Prefix + "&a你当前的死亡数为 &b" + PlayerData.getDataByName(game.getliveplayers().get(e)).getDeathingame() + " &a次");
                        BaseComponent[] component = TextComponent.fromLegacyText(s);
                        Bukkit.getPlayer(game.getliveplayers().get(e)).spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
                    }
                }
            }
            if(game.getgamestate().equalsIgnoreCase("InGame") && game.getPlayers().size() <= 1 && game.getliveplayers().size() <= 1)
            {
                game.setgamestate("End");
            }
        }


    }
    public void ReplaceBlock(Location location, Material block1, Material block2)
    {
        BlockState state = location.getBlock().getState();
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
