package me.quintupple.alert;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class alertcommand implements CommandExecutor, Listener {

    public Alert mainClass;

    public alertcommand(Alert main) {
        this.mainClass = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;

        StringBuilder b = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            b.append(args[i]);
            b.append(" ");
        }

        World w = player.getWorld();

        String wrongusagealertsound = mainClass.getConfig().getString("wrong_usage_alert_sound");
        String alertsound = mainClass.getConfig().getString("alert_sound");
        String no_permission = mainClass.getConfig().getString("no_permission").replace('&', '§');
        String prefix = mainClass.getConfig().getString("alert_prefix").replace('&', '§');

        if(command.getName().equalsIgnoreCase("alert")) {
            for (Player all : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("alert.alert") || player.isOp()) {
                    if (args.length == 0) {
                        player.playSound(player.getLocation(), Sound.valueOf(wrongusagealertsound), 5, 1);
                        player.sendMessage(prefix + "Wrong usage! /alert [message]");
                    } else if (args.length > 0) {
                        for (Player allp : Bukkit.getOnlinePlayers()) {
                            w.playSound(allp.getLocation(), Sound.valueOf(alertsound), 6, 1);
                            allp.sendMessage(prefix + b.toString().replace('&', '§'));
                        }
                    } return false;
                } else if (!player.hasPermission("alert.alert") || !player.isOp()) {
                    player.sendMessage(no_permission);
                    return true;
                }
            }
        }
        return false;
    }
}
