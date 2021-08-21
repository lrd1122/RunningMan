package gx.lrd1122.EventHandler;

import gx.lrd1122.GameStates.Game;
import gx.lrd1122.GameStates.PlayerData;
import gx.lrd1122.GameStates.PlayerState;
import gx.lrd1122.Lobby.LobbyMenu;
import gx.lrd1122.PlayerControl.ShowDeathInGame;
import gx.lrd1122.ReadConfig.GetConfig;
import gx.lrd1122.RunningMan;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class ToolsInGame implements Listener {
    public String colorstring(String s)
    {
        return s.replace("&", "§");
    }
    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        try {
            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                if (player.getItemInHand().getItemMeta().getDisplayName().contains("离开游戏")) {
                    player.getInventory().remove(player.getItemInHand());
                    if(GetConfig.getConfigBoolean(RunningMan.Config, "Bungeecord"))
                    {
                        connectToServer(player, GetConfig.getConfigString(RunningMan.Config, "FallBackServer"));
                    }
                    else
                    {
                        player.performCommand("rman leave");
                    }

                }
                if (player.getItemInHand().getItemMeta().getDisplayName().contains("减少倒计时")) {
                    player.getInventory().remove(player.getItemInHand());
                    Game.getGameByName(player.getName()).setCountdownnow(10);
                }
                if (player.getItemInHand().getItemMeta().getDisplayName().contains("加入游戏")) {
                    new LobbyMenu(player).openMenu();
                }
                if (player.getItemInHand().getItemMeta().getDisplayName().contains("显示死亡数")) {
                    ShowDeathInGame.ShowDeathInGame(player);
                }
                if (player.getItemInHand().getItemMeta().getDisplayName().contains("返回复活点")) {
                    player.teleport(PlayerData.getDataByName(player.getName()).getLocation());
                }
                if (PlayerState.GetPlayerState(player.getName()).equalsIgnoreCase("Game")) {
                    if (player.getItemInHand().getItemMeta().getDisplayName().contains("风行萝卜")) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1));
                        player.sendMessage(colorstring(RunningMan.Prefix + GetConfig.getConfigString(RunningMan.MessageConfig, "GameTools", "UseSpeedCarrot")));
                        player.getItemInHand().setAmount(player.getItemInHand().getAmount()-1);
                        player.updateInventory();
                    }
                    if (player.getItemInHand().getItemMeta().getDisplayName().contains("僵尸肉干")) {
                        for (int i = 0; i < Game.getGameByName(player.getName()).getliveplayers().size(); i++) {
                            Player player1 = Bukkit.getPlayer(Game.getGameByName(player.getName()).getliveplayers().get(i));
                            if (player1.getLocation().distance(player.getLocation()) < 4 && !player1.getName().equalsIgnoreCase(player.getName())) {
                                player1.sendMessage(colorstring(RunningMan.Prefix + GetConfig.getConfigString(RunningMan.MessageConfig, "GameTools", "GetRottenFlesh").replace("<player>", player1.getName())));
                                player1.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 1));
                            }
                        }
                        player.getItemInHand().setAmount(player.getItemInHand().getAmount()-1);
                        player.sendMessage(colorstring(RunningMan.Prefix + GetConfig.getConfigString(RunningMan.MessageConfig, "GameTools", "UseRottenFlesh")));
                        player.updateInventory();
                    }
                }
            }
        }
        catch (Exception e) {}
    }
    @EventHandler
    public void PlayerDamageEvent(EntityDamageByEntityEvent event)
    {
        if(event.getEntity() instanceof Player) {
            Player player1 = (Player) event.getEntity();
            if(event.getDamager() instanceof Player) {
                Player player = (Player) event.getDamager();
                if (PlayerState.GetPlayerState(player.getName()).equalsIgnoreCase("Game")) {
                    if (player.getItemInHand().getItemMeta().getDisplayName().contains("惩罚棒")) {
                        player1.sendMessage(colorstring(RunningMan.Prefix + GetConfig.getConfigString(RunningMan.MessageConfig, "GameTools","GetPunishStick")));
                        player1.teleport(PlayerData.getDataByName(player1.getName()).getLocation());
                        player.getItemInHand().setAmount(player.getItemInHand().getAmount()-1);
                        player.sendMessage(colorstring(RunningMan.Prefix + GetConfig.getConfigString(RunningMan.MessageConfig, "GameTools","UsePunishStick").replace("<player>", player1.getName())));
                        player.updateInventory();
                    }
                }
            }
            if(event.getDamager().getType().equals(EntityType.ARROW))
            {
                player1.teleport(PlayerData.getDataByName(player1.getName()).getLocation());
            }
        }
    }
    @EventHandler
    public void PlayerShootEvent(EntityShootBowEvent event)
    {
        event.getBow().setAmount(0);
        Player player = (Player) event.getEntity();
        player.getInventory().remove(event.getBow());
    }
    private void connectToServer(Player player, String server) {
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            try {
                out.writeUTF("Connect");
                out.writeUTF(server);
            } catch (Exception e) {
                e.printStackTrace();
            }
            player.sendPluginMessage(RunningMan.getPlugin(RunningMan.class), "BungeeCord", b.toByteArray());
        } catch (org.bukkit.plugin.messaging.ChannelNotRegisteredException e) {
            Bukkit.getLogger().warning("§c无法连接至服务器");
        }
    }

}
