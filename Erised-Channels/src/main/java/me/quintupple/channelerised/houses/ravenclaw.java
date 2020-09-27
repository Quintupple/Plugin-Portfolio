package me.quintupple.channelerised.houses;

import me.quintupple.channelerised.channelerised;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ravenclaw implements Listener {

    public channelerised mainClass;

    public ravenclaw(channelerised main) {
        this.mainClass = main;
    }

    @EventHandler
    public void OnAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {

        event.setCancelled(true);

        String ravenclawprefix = mainClass.getConfig().getString("ravenclaw-prefix").replace('&', '§');

        String noperm = mainClass.getConfig().getString("noperm-ravenclaw").replace('&', '§');

        Player executor = event.getPlayer();

        Chat ch = Bukkit.getServer().getServicesManager().getRegistration(Chat.class).getProvider();
        String rank = ch.getPlayerPrefix(executor).replace('&', '§');

        if (event.getMessage().startsWith("#")) {

            if (executor.hasPermission("echat.ravenclaw")) {

                for (Player player : Bukkit.getServer().getOnlinePlayers()) {

                    if (player.hasPermission("echat.ravenclaw")) {
                        player.sendMessage(ravenclawprefix + rank +
                                executor.getDisplayName() + ChatColor.GRAY + ": " + ChatColor.WHITE + event.getMessage().substring(1));
                        System.out.println(executor.getName() + ": " + event.getMessage());

                    } else if(!executor.hasPermission("echat.ravenclaw")) {
                        player.sendMessage(noperm);
                        event.setCancelled(true);
                    }
                }
            } else if (!executor.hasPermission("echat.ravenclaw") || !executor.isOp()) {
                executor.sendMessage(noperm);
                event.setCancelled(true);
            }
        }
    }
}
