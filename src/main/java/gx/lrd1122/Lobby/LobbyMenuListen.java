package gx.lrd1122.Lobby;

import gx.lrd1122.PlayerControl.PlayerJoinGame;
import gx.lrd1122.ReadConfig.LoadMap;
import gx.lrd1122.RunningMan;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class LobbyMenuListen implements Listener {
    @EventHandler
    public void PlayerClick(InventoryClickEvent event)
    {
        Player player = (Player) event.getWhoClicked();
        if(event.getWhoClicked().getOpenInventory().getTitle().equalsIgnoreCase(RunningMan.Prefix))
        {
            event.setCancelled(true);
            if(event.getCurrentItem() != null)
            {
                player.performCommand("rman join " + LoadMap.GameList.get(event.getSlot()).getName().replace(".yml", ""));
            }
        }
    }
}
