package gx.lrd1122.EventHandler;

import gx.lrd1122.GameStates.Game;
import gx.lrd1122.GameStates.PlayerData;
import gx.lrd1122.GameStates.PlayerState;
import gx.lrd1122.ReadConfig.LoadPlayerData;
import gx.lrd1122.ReadConfig.LoadSpecialBlock;
import gx.lrd1122.RegisterEvent.PlayerEndEvent;
import gx.lrd1122.RunTasks.GameTask;
import gx.lrd1122.RunningMan;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.List;

public class BlockCheckEvent implements Listener {
    public String ColorString(String string)
    {
        return string.replace("&", "§");
    }
    @EventHandler
    public void PlayerWalk(PlayerMoveEvent event)
    {
        Player player = event.getPlayer();
        Location location = event.getFrom().getBlock().getLocation();
        location.setY(location.getY()-1);
        Location location1 = event.getFrom().getBlock().getLocation();
        location1.setY(location1.getY()+2);
        Location location2 = event.getFrom().getBlock().getLocation();
        if(location2.getY() < 0)
        {
            player.teleport(PlayerData.getDataByName(player.getName()).getLocation());
        }
        Game game = Game.getGameByName(player.getName());
        if(game != null) {
            try {
                for (int x = 0; x < game.getTouchDieBlock().size(); x++) {
                    String[] getdieblock = game.getTouchDieBlock().get(x).split(",");
                    if(location2.getBlock().getType().equals(Material.LAVA) || location2.getBlock().getType().equals(Material.WATER))
                    {
                        if (Material.getMaterial(getdieblock[0]).equals(location2.getBlock().getType()) && getdieblock[1].equalsIgnoreCase("Ground")) {
                            player.teleport(PlayerData.getDataByName(player.getName()).getLocation());
                            for (PotionEffect potion : player.getActivePotionEffects()) {
                                player.removePotionEffect(potion.getType());
                            }
                            PlayerData.getDataByName(player.getName()).setDeathingame(PlayerData.getDataByName(player.getName()).getDeathingame() + 1);
                        }
                    }
                    else {
                        if (player.isOnGround() && Material.getMaterial(getdieblock[0]).equals(location.getBlock().getType()) && getdieblock[1].equalsIgnoreCase("Ground")) {
                            player.teleport(PlayerData.getDataByName(player.getName()).getLocation());
                            for (PotionEffect potion : player.getActivePotionEffects()) {
                                player.removePotionEffect(potion.getType());
                            }
                            PlayerData.getDataByName(player.getName()).setDeathingame(PlayerData.getDataByName(player.getName()).getDeathingame() + 1);
                            LoadPlayerData.AddPlayerData(player, "PlayerDies", PlayerData.getDataByName(player.getName()).getDies()+1);
                        }
                    }
                    if (Material.getMaterial(getdieblock[0]).equals(location1.getBlock().getType()) && getdieblock[1].equalsIgnoreCase("Head")) {
                        player.teleport(PlayerData.getDataByName(player.getName()).getLocation());
                        for (PotionEffect potion : player.getActivePotionEffects()) {
                            player.removePotionEffect(potion.getType());
                        }
                        PlayerData.getDataByName(player.getName()).setDeathingame(PlayerData.getDataByName(player.getName()).getDeathingame() + 1);
                    }
                }
                if (player.getLocation().getBlock().getLocation().toVector().equals(game.getEndlocation().getBlock().getLocation().toVector()) && location.getWorld().getName().equalsIgnoreCase(game.getEndlocation().getWorld().getName())) {
                    if (PlayerState.GetPlayerState(player.getName()).equalsIgnoreCase("Game")) {
                        PlayerEndEvent endEvent = new PlayerEndEvent(player);
                        Bukkit.getServer().getPluginManager().callEvent(endEvent);
                        if (!endEvent.isCancelled()) {
                            PlayerState.RemovePlayerInGame(player);
                            PlayerState.PlayerInEnd.add(player.getName());
                            game.removeliveplayers(player.getName());
                            player.setAllowFlight(true);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 30000, 1));
                            game.addWinner(player.getName());
                            String number = String.valueOf(game.getPlayers().size() - game.getliveplayers().size());
                            for (int t = 0; t < game.getPlayers().size(); t++) {
                                Player player1 = Bukkit.getPlayer(game.getPlayers().get(t));
                                player1.sendMessage(ColorString(RunningMan.Prefix + "&a玩家 &d" + player.getName() + " &a完成了游戏, 成为了第 &b" + number + " &a名"));
                            }
                            if(Integer.valueOf(number) == 1)
                            {
                                game.setWaitforendcount(120);
                                GameTask.WaitForEndGames.add(game);
                            }
                        }
                    }
                }
                List<String> list = LoadSpecialBlock.SpecialBlockList;
                for (int e = 0; e < list.size(); e++) {
                    String[] current = list.get(e).split(",");
                    if (current[0].equalsIgnoreCase(location.getWorld().getName()) && current[1].replace(".0", "").equalsIgnoreCase(String.valueOf(location.getBlockX())) && current[2].replace(".0", "").equalsIgnoreCase(String.valueOf(location.getBlockY())) && current[3].replace(".0", "").equalsIgnoreCase(String.valueOf(location.getBlockZ()))) {
                        if (current[4].equalsIgnoreCase("command")) {
                            String command = current[5].replace("<player>", player.getName());
                            if (!player.isOp()) {
                                player.setOp(true);
                                player.performCommand(command);
                                player.setOp(false);
                            } else {
                                player.performCommand(command);
                            }
                        }
                        if (current[4].equalsIgnoreCase("sprint")) {
                            Vector direct = player.getLocation().getDirection();
                            direct.multiply(Double.valueOf(current[5]));
                            direct.setY(Double.valueOf(current[6]));
                            player.setVelocity(direct);
                        }
                        if (current[4].equalsIgnoreCase("nojump") && !player.isOnGround()) {
                            player.teleport(event.getFrom());
                        }
                        if (current[4].equalsIgnoreCase("respawn") && !player.getLocation().getBlock().getLocation().toVector().toString().equalsIgnoreCase(PlayerData.getDataByName(player.getName()).getLocation().getBlock().getLocation().toVector().toString()) && player.getLocation().getWorld().equals(PlayerData.getDataByName(player.getName()).getLocation().getWorld())) {
                            player.sendMessage(ColorString(RunningMan.Prefix + "你设置了一个重生点"));
                            PlayerData.getDataByName(player.getName()).setLocation(event.getFrom().getBlock().getLocation());
                        }
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
