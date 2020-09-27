package me.quintupple.channelerised;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class headstaff implements Listener {

    Chat ch = Bukkit.getServer().getServicesManager().getRegistration(Chat.class).getProvider();

    public channelerised mainClass;

    public headstaff(channelerised main) {
        this.mainClass = main;
    }

    @EventHandler
    public void OnAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {

        event.setCancelled(true);

        String headstaffprefix = mainClass.getConfig().getString("head-staff-prefix").replace('&', '§');
        String chatcolourhs = mainClass.getConfig().getString("head-staff-chat-colour").replace('&', '§');
        String noperm = mainClass.getConfig().getString("noperm-headstaff").replace('&', '§');

        Player executor = event.getPlayer();

        String rank = ch.getPlayerPrefix(executor).replace('&', '§');

        if (event.getMessage().startsWith("@")) {

            if (executor.hasPermission("echat.headstaff")) {

                for (Player player : Bukkit.getServer().getOnlinePlayers()) {

                    if (player.hasPermission("echat.headstaff")) {
                            player.sendMessage(headstaffprefix + rank + executor.getDisplayName() + ChatColor.GRAY + ": "
                                    + chatcolourhs + event.getMessage().substring(1));

                    } else if(!executor.hasPermission("echat.headstaff")) {
                        player.sendMessage(noperm);
                        event.setCancelled(true);
                    }
                }
            } else if (!executor.hasPermission("echat.headstaff") || !executor.isOp()) {
                executor.sendMessage(noperm);
                event.setCancelled(true);
            }
        }
    }
}
