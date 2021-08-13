package gx.lrd1122;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.bukkit.WorldEditAPI;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.adapter.BukkitImplLoader;
import com.sk89q.worldedit.world.World;
import gx.lrd1122.EditMap.*;
import gx.lrd1122.GameStates.Game;
import gx.lrd1122.GameStates.PlayerData;
import gx.lrd1122.GameStates.PlayerState;
import gx.lrd1122.ReadConfig.LoadConfig;
import gx.lrd1122.ReadConfig.LoadMap;
import gx.lrd1122.ReadConfig.LoadSpecialBlock;
import gx.lrd1122.ReadConfig.WriteData;
import gx.lrd1122.RunningMan;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import com.sk89q.worldedit.bukkit.adapter.BukkitImplAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static gx.lrd1122.RunningMan.Prefix;
import static org.bukkit.plugin.java.JavaPlugin.getPlugin;

public class EditCommand implements CommandExecutor {
    public String  ColorString(String string)
    {
        return string.replace("&", "§");
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 0 || strings[0].equals("help") && commandSender.hasPermission("rman.help")) {
            commandSender.sendMessage(ColorString("&7=========&d" + Prefix + "&b插件帮助" + "&7========" +
                    "\n&a/rman join <地图名> 加入一张地图 &7rman.join" +
                    "\n&a/rman leave 离开地图 &7rman.leave" +
                    "\n&a/rman setmainlobby 设置主大厅位置 &7rman.setmainlobby" +
                    "\n&a/rman create <地图名> 创建一张地图 &7rman.create" +
                    "\n&a/rman delete <地图名> 删除一张地图 &7rman.delete" +
                    "\n&a/rman maplist 列出所有地图 &7rman.maplist" +
                    "\n&a/rman reload 重载插件 &7rman.reload" +
                    "\n&a/rman setup help 来查询地图制作帮助 &7rman.setup" +
                    "\n&a/rman edit 开启编辑模式 &7rman.edit"));
            return true;
        }
        if (strings.length > 1 && strings[0].equals("create") && commandSender.hasPermission("rman.create")) {
            String mapname = strings[1];
            File file = new File(RunningMan.instance.getDataFolder() + "\\MapData", mapname + ".yml");
            if (file.exists()) {
                commandSender.sendMessage(ColorString(Prefix + RunningMan.MessageConfig.getString("MapExist")));
            }
            if (!file.exists()) {
                LoadMap.MapList.add(file.getName());
                commandSender.sendMessage(ColorString(Prefix + RunningMan.MessageConfig.getString("CreateMap").replace("<Map>", mapname)));
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                WriteData.WriteStringListData(mapname, "TouchDieBlock", "BEDROCK");
            }
            return true;
        }
        if (strings.length > 1 && strings[0].equals("delete") && commandSender.hasPermission("rman.delete")) {
            String mapname = strings[1];
            File file = new File(RunningMan.instance.getDataFolder() + "\\MapData", mapname + ".yml");
            if (!file.exists()) {
                commandSender.sendMessage(ColorString(Prefix + RunningMan.MessageConfig.getString("MapNotFound").replace("<Map>", mapname)));
            }
            if (file.exists()) {
                LoadMap.MapList.remove(file.getName().replace(".yml", ""));
                commandSender.sendMessage(ColorString(Prefix + RunningMan.MessageConfig.getString("DeleteMap").replace("<Map>", mapname)));
                file.delete();
            }
            return true;
        }
        if (strings.length > 1 && strings[0].equals("join") && commandSender.hasPermission("rman.join")) {
            String mapname = strings[1];
            Player player = (Player) commandSender;
            Game game = Game.getGameByGameName(mapname + ".yml");
            if(game == null)
            {
                player.sendMessage(ColorString(Prefix + RunningMan.MessageConfig.getString("MapNotFound").replace("<Map>", mapname)));
                return true;
            }
            else if(!game.getgamestate().equalsIgnoreCase("Wait"))
            {
                player.sendMessage(ColorString(Prefix + RunningMan.MessageConfig.getString("GameHasStart").replace("<Map>", mapname)));
                return true;
            }
            else if(game.getPlayers().size() >= game.getMaxplayers())
            {
                player.sendMessage(RunningMan.MessageConfig.getString("GameHasFull").replace("<Map>", mapname));
                return true;
            }
            else if(game.getgamestate().equalsIgnoreCase("Wait") && game.getPlayers().size() < game.getMaxplayers()){
                List<String> players = game.getPlayers();
                players.add(player.getName());
                game.setPlayers(players);
                for (int c = 0; c < game.getPlayers().size(); c++) {
                    Bukkit.getPlayer(game.getPlayers().get(c)).sendMessage(ColorString(Prefix + RunningMan.MessageConfig.getString("PlayerJoinGame").replace("<player>", player.getName()).replace("<number>", String.valueOf(game.getPlayers().size()))));
                }
                File file = new File(RunningMan.instance.getDataFolder() + "\\MapData", mapname + ".yml");
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                String[] lobbyloc = config.getString("LobbyLocation").split(",");
                Location location = new Location(Bukkit.getWorld(lobbyloc[0]), Double.valueOf(lobbyloc[1]), Double.valueOf(lobbyloc[2]), Double.valueOf(lobbyloc[3]));
                player.teleport(location);
                PlayerState.RemovePlayerInFree(player);
                PlayerState.PlayerInWait.add(player.getName());
            }
            return true;
        }
        if (strings.length >= 1 && strings[0].equals("leave") && commandSender.hasPermission("rman.leave")) {
            Player player = (Player) commandSender;
            Game game = Game.getGameByName(player.getName());
            if(game == null)
            {
                player.sendMessage(ColorString(Prefix + RunningMan.MessageConfig.getString("NotInGame")));
            }
            if(game != null) {
                player.sendMessage(ColorString(Prefix + RunningMan.MessageConfig.getString("ExitGame")));
                String[] stringloc = RunningMan.Config.getString("MainLobby").split(",");
                Location location = new Location(Bukkit.getWorld(stringloc[0]), Double.valueOf(stringloc[1]), Double.valueOf(stringloc[2]), Double.valueOf(stringloc[3]));
                PlayerData.getDataByName(player.getName()).setLocation(location);
                player.teleport(location);
                List<String> players = game.getPlayers();
                for (int i = 0; i < game.getPlayers().size(); i++) {
                    if (game.getPlayers().get(i).equalsIgnoreCase(player.getName())) {
                        game.getPlayers().remove(i);
                    }
                }
                game.getliveplayers().remove(player);
                game.setPlayers(players);
            }
            return true;
        }
        if (strings.length >= 1 && strings[0].equalsIgnoreCase("setmainlobby") && commandSender.hasPermission("rman.setmainlobby")) {
            Player player = (Player)commandSender;
            RunningMan.Config.set("MainLobby", player.getLocation().getWorld().getName() + "," + player.getLocation().toBlockLocation().toVector());
            return true;
        }
        if (strings.length >= 1 && strings[0].equals("maplist") && commandSender.hasPermission("rman.maplist")) {
            for (int i = 0; i < LoadMap.MapList.size(); i++) {
                commandSender.sendMessage(ColorString(Prefix + "&7 -> &b" + LoadMap.MapList.get(i)));
            }
            return true;
        }
        if (strings.length >= 1 && strings[0].equals("reload") && commandSender.hasPermission("rman.reload")) {
            for(int c = 0 ; c <LoadMap.GameList.size(); c++)
            {
                LoadMap.GameList.get(c).StopGame();
            }
            LoadConfig.LoadPluginConfig();
            LoadConfig.LoadPluginMessage();
            LoadMap.LoadMapConfig(true);
            return true;
        }
        if (strings.length >= 1 && strings[0].equals("edit") && commandSender.hasPermission("rman.edit")) {
            if(!PlayerState.PlayerInEdit.contains(commandSender.getName())) {
                commandSender.sendMessage(ColorString(Prefix + "&a你开启了编辑模式"));
                PlayerState.PlayerInEdit.add(commandSender.getName());
                return true;
            }
            if(PlayerState.PlayerInEdit.contains(commandSender.getName())) {
                commandSender.sendMessage(ColorString(Prefix + "&c你关闭了编辑模式"));
                PlayerState.PlayerInEdit.remove(commandSender.getName());
                return true;
            }
        }
        if(strings[0].equals("setup")) {
            Player player = (Player) commandSender;
            if (strings.length == 1 || strings[1].equalsIgnoreCase("help")) {
                player.sendMessage(ColorString("&7=========&d" + Prefix + "&b插件帮助" + "&7========" +
                        "\n&a/rman setup <地图名> setlobby &7设置等待大厅位置在你脚下" +
                        "\n&a/rman setup <地图名> addspawn &7增加一个出生点在你脚下(出生点数量=地图最大人数,出生点可设在同一地点)" +
                        "\n&a/rman setup <地图名> spawnlist &7列出出生点与编号,编号用于删除出生点" +
                        "\n&a/rman setup <地图名> deletespawn <编号> &7删除出生点" +
                        "\n&a/rman setup <地图名> setend &7设置结束位置在你脚下" +
                        "\n&a/rman setup <地图名> addspecialblock <方式> <参数> &7增加一个特殊效果在你看的方块中(可叠加)" +
                        "\n&a/rman setup specialblockhelp &7查询特殊方块的方式与参数" +
                        "\n&a/rman setup <地图名> setdieblock <材质英文> <方式 &7设置碰到触发死亡的方块材质,方式可为 &cHead &7(头顶)/&cGround &7(脚下)" +
                        "\n&a/rman setup <地图名> deletespecialblock &7删除一个特殊效果在你看的方块中" +
                        "\n&c当你使用地图编辑指令后该地图会自动重置状态并踢出所有玩家"));
                return true;
            }
            if(strings.length > 1) {
                if (strings[1].equalsIgnoreCase("specialblockhelp")) {
                    player.sendMessage(ColorString("&7=========&d " + Prefix + "&b插件帮助" + "&7========" +
                            "\n&a/rman setup <地图名> addspecialblock command <指令>" +
                            "\n&7* 踩方块后执行指令,指令中的 <player> 将被替换为玩家名" +
                            "\n&7示例: /rman setup a addspecialblock command kill <player>" +
                            "\n" +
                            "\n&a/rman setup <地图名> addspecialblock sprint <向前> <向上>" +
                            "\n&7* 强制玩家冲刺,负数为向后|向下" +
                            "\n&7示例: /rman setup a addspecialblock sprint 1 5" +
                            "\n" +
                            "\n&a/rman setup <地图名> addspecialblock respawn" +
                            "\n&7* 增加一个复活点,踩上去即可更新复活点" +
                            "\n&7示例/rman setup a addspecialblock respawn" +
                            "\n" +
                            "\n&a/rman setup <地图名> addspecialblock timer <替换方块>" +
                            "\n&7* 增加一个每2秒会将 目标方块 与 替换方块 来回替换的方块" +
                            "\n&7示例/rman setup a addspecialblock timer GRASS" +
                            "\n" +
                            "\n&a/rman setup <地图名> addspecialblock nojump" +
                            "\n&7* 增加一个踩在上面无法跳跃的方块" +
                            "\n&7示例/rman setup a addspecialblock nojump"));
                }
                if (strings.length > 2) {
                    String mapname = strings[1];
                    if (strings[2].equalsIgnoreCase("setlobby")) {
                        player.sendMessage(ColorString(Prefix + "&a你设置了 &7" + mapname + "&a 的等候大厅位置"));
                        SetLobbyLocation.SetLobbyLocation(mapname, player.getLocation());
                    }
                    if (strings[2].equalsIgnoreCase("addspawn")) {
                        player.sendMessage(ColorString(Prefix + "&a你增加了 &7" + mapname + "&a 的起点位置"));
                        SetStartLocation.AddStartLocation(mapname, player.getLocation());
                    }
                    if (strings[2].equalsIgnoreCase("setend")) {
                        player.sendMessage(ColorString(Prefix + "&a你设置了 &7" + mapname + "&a 的终点位置"));
                        Game.getGameByGameName(mapname + ".yml").setEndlocation(player.getLocation());
                        SetEndLocation.SetEndLocation(mapname, player.getLocation());
                    }
                    if (strings[2].equalsIgnoreCase("spawnlist")) {
                        for (int b = 0; b < ListStartLocation.ListStartLocation(mapname).size(); b++) {
                            player.sendMessage(ColorString(Prefix + "&7" + b + ". " + ListStartLocation.ListStartLocation(mapname).get(b)));
                        }
                    }
                    if (strings[2].equalsIgnoreCase("deletespawn")) {
                        player.sendMessage(ColorString(Prefix + "&c你删除了 &7" + mapname + "&c 的 &b1个 &c出生位置"));
                        WriteData.RemoveStringListData(mapname, "StartLocation", Integer.valueOf(strings[3]));
                    }
                    if (strings[2].equalsIgnoreCase("addspecialblock")) {
                        List<Material> blocks = new ArrayList<>();
                        blocks.add(Material.AIR);
                        Set<Material> blockSet = blocks.stream().collect(Collectors.toSet());
                        Block block = player.getTargetBlock(blockSet, 50);
                        if (strings[3].equalsIgnoreCase("command")) {
                            player.sendMessage(ColorString(Prefix + "&a你增加了 &b1个 &a特殊效果 类型: 指令"));
                            String args = strings[4];
                            for(int p = 5; p < strings.length; p++)
                            {
                                args = args+ " " + strings[p];
                            }
                            AddSpecialBlock.AddSpecialBlock(mapname, block.getLocation(), "command," + args);
                        }
                        if (strings[3].equalsIgnoreCase("sprint")) {
                            player.sendMessage(ColorString(Prefix + "&a你增加了 &b1个 &a特殊效果 类型: 冲刺"));
                            AddSpecialBlock.AddSpecialBlock(mapname, block.getLocation(), "sprint," + strings[4] + "," + strings[5]);
                        }
                        if (strings[3].equalsIgnoreCase("respawn")) {
                            player.sendMessage(ColorString(Prefix + "&a你增加了 &b1个 &a特殊效果 类型: 复活点"));
                            AddSpecialBlock.AddSpecialBlock(mapname, block.getLocation(), "respawn");
                        }
                        if (strings[3].equalsIgnoreCase("timer")) {
                            if(Material.getMaterial(strings[4]) != null) {
                                player.sendMessage(ColorString(Prefix + "&a你增加了 &b1个 &a特殊效果 类型: 定时刷新"));
                                AddSpecialBlock.AddSpecialBlock(mapname, block.getLocation(), "timer," + block.getLocation().getBlock().getType() + "," + strings[4]);

                            }
                            else{
                                player.sendMessage(ColorString(Prefix + "&c未知的方块材料"));
                            }
                        }
                        if (strings[3].equalsIgnoreCase("nojump")) {
                            player.sendMessage(ColorString(Prefix + "&a你增加了 &b1个 &a特殊效果 类型: 无法跳跃"));
                            AddSpecialBlock.AddSpecialBlock(mapname, block.getLocation(), "nojump");
                        }
                    }
                    if (strings[2].equalsIgnoreCase("adddieblock")) {
                        Material material = Material.getMaterial(strings[3]);
                        String way = strings[4];
                        if(material == null)
                        {
                            player.sendMessage(ColorString(Prefix + "&c未知的方块"));
                        }
                        if(material != null && strings.equals("Head") && strings.equals("Ground")) {
                            player.sendMessage(ColorString(Prefix + "&a你增加了死亡方块材质为 " + material + " ,&a方式为 &b" + way));
                            Game.getGameByGameName(mapname + ".yml").addTouchDieBlock(material.name() + "," + way);
                            WriteData.WriteStringListData(mapname, "TouchDieBlock", material.name() + "," + way);
                        }
                    }
                    if (strings[2].equalsIgnoreCase("deletespecialblock")) {
                        player.sendMessage(ColorString(Prefix + "&c你移除了特殊效果"));
                        List<Material> blocks = new ArrayList<>();
                        blocks.add(Material.AIR);
                        Set<Material> blockSet = blocks.stream().collect(Collectors.toSet());
                        Block block = player.getTargetBlock(blockSet, 50);
                        WriteData.RemoveStringListData(mapname, "SpecialBlocks", block.getLocation(), player);
                    }
                    if(Game.getGameByGameName(mapname) != null)
                    {
                        Game.getGameByGameName(mapname).StopGame();
                    }
                    LoadMap.LoadMapConfig(false);
                    LoadSpecialBlock.LoadSpecialBlockList();
                    LoadSpecialBlock.LoadEndBlockList();
                    LoadSpecialBlock.LoadTimerBlockList();
                }
                return true;
            }
        }
        return false;
    }
}
