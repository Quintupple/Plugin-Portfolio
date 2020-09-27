package me.quintupple.channelerised;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class global implements Listener {

    public channelerised mainClass;

    public global(channelerised main) {
        this.mainClass = main;
    }

    @EventHandler
    public void OnAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {

        event.setCancelled(true);

        Player executor = event.getPlayer();

        String globalprefix = mainClass.getConfig().getString("global-prefix").replace('&', '§');
        String noperm = mainClass.getConfig().getString("noperm-global").replace('&', '§');

        Chat ch = Bukkit.getServer().getServicesManager().getRegistration(Chat.class).getProvider();
        String rank = ch.getPlayerPrefix(executor).replace('&', '§');

        if (event.getMessage().startsWith("!")) {
            if (executor.hasPermission("echat.global")) {
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    if (player.hasPermission("echat.global")) {
                        player.sendMessage(globalprefix + rank + executor.getDisplayName() + ChatColor.GRAY + ": "
                                + ChatColor.WHITE + event.getMessage().substring(1));
                        System.out.println(executor.getName() + ": " + event.getMessage());

                    } else if(!executor.hasPermission("echat.global")) {
                        player.sendMessage(noperm);
                        event.setCancelled(true);
                    }
                }
            } else if (!executor.hasPermission("echat.global") || !executor.isOp()) {
                executor.sendMessage(noperm);
                event.setCancelled(true);
            }
        }
    }
}
