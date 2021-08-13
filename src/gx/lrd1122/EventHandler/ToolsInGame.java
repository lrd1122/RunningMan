package gx.lrd1122.EventHandler;

import gx.lrd1122.GameStates.Game;
import gx.lrd1122.GameStates.PlayerData;
import gx.lrd1122.GameStates.PlayerState;
import gx.lrd1122.RunningMan;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ToolsInGame implements Listener {
    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (PlayerState.GetPlayerState(player.getName()).equalsIgnoreCase("Game")) {
                if (player.getItemInHand().getItemMeta().getDisplayName().contains("风行萝卜")) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1));
                    player.sendMessage(RunningMan.Prefix + "你得到了风行萝卜的疾速祝福");
                    player.getInventory().remove(player.getItemInHand());
                    player.updateInventory();
                }
                if (player.getItemInHand().getItemMeta().getDisplayName().contains("僵尸肉干")) {
                    for (int i = 0; i < Game.getGameByName(player.getName()).getliveplayers().size(); i++) {
                        Player player1 = Bukkit.getPlayer(Game.getGameByName(player.getName()).getliveplayers().get(i));
                        if (player1.getLocation().distance(player.getLocation()) < 4) {
                            player1.sendMessage(RunningMan.Prefix + "你因 僵尸肉干 而减速");
                            player1.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 1));
                        }
                    }
                    player.getInventory().remove(player.getItemInHand());
                    player.sendMessage(RunningMan.Prefix + "你减速了其他人");
                    player.updateInventory();
                }
            }
        }
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
                        player1.sendMessage(RunningMan.Prefix + "你受到了惩罚");
                        player1.teleport(PlayerData.getDataByName(player1.getName()).getLocation());
                        player.getInventory().remove(player.getItemInHand());
                        player.sendMessage(RunningMan.Prefix + "你惩罚了" + player1.getName());
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
}
