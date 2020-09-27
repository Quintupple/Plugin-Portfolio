package me.quintupple.channelerised;

import me.quintupple.channelerised.houses.gryffindor;
import me.quintupple.channelerised.houses.hufflepuff;
import me.quintupple.channelerised.houses.ravenclaw;
import me.quintupple.channelerised.houses.slytherin;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class channelerised extends JavaPlugin implements Listener, CommandExecutor {

    private static Permission perms = null;
    private static Chat chat = null;

    public global other;

    @Override
    public void onEnable() {
        other = new global(this);
        setupPermissions();
        setupChat();
        getConfig();
        saveDefaultConfig();
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new global(this), this);
        getServer().getPluginManager().registerEvents(new local(this), this);
        getServer().getPluginManager().registerEvents(new staff(this), this);
        getServer().getPluginManager().registerEvents(new headstaff(this), this);
        getServer().getPluginManager().registerEvents(new gryffindor(this), this);
        getServer().getPluginManager().registerEvents(new hufflepuff(this), this);
        getServer().getPluginManager().registerEvents(new ravenclaw(this), this);
        getServer().getPluginManager().registerEvents(new slytherin(this), this);
        getCommand("echannels").setExecutor(new channels(this));
    }

    public boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    public boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        String configreload = getConfig().getString("config-reload").replace('&', '§');
        String noperm = getConfig().getString("noperm-configreload").replace('&', '§');

        Player player = (Player) sender;

        if(command.getName().equalsIgnoreCase("ecrl")) {
            if(player.hasPermission("echat.reload") || player.isOp()) {
                reloadConfig();
                saveDefaultConfig();
                player.sendMessage(configreload);
            } else {
                player.sendMessage(noperm);
            }
        }
        return false;
    }
}
