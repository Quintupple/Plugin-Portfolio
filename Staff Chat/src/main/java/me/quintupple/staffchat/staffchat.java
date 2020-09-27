package me.quintupple.staffchat;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class staffchat extends JavaPlugin implements Listener, CommandExecutor {

    private static Chat chat = null;

    Chat ch = Bukkit.getServer().getServicesManager().getRegistration(Chat.class).getProvider();

    String prefix = getConfig().getString("prefix").replace("&", "§");

    public void onEnable() {
        setupChat();
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        saveDefaultConfig();
        //reference classes
        System.out.println(ChatColor.AQUA + "Staff Chat is online!");
    }

    public boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {

        Player play = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("screload")) {
            if (play.hasPermission("staffchat.reload") || play.isOp()) {
                reloadConfig();
                saveDefaultConfig();
                play.sendMessage(ChatColor.GOLD + "Staff Chat config has been reloaded!");
            } else {
                play.sendMessage(ChatColor.RED + "You must be " + ChatColor.GOLD + "STAFF" + ChatColor.RED + " to use this command.");
            } return false;
        }


        if (sender != null) {
            if (cmd.getName().equalsIgnoreCase("staffchat")) {
                if (args.length == 0) {
                    sender.sendMessage(ChatColor.RED + "You need to define your argument" + ChatColor.GRAY + " (/staffchat <message>)");
                    return true;
                }

                    StringBuilder b = new StringBuilder();
                    for (int i = 0; i < args.length; i++) {
                        b.append(args[i]);
                        b.append(" ");
                    }

                Player p = (Player) sender;

                String playerrank = ch.getPlayerPrefix(p).replace('&', '§');
                String playersuffix = ch.getPlayerSuffix(p).replace('&', '§');

                    System.out.println(prefix + playerrank + p.getDisplayName() + ": " + b.toString()); //sends message to console
                        for (Player player : Bukkit.getServer().getOnlinePlayers()) {

                                String chatColour = getConfig().getString("chatColour").replace("&", "§");

                                if (player.hasPermission("staffchat.chat") || player.isOp()) {
                                    player.sendMessage(prefix + playerrank + p.getDisplayName() + playersuffix + ChatColor.DARK_GRAY + ": " + chatColour + b.toString());

                                } else {
                                    player.sendMessage(ChatColor.RED + "You must be " + ChatColor.GOLD + "STAFF" + ChatColor.RED + " to use this command.");
                                    return false;

                            }
                    }
            }
            return true;
        }
        return false;
    }

}

