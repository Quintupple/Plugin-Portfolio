package me.quintupple.channelerised;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class channels implements CommandExecutor {

    public channelerised mainClass;

    public channels(channelerised main) {
        this.mainClass = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // No perm message
        String noperm = mainClass.getConfig().getString("noperm-channels").replace('&', '§');

        // Defines player sender
        Player player = (Player) sender;

        // Channel list (not working)
        /*String message = ChatColor.GRAY + "==========================" + "\r\n" + ChatColor.RED + "Channels:"
                + System.lineSeparator() + ChatColor.GOLD + "[!] Global" + "\r\n" +
                ChatColor.GOLD + "Local" + System.lineSeparator() + ChatColor.GOLD + "[%] Gryffindor"
                + System.lineSeparator() + ChatColor.GOLD + "[?] Hufflepuff" + System.lineSeparator()
                + ChatColor.GOLD + "[#] Ravenclaw" + System.lineSeparator()
                + ChatColor.GOLD + "[~] Slytherin" + System.lineSeparator() + ChatColor.GOLD + "[+] Staff" + System.lineSeparator()
                + ChatColor.GOLD + "[@] Head Staff" + System.lineSeparator() + ChatColor.GRAY + "==========================";*/

        //Channel List - Had to do it like this because the CR code was appearing in place of the System.lineSeparator()
        String equals = ChatColor.GRAY + "==========================";
        String channels = ChatColor.RED + "Channels:";
        String global = ChatColor.GOLD + "[!] Global";
        String local = ChatColor.GOLD + "Local";
        String gryffindor = ChatColor.GOLD + "[%] Gryffindor";
        String hufflepuff = ChatColor.GOLD + "[?] Hufflepuff";
        String ravenclaw = ChatColor.GOLD + "[#] Ravenclaw";
        String slytherin = ChatColor.GOLD + "[~] Slytherin";
        String staff = ChatColor.GOLD + "[+] Staff";
        String headstaff = ChatColor.GOLD + "[@] Head Staff";

        // Command to get list
        if (command.getName().equalsIgnoreCase("echannels")) {
            if (player.hasPermission("echat.channels") || player.isOp()) {
                player.sendMessage(equals);
                player.sendMessage(channels);
                player.sendMessage(global);
                player.sendMessage(local);
                player.sendMessage(gryffindor);
                player.sendMessage(hufflepuff);
                player.sendMessage(ravenclaw);
                player.sendMessage(slytherin);
                player.sendMessage(staff);
                player.sendMessage(headstaff);
                player.sendMessage(equals);
            } else {
                player.sendMessage(noperm);
            }
        }
        return false;
    }
}
