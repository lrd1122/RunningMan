package gx.lrd1122.Lobby;

import gx.lrd1122.GameStates.Game;
import gx.lrd1122.PlayerControl.PlayerJoinGame;
import gx.lrd1122.ReadConfig.LoadMap;
import gx.lrd1122.RunningMan;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;

public class LobbyMenu implements Listener {
    public Inventory inventory;
    public Player player;
    public LobbyMenu(Player player)
    {
        this.player = player;
        inventory = Bukkit.createInventory(player, 9, RunningMan.Prefix);
        for(int i =0 ; i < LoadMap.GameList.size(); i++)
        {
            Game game = LoadMap.GameList.get(i);
            ItemStack item = new ItemStack(Material.WOOL);
            item.setData(game.getgamestate().equalsIgnoreCase("Wait") ? new MaterialData(5) : new MaterialData((11)));
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(game.getDisplayname());
            List<String> list  = new ArrayList<>();
            list.add("§b游戏内人数: §c" + game.getPlayers().size());
            meta.setLore(list);
            item.setItemMeta(meta);
            inventory.setItem(i, item);
        }
    }
    public void openMenu()
    {
        player.openInventory(inventory);
    }

}
