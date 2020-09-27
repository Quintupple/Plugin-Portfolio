package me.quintupple.obscurusclasses.Commands;

import me.quintupple.obscurusclasses.ObscurusClasses;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public class ClassCreate implements CommandExecutor, Listener {

    public static FileConfiguration cfg;

    public static ArrayList<UUID> classPlayers = new ArrayList<>();
    public static ArrayList<UUID> outOfClass = new ArrayList<>();

    private void joinClass(Player player) {
        outOfClass.remove(player.getUniqueId());
        classPlayers.add(player.getUniqueId());
    }

    private void endClass(Player player) {
        classPlayers.clear();
        outOfClass.add(player.getUniqueId());
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent e) {
        outOfClass.add(e.getPlayer().getUniqueId());
    }

    @EventHandler
    private void onLeave(PlayerQuitEvent e) {
        outOfClass.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {

        String classprefix = mainClass.getConfig().getString("class-prefix").replace('&', '§');

        Player player = e.getPlayer();

        if(classPlayers.contains(e.getPlayer().getUniqueId())) {

            for(Player pl : Bukkit.getOnlinePlayers()) {

                if(classPlayers.contains(pl.getUniqueId())) {
                    if(player.hasPermission("oc.chat") || player.isOp()) {
                        e.setCancelled(true);
                        pl.sendMessage(classprefix + e.getPlayer().getDisplayName() + ChatColor.GRAY + ": " + ChatColor.RESET + e.getMessage());
                    } else {
                        e.setCancelled(true);
                    }
                }
            }
        }

        if(outOfClass.contains(e.getPlayer().getUniqueId())) {

            for(Player pl : Bukkit.getOnlinePlayers()) {

                if(outOfClass.contains(pl.getUniqueId())) {
                    e.setCancelled(true);
                    pl.sendMessage(e.getPlayer().getDisplayName() + ChatColor.GRAY + " » " + ChatColor.RESET + e.getMessage());
                } else {
                    e.setCancelled(true);
                }
            }
        }
    }

    private ObscurusClasses mainClass;

    public ClassCreate(ObscurusClasses main) {
        this.mainClass = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        String prefix = mainClass.getConfig().getString("plugin-prefix").replace('&', '§');
        String noPerm = mainClass.getConfig().getString("noPerm").replace('&', '§');

        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("class")) {
            if (args.length == 0) {
                player.sendMessage(prefix + "You need to specify more arguments! /class help");

            } else if (player.hasPermission("obscurusclasses.class")) {
                if (args[0].equalsIgnoreCase("help")) {
                    player.sendMessage(ChatColor.GRAY + "==========");
                    player.sendMessage(ChatColor.AQUA + "/class help" + ChatColor.GRAY + " (shows this page)");
                    player.sendMessage(ChatColor.AQUA + "/class create <className>" + ChatColor.GRAY + " (creates a class)");
                    player.sendMessage(ChatColor.AQUA + "/class end <className>" + ChatColor.GRAY + " (ends a current class)");
                    player.sendMessage(ChatColor.AQUA + "/class call <student>" + ChatColor.GRAY + " (calls upon a student)");
                    player.sendMessage(ChatColor.AQUA + "/class uncall <student>" + ChatColor.GRAY + " (uncalls a student)");
                    player.sendMessage(ChatColor.AQUA + "/class silence" + ChatColor.GRAY + " (silences the class)");
                    player.sendMessage(ChatColor.AQUA + "/mgive <student> ac <amount>" + ChatColor.GRAY + " (gives target student AC)");
                    player.sendMessage(ChatColor.GRAY + "==========");

                } else if (args[0].equalsIgnoreCase("create")) {
                    if (player.hasPermission("obscurusclasses.class.create")) {
                        if (args.length == 2) {

                            File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("ObscurusClasses").getDataFolder(), File.separator + "ClassLocations");
                            File file = new File(userdata, File.separator + args[1] + ".yml");
                            FileConfiguration playerData = YamlConfiguration.loadConfiguration(file);

                            if (!file.exists()) {
                                try {

                                    playerData.createSection(args[1]);
                                    playerData.set("world", player.getWorld().getName());
                                    playerData.set("x", player.getLocation().getBlockX());
                                    playerData.set("y", player.getLocation().getBlockY());
                                    playerData.set("z", player.getLocation().getBlockZ());

                                    playerData.save(file);

                                    if (!classPlayers.contains(player.getUniqueId())) {
                                        player.sendMessage(prefix + ChatColor.AQUA + "You have joined the class! Please wait until the professor is ready.");
                                        outOfClass.remove(player.getUniqueId());
                                        joinClass(player);
                                    } else if (classPlayers.contains(player.getUniqueId())) {
                                        player.sendMessage(prefix + "You are already in this class!");
                                    }

                                    for (Player online : Bukkit.getOnlinePlayers()) {
                                        online.sendTitle(ChatColor.AQUA + args[1] + ChatColor.GRAY + " is starting!", ChatColor.GRAY + "/class join " + ChatColor.AQUA + args[1], 10, 60, 10);
                                        online.sendMessage(prefix + ChatColor.AQUA + args[1] + ChatColor.GRAY + " is starting! " + "It is being hosted by " + ChatColor.BLUE + player.getDisplayName() + ChatColor.GRAY + "! " + ChatColor.GRAY + "/class join " + args[1]);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            player.sendMessage(prefix + ChatColor.GRAY + "You need to specify! /class create <class>");
                        }
                    } else {
                        player.sendMessage(prefix + noPerm);
                    }
                } else if (args[0].equalsIgnoreCase("join")) {

                    File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("ObscurusClasses").getDataFolder(), File.separator + "ClassLocations");
                    File file = new File(userdata, File.separator + args[1] + ".yml");
                    FileConfiguration playerData = YamlConfiguration.loadConfiguration(file);

                    if (player.hasPermission("obscurusclasses.class.join") || player.isOp()) {
                        if(args.length == 2) {
                            if (file == null) {
                                player.sendMessage(prefix + ChatColor.AQUA + "This class does not exist!");
                            } else if (file.exists()) {
                                try {

                                    playerData.createSection(player.getName());
                                    playerData.set(player.getUniqueId().toString() + ".lastLocation.x", player.getLocation().getX());
                                    playerData.set(player.getUniqueId().toString() + ".lastLocation.y", player.getLocation().getY());
                                    playerData.set(player.getUniqueId().toString() + ".lastLocation.z", player.getLocation().getZ());
                                    playerData.save(file);

                                    if (!classPlayers.contains(player.getUniqueId())) {
                                        joinClass(player);
                                        player.sendMessage(prefix + ChatColor.AQUA + "You have joined the queue! Please wait until the professor is ready to teleport you.");
                                    } else if (classPlayers.contains(player.getUniqueId())) {
                                        player.sendMessage(prefix + "You are already in the queue!");
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            player.sendMessage(prefix + ChatColor.GRAY + "You need to specify! /class join <class>");
                        }
                        return false;
                    }
                } else if (args[0].equalsIgnoreCase("tp")) {

                    File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("ObscurusClasses").getDataFolder(), File.separator + "ClassLocations");
                    File file = new File(userdata, File.separator + args[1] + ".yml");
                    FileConfiguration playerData = YamlConfiguration.loadConfiguration(file);

                    if(player.hasPermission("obscurusclasses.class.tp") || player.isOp()) {
                        if(args.length == 2) {
                            if (file == null) {
                                player.sendMessage(prefix + ChatColor.AQUA + "This class does not exist!");
                            } else if (file.exists()) {
                                try {

                                    int x = playerData.getInt("x");
                                    int y = playerData.getInt("y");
                                    int z = playerData.getInt("z");

                                    Location l = new Location(player.getWorld(), x, y, z);

                                    for(UUID classStudent : classPlayers) {
                                        if (classPlayers.contains(player.getUniqueId())) {
                                            Player student = Bukkit.getPlayer(classStudent);
                                            joinClass(student);
                                            student.sendMessage(prefix + ChatColor.AQUA + "You have been teleported to the class " + args[1] + "!");
                                            student.teleport(l);
                                        }
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            player.sendMessage(prefix + ChatColor.GRAY + "You need to specify! /class tp <class>");
                        }
                    }

                }

                else if (args[0].equalsIgnoreCase("end")) {

                    File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("ObscurusClasses").getDataFolder(), File.separator + "ClassLocations");
                    File file = new File(userdata, File.separator + args[1] + ".yml");
                    FileConfiguration playerData = YamlConfiguration.loadConfiguration(file);

                    if (player.hasPermission("obscurusclasses.class.end") || player.isOp()) {
                        if(args.length == 2) {
                            if (file.delete()) {
                                for (Player online : Bukkit.getOnlinePlayers()) {
                                    online.sendMessage(prefix + ChatColor.AQUA + "The " + args[1] + " class has ended.");

                                    //cannot convert double to int. find a work around tomorrow. get some sleep.
                                    int lx = (int) playerData.get(".lastLocation.x", player.getLocation().getX());
                                    int ly = (int) playerData.get(".lastLocation.y", player.getLocation().getY());
                                    int lz = (int) playerData.get(".lastLocation.z", player.getLocation().getZ());

                                    Location lastLoc = new Location(player.getWorld(), lx, ly, lz);

                                    //need to fix. it's not teleporting the player. maybe find a better way to get data from config??

                                    online.teleport(lastLoc);

                                    endClass(online);
                                }
                            } else {
                                player.sendMessage(prefix + ChatColor.AQUA + "This class doesn't exist.");
                            }
                        } else {
                            player.sendMessage(prefix + ChatColor.GRAY + "You need to specify! /class end <class>");
                        }
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("call")) {

                    if(player.hasPermission("obscurusclasses.class.call") || player.isOp()) {
                        if(args.length == 2) {
                            Player target = Bukkit.getPlayerExact(args[1]);

                            if (target instanceof Player) {

                                for (Player pl : Bukkit.getOnlinePlayers()) {
                                    pl.sendMessage(player.getDisplayName() + ChatColor.GRAY + " has called on " + ChatColor.AQUA + target.getDisplayName() + "!");
                                }

                                ObscurusClasses.perms.playerAdd(target, "oc.chat");
                            } else {
                                player.sendMessage(prefix + ChatColor.AQUA + target.getDisplayName() + ChatColor.GRAY + " is not a player in your class!");
                            }
                        } else {
                            player.sendMessage(prefix + ChatColor.GRAY + "You need to specify! /class call <player>");
                        }
                    }

                } else if (args[0].equalsIgnoreCase("uncall")) {

                    if(player.hasPermission("obscurusclasses.class.uncall") || player.isOp()) {

                        if(args.length == 2) {

                            Player target = Bukkit.getPlayerExact(args[1]);

                            for(Player pl : Bukkit.getOnlinePlayers()) {

                                if(target != null) {

                                    if(classPlayers.contains(pl.getUniqueId())) {

                                        ObscurusClasses.perms.playerRemove(target, "oc.chat");
                                        pl.sendMessage(player.getDisplayName() + ChatColor.GRAY + " has stopped calling on " + ChatColor.AQUA + target.getDisplayName() + "!");

                                    }
                                }  else {
                                    player.sendMessage(prefix + ChatColor.AQUA + args[1] + ChatColor.GRAY + " is not a player in your class!");
                                }
                            }
                        } else {
                            player.sendMessage(prefix + ChatColor.GRAY + "You need to specify! /class uncall <player>");
                        }
                    }

                } else if (args[0].equalsIgnoreCase("silence")) {

                    if(player.hasPermission("obscurusclasses.class.silence") || player.isOp()) {

                        if(args.length == 1) {

                            for(Player pl : Bukkit.getOnlinePlayers()) {

                                if(classPlayers.contains(pl.getUniqueId())) {

                                    ObscurusClasses.perms.playerRemove(pl, "oc.chat");
                                    pl.sendMessage(prefix + ChatColor.GRAY + "The class has been silenced!");

                                } else {
                                    player.sendMessage(prefix + ChatColor.GRAY + "You are currently not in a class!");
                                }
                            }

                        }
                    }
                }
            } else {
                player.sendMessage(prefix + noPerm);
            }

            return false;
        }
        return false;
    }
}
