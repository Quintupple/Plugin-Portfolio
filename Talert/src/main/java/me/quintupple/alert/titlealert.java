package me.quintupple.alert;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class titlealert implements CommandExecutor {

    public Alert mainClass;

    public titlealert(Alert main) {
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
        String talertsound = mainClass.getConfig().getString("talert_sound");
        String no_permission = mainClass.getConfig().getString("no_permission").replace('&', '§');
        String prefix = mainClass.getConfig().getString("alert_prefix").replace('&', '§');
        String talertsubcolour = mainClass.getConfig().getString("talert_subtitle_colour").replace('&', '§');

        if(command.getName().equalsIgnoreCase("talert")) {
            for (Player all : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("alert.talert") || player.isOp()) {
                    if (args.length == 0) {
                        player.playSound(player.getLocation(), Sound.valueOf(wrongusagealertsound), 5, 1);
                        player.sendMessage(prefix + "Wrong usage! /talert [message]");
                    } else if (args.length > 0) {
                        for (Player allp : Bukkit.getOnlinePlayers()) {
                            w.playSound(allp.getLocation(), Sound.valueOf(talertsound), 6, 1);
                            allp.sendTitle(prefix, talertsubcolour + b.toString().replace('&', '§'), 5, 80, 5);
                        }
                    } return false;
                } else if (!player.hasPermission("alert.talert") || !player.isOp()) {
                    player.sendMessage(no_permission);
                return false;
                }
             }
        }
        return false;
    }
}
