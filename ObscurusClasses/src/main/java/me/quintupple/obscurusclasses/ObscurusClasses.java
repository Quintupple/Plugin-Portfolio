package me.quintupple.obscurusclasses;

import com.elmakers.mine.bukkit.api.magic.MageController;
import com.elmakers.mine.bukkit.api.magic.MagicAPI;
import me.quintupple.obscurusclasses.Commands.ClassCreate;
import me.quintupple.obscurusclasses.Commands.Help;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class ObscurusClasses extends JavaPlugin implements CommandExecutor {

    public static Chat chat = null;
    public static Permission perms = null;

    public MageController magic = magicAPI().getController();

    @Override
    public void onEnable() {
        //register stuff
        setupChat();
        magicAPI();
        setupPermissions();

        //config
        getConfig();
        saveDefaultConfig();

        //commands and listeners
        getCommand("ochelp").setExecutor(new Help(this));
        getCommand("class").setExecutor(new ClassCreate(this));
        getServer().getPluginManager().registerEvents(new ClassCreate(this), this);

        System.out.println("ObscurusClasses plugin is online!");
    }

    @Override
    public void onDisable() {
        System.out.println("ObscurusClasses plugin is shutting down!");
    }

    MagicAPI magicAPI() {
        Plugin magicPlugin = Bukkit.getPluginManager().getPlugin("Magic");
        if (magicPlugin == null || !(magicPlugin instanceof MagicAPI)) {
            return null;
        }
        return (MagicAPI)magicPlugin;
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    public static Chat getChat() {
        return chat;
    }

    public static Permission getPermissions() {
        return perms;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;

        String prefix = getConfig().getString("plugin-prefix").replace('&', '§');

        if (sender instanceof Player) {
            if (command.getName().equalsIgnoreCase("ocrl")) {
                if (player.hasPermission("obscurusclasses.reload") || player.isOp()) {
                    reloadConfig();
                    saveDefaultConfig();
                    player.sendMessage(prefix + ChatColor.GRAY + "Configuration reloaded!");
                }
            } else {
                System.out.println("You must be a player in order to do this command.");
            }
        }
        return false;
    }
}
