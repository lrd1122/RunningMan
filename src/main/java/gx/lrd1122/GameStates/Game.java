package gx.lrd1122.GameStates;

import gx.lrd1122.ReadConfig.LoadMap;
import gx.lrd1122.ReadConfig.LoadPlayerData;
import gx.lrd1122.ReadConfig.LoadTools;
import gx.lrd1122.RunningMan;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    private String name;
    private int countdownMax;
    private int countdownnow;
    private String gamestate;
    private List<String> players;
    private List<String> TouchDieBlock;
    private List<String> liveplayers;
    private Location endlocation;
    private int gametime;
    private int blocktime;
    private List<String> timerblocklist;
    private int maxplayers;
    private List<String> commandinstart;
    private List<String> commandinend;
    private List<String> winners;
    private String displayname;
    private int waitforendcount;

    public Game() {

    }

    public Game(String name, String displayname, int countdownMax,
                int countdownnow, String gamestate, List<String> players,
                List<String> touchDieBlock, List<String> liveplayers, Location endlocation,
                int gametime, int blocktime, List<String> timerblocklist, int maxplayers,
                List<String> commandinstart, List<String> commandinend, List<String> winners,
                int waitforendcount) {
        this.name = name;
        this.countdownMax = countdownMax;
        this.countdownnow = countdownnow;
        this.gamestate = gamestate;
        this.players = players;
        this.TouchDieBlock = touchDieBlock;
        this.liveplayers = liveplayers;
        this.endlocation = endlocation;
        this.gametime = gametime;
        this.blocktime = blocktime;
        this.timerblocklist = timerblocklist;
        this.maxplayers = maxplayers;
        this.commandinstart = commandinstart;
        this.commandinend = commandinend;
        this.winners = winners;
        this.displayname = displayname;
        this.waitforendcount = waitforendcount;
    }

    public int getWaitforendcount() {
        return waitforendcount;
    }

    public void setWaitforendcount(int waitforendcount) {
        this.waitforendcount = waitforendcount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCountdownMax() {
        return this.countdownMax;
    }

    public void setCountdownMax(int countdownMax) {
        this.countdownMax = countdownMax;
    }

    public int getCountdownnow() {
        return this.countdownnow;
    }

    public void setCountdownnow(int countdownnow) {
        this.countdownnow = countdownnow;
    }

    public String getgamestate() {
        return gamestate;
    }

    public void setgamestate(String gamestate) {
        this.gamestate = gamestate;
    }

    public List<String> getPlayers() {
        return this.players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }

    public List<String> getTouchDieBlock() {
        return this.TouchDieBlock;
    }

    public void setTouchDieBlock(List<String> touchDieBlock) {
        this.TouchDieBlock = touchDieBlock;
    }

    public void addTouchDieBlock(String material) {
        this.TouchDieBlock.add(material);
    }

    public static Game getGameByName(String name) {
        for (int i = 0; i < LoadMap.GameList.size(); i++) {
            for (int b = 0; b < LoadMap.GameList.get(i).getPlayers().size(); b++) {
                if (LoadMap.GameList.get(i).getPlayers().get(b).equalsIgnoreCase(name)) {
                    return LoadMap.GameList.get(i);
                }
            }
        }
        return null;
    }

    public static Game getGameByGameName(String name) {
        for (int i = 0; i < LoadMap.GameList.size(); i++) {
            if (LoadMap.GameList.get(i).getName().equals(name)) {
                return LoadMap.GameList.get(i);
            }
        }
        return null;
    }

    public List<String> getliveplayers() {
        return this.liveplayers;
    }

    public void setliveplayers(List<String> liveplayers) {
        this.liveplayers = liveplayers;
    }

    public void addliveplayers(String name) {
        this.liveplayers.add(name);
    }

    public void removeliveplayers(String name) {
        for (int i = 0; i < this.liveplayers.size(); i++) {
            if (this.liveplayers.get(i).equalsIgnoreCase(name)) {
                this.liveplayers.remove(i);
            }
        }
    }

    public void setMaxplayers(int maxplayers) {
        this.maxplayers = maxplayers;
    }

    public int getMaxplayers() {
        return this.maxplayers;
    }

    public void setEndlocation(Location location) {
        this.endlocation = location;
    }

    public Location getEndlocation() {
        return this.endlocation;
    }

    public void StopGame() {
        String[] stringloc = RunningMan.Config.getString("MainLobby").split(",");
        Location location = new Location(Bukkit.getWorld(stringloc[0]), Double.valueOf(stringloc[1]), Double.valueOf(stringloc[2]), Double.valueOf(stringloc[3]));
        for (int i = 0; i < this.getPlayers().size(); i++) {
            Player player = Bukkit.getPlayer(this.getPlayers().get(i));
            player.setGameMode(GameMode.SURVIVAL);
            player.teleport(location);
            PlayerState.RemovePlayerInGame(player);
            PlayerState.PlayerInFree.add(player.getName());
            player.getInventory().clear();
            player.updateInventory();
            ItemStack item = new ItemStack(Material.CHEST);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§a加入游戏");
            item.setItemMeta(meta);
            player.getInventory().setItem(0, item);
            player.updateInventory();
        }
        this.setliveplayers(new ArrayList<>());
        this.setPlayers(new ArrayList<>());
        this.setCountdownMax(RunningMan.Config.getInt("CountDown"));
        this.setCountdownnow(RunningMan.Config.getInt("CountDown"));
        this.setgamestate("Wait");
        this.setGametime(0);
        this.setWinners(new ArrayList<>());
    }
    public void StopGame(List<String> winners) {
        String[] stringloc = RunningMan.Config.getString("MainLobby").split(",");
        Location location = new Location(Bukkit.getWorld(stringloc[0]), Double.valueOf(stringloc[1]), Double.valueOf(stringloc[2]), Double.valueOf(stringloc[3]));
        for (int i = 0; i < this.getPlayers().size(); i++) {
            Player player = Bukkit.getPlayer(this.getPlayers().get(i));
            player.setGameMode(GameMode.SURVIVAL);
            player.teleport(location);
            for(int q = 0 ; q <winners.size(); q++)
            {
                String count = String.valueOf(q+1);
                player.sendMessage((RunningMan.Prefix + "&a玩家 &b" + winners.get(q) + " &a获得了第 &c" + count + "&a 名").replace("&", "§"));
                LoadPlayerData.AddPlayerData(Bukkit.getPlayer(winners.get(q)), "PlayerWins", PlayerData.getDataByName(winners.get(q)).getWins() + 1);
            }
            PlayerState.RemovePlayerInGame(player);
            PlayerState.PlayerInFree.add(player.getName());
            player.getInventory().clear();
            player.updateInventory();
            ItemStack item = new ItemStack(Material.CHEST);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§a加入游戏");
            item.setItemMeta(meta);
            player.getInventory().setItem(0, item);
            player.updateInventory();
            if(getCommandinend() != null && getCommandinend().size() > 0) {
                for (int f = 0; f < getCommandinend().size(); f++) {
                    if(!player.isOp())
                    {
                        player.setOp(true);
                        player.performCommand(getCommandinend().get(f).replace("<player>", player.getName()));
                        player.setOp(false);
                    }
                    else{
                        player.performCommand(getCommandinend().get(f));
                    }
                }
            }
        }
        this.setliveplayers(new ArrayList<>());
        this.setPlayers(new ArrayList<>());
        this.setCountdownMax(RunningMan.Config.getInt("CountDown"));
        this.setCountdownnow(RunningMan.Config.getInt("CountDown"));
        this.setgamestate("Wait");
        this.setGametime(0);
        this.setWinners(new ArrayList<>());
    }

    public int getGametime() {
        return this.gametime;
    }

    public void setGametime(int time) {
        this.gametime = time;
    }

    public int getBlocktime() {
        return this.blocktime;
    }

    public void setBlocktime(int time) {
        this.blocktime = time;
    }

    public void addtimerblock(String timerblock) {
        this.timerblocklist.add(timerblock);
    }

    public void removetimerblock(String timerblock) {
        this.timerblocklist.remove(timerblock);
    }

    public void setTimerblocklist(List<String> list) {
        this.timerblocklist = list;
    }

    public List<String> getTimerblocklist() {
        return this.timerblocklist;
    }

    public List<String> getCommandinstart() {
        return this.commandinstart;
    }

    public void setCommandinstart(List<String> commandinstart) {
        this.commandinstart = commandinstart;
    }

    public void addCommandinstart(String command) {
        this.commandinstart.add(command);
    }

    public void removeCommandinstart(String command) {
        this.commandinstart.remove(command);
    }

    public List<String> getCommandinend() {
        return commandinend;
    }

    public void setCommandinend(List<String> commandinend) {
        this.commandinend = commandinend;
    }
    public void addCommandinend(String string)
    {
        this.commandinend.add(string);
    }
    public void removeCommandinend(int i)
    {
        this.commandinend.remove(i);
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public void giveRandomToolstoAll() {
        for (int e = 0; e < this.getliveplayers().size(); e++) {
            int ran = getRandom(0, LoadTools.ToolList.size());
            if (LoadTools.ToolList.get(ran).equalsIgnoreCase("PunishStick")) {
                ItemStack itemStack = new ItemStack(Material.STICK);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName("§a惩罚棒");
                itemStack.setItemMeta(itemMeta);
                Bukkit.getPlayer(players.get(e)).getInventory().addItem(itemStack);
            }
            if (LoadTools.ToolList.get(ran).equalsIgnoreCase("PunishBow")) {
                ItemStack itemStack = new ItemStack(Material.BOW);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName("§a惩罚弓");
                itemStack.setItemMeta(itemMeta);
                Bukkit.getPlayer(players.get(e)).getInventory().addItem(itemStack);
                Bukkit.getPlayer(players.get(e)).updateInventory();
                Bukkit.getPlayer(players.get(e)).getInventory().addItem(new ItemStack(Material.ARROW));
            }
            if (LoadTools.ToolList.get(ran).equalsIgnoreCase("SpeedCarrot")) {
                ItemStack itemStack = new ItemStack(Material.CARROT);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName("§a风行萝卜");
                itemStack.setItemMeta(itemMeta);
                Bukkit.getPlayer(players.get(e)).getInventory().addItem(itemStack);
            }
            if (LoadTools.ToolList.get(ran).equalsIgnoreCase("RottenFlesh")) {
                ItemStack itemStack = new ItemStack(Material.ROTTEN_FLESH);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName("§a僵尸肉干");
                itemStack.setItemMeta(itemMeta);
                Bukkit.getPlayer(players.get(e)).getInventory().addItem(itemStack);
            }
            Bukkit.getPlayer(players.get(e)).updateInventory();
        }
    }

    public List<String> getWinners() {
        return winners;
    }

    public void setWinners(List<String> winners) {
        this.winners = winners;
    }
    public void addWinner(String name)
    {
        this.winners.add(name);
    }
    public void removeWinner(String name)
    {
        this.winners.remove(name);
    }

    private int getRandom(int min, int max) {
        return new Random().nextInt(max - min) + min;
    }

}
