package me.quintupple.magicalmoney;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Currency implements CommandExecutor {

    public MagicalMain main;

    public Currency(MagicalMain main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        String prefix = main.getConfig().getString("prefix").replace('&', '§');
        String currency = main.getConfig().getString("currency").replace('&', '§');
        String noPerm = main.getConfig().getString("noPerm").replace('&', '§');

        Player player = (Player) sender;

        if(command.getName().equalsIgnoreCase("currency")) {
            if(player.hasPermission("magicalmoney.currency") || player.isOp()) {
                player.sendMessage(prefix + ChatColor.RED + "The currency is: " + currency);
            } else {
                player.sendMessage(prefix + noPerm);
            }
        }

        return false;
    }
}
