package me.quintupple.magicalmoney;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Reload implements CommandExecutor {

    public MagicalMain main;

    public Reload(MagicalMain main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        String currency = main.getConfig().getString("currency").replace('&', '§');
        String prefix = main.getConfig().getString("prefix").replace('&', '§');
        String noPerm = main.getConfig().getString("noPerm").replace('&', '§');

        Player player = (Player) sender;

        if(command.getName().equalsIgnoreCase("mmreload")) {
            if(player.hasPermission("magicalmoney.reload") || player.isOp()) {
                main.reloadConfig();
                main.saveDefaultConfig();
                player.sendMessage(prefix + ChatColor.RED + "Configuration reloaded!" + ChatColor.GOLD + ChatColor.BOLD + " The currency is: " + currency + "!");
            } else {
                player.sendMessage(prefix + noPerm);
            }
        }

        return false;
    }
}
