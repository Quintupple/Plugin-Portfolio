package me.quintupple.obscurusclasses.Commands;

import me.quintupple.obscurusclasses.ObscurusClasses;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Help implements CommandExecutor {

    public ObscurusClasses main;

    public Help(ObscurusClasses main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;

            if(command.getName().equalsIgnoreCase("ochelp")) {
                if(player.hasPermission("obscurusclasses.help") || player.isOp()) {
                    if(args.length == 0) {
                        player.sendMessage(ChatColor.GRAY + "==============");
                        player.sendMessage(ChatColor.AQUA + "/ocrl" + ChatColor.GRAY + " (reload the config)");
                        player.sendMessage(ChatColor.AQUA + "/class help" + ChatColor.GRAY + " (shows the class help page)");
                        player.sendMessage(ChatColor.GRAY + "==============");
                    } else if(args[0].equalsIgnoreCase("2")) {
                        player.sendMessage(ChatColor.RED + "There is no page for this yet.");
                    }
                }
            }

        return false;
    }
}
