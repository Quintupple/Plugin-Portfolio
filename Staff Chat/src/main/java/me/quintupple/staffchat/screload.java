package me.quintupple.staffchat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class screload extends JavaPlugin implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        System.out.println("oncommand is working");

        if(sender instanceof Player) {
            System.out.println("instance of is working");


            Player player = (Player) sender;
            System.out.println("player sender is working");

        if (command.getName().equalsIgnoreCase("screload")) {
            System.out.println("this is working");
            if (player.hasPermission("staffchat.reload") || player.isOp()) {
                reloadConfig();
                saveDefaultConfig();
                player.sendMessage(ChatColor.GOLD + "Staff Chat config has been reloaded!");
            } else {
                player.sendMessage(ChatColor.RED + "You must be " + ChatColor.GOLD + "STAFF" + ChatColor.RED + " to use this command.");
                } return false;
            }
        }
        return false;
    }
}
