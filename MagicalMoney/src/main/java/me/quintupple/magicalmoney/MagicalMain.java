package me.quintupple.magicalmoney;

import me.quintupple.magicalmoney.GUI.GUI;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class MagicalMain extends JavaPlugin implements CommandExecutor {

    public static Economy econ = null;

    @Override
    public void onEnable() {
        //register config
        getConfig();
        saveDefaultConfig();
        //commands
        getCommand("pmoney").setExecutor(new GUI(this));
        getCommand("mmreload").setExecutor(new Reload(this));
        getCommand("currency").setExecutor(new Currency(this));
        //events
        getServer().getPluginManager().registerEvents(new GUI(this), this);

        //economy
        setupEconomy();
        if (!setupEconomy() ) {
            System.out.println(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
}
