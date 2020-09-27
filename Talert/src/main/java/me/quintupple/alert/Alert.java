package me.quintupple.alert;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class Alert extends JavaPlugin implements CommandExecutor {

    public alertcommand other;
    public titlealert other1;

    @Override
    public void onEnable() {

        other = new alertcommand(this);
        other1 = new titlealert(this);

        System.out.println("Alert plugin is online!");

        getCommand("alert").setExecutor(new alertcommand(this));
        getCommand("talert").setExecutor(new titlealert(this));

        getConfig();
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        System.out.println("Alert Plugin is shutting down!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        String no_permission = getConfig().getString("no_permission").replace('&', '§');
        String prefix = getConfig().getString("alert_prefix").replace('&', '§');

        Player p = (Player) sender;

        if(command.getName().equalsIgnoreCase("areload")) {
            if(p.hasPermission("alert.reload") || p.isOp()) {
                reloadConfig();
                saveDefaultConfig();
                p.sendMessage(prefix + "The configuration file has been reloaded.");
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
            } else if (!p.hasPermission("alert.reload") || !p.isOp()) {
                p.sendMessage(no_permission);
            }
        }
        return false;
    }
}
