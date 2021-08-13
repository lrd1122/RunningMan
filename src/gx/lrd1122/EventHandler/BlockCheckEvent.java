package gx.lrd1122.EventHandler;

import gx.lrd1122.GameStates.Game;
import gx.lrd1122.GameStates.PlayerData;
import gx.lrd1122.GameStates.PlayerState;
import gx.lrd1122.ReadConfig.LoadPlayerData;
import gx.lrd1122.ReadConfig.LoadSpecialBlock;
import gx.lrd1122.RunningMan;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
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
        player.setSprinting(true);
        Location location = event.getFrom().toBlockLocation();
        location.setY(location.getY()-1);
        Location location1 = event.getFrom().toBlockLocation();
        location1.setY(location1.getY()+2);
        Game game = Game.getGameByName(player.getName());
        if(game != null && player.isOnGround()) {
            for(int x = 0; x <game.getTouchDieBlock().size(); x++)
            {
                String[] getdieblock = game.getTouchDieBlock().get(x).split(",");
                if (Material.getMaterial(getdieblock[0]).equals(location.getBlock().getType()) && getdieblock[1].equalsIgnoreCase("Ground")) {
                player.teleport(PlayerData.getDataByName(player.getName()).getLocation());
            }
                if (Material.getMaterial(getdieblock[0]).equals(location1.getBlock().getType()) && getdieblock[1].equalsIgnoreCase("Head")) {
                    player.teleport(PlayerData.getDataByName(player.getName()).getLocation());
                }
            }
            if(location.toVector().equals(game.getEndlocation().toVector()) && location.getWorld().getName().equalsIgnoreCase(game.getEndlocation().getWorld().getName()))
            {
                    PlayerState.RemovePlayerInGame(player);
                    PlayerState.PlayerInEnd.add(player.getName());
                    player.setGameMode(GameMode.SPECTATOR);
                    game.getliveplayers().remove(player.getName());
                for(int t = 0; t < game.getPlayers().size(); t++)
                {
                    Player player1 = Bukkit.getPlayer(game.getPlayers().get(t));
                    String number = String.valueOf(game.getPlayers().size() - game.getliveplayers().size());
                    player1.sendMessage(ColorString(RunningMan.Prefix + "&a玩家 &d" + player.getName() + " &a完成了游戏, 成为了第 &b" + number + " &a名"));
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
                    if (current[4].equalsIgnoreCase("respawn") && !player.getLocation().toBlockLocation().toVector().toString().equalsIgnoreCase(PlayerData.getDataByName(player.getName()).getLocation().toBlockLocation().toVector().toString()) && player.getLocation().getWorld().equals(PlayerData.getDataByName(player.getName()).getLocation().getWorld())) {
                        player.sendMessage(ColorString(RunningMan.Prefix + "你设置了一个重生点"));
                        PlayerData.getDataByName(player.getName()).setLocation(event.getFrom().toBlockLocation());
                    }
                }
            }
        }
    }
}
