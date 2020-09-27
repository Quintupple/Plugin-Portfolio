package me.quintupple.channelerised;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class staff implements Listener {

    public channelerised mainClass;

    public staff(channelerised main) {
        this.mainClass = main;
    }

    @EventHandler
    public void OnAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {

        event.setCancelled(true);

        String staffprefix = mainClass.getConfig().getString("staff-prefix").replace('&', '§');
        String chatcolour = mainClass.getConfig().getString("staff-chat-colour").replace('&', '§');
        String noperm = mainClass.getConfig().getString("noperm-staff").replace('&', '§');

        Player executor = event.getPlayer();

        Chat ch = Bukkit.getServer().getServicesManager().getRegistration(Chat.class).getProvider();
        String rank = ch.getPlayerPrefix(executor).replace('&', '§');

        if (event.getMessage().startsWith("+")) {

            if (executor.hasPermission("echat.staff")) {

                for (Player player : Bukkit.getServer().getOnlinePlayers()) {

                    if (player.hasPermission("echat.staff")) {
                        player.sendMessage(staffprefix + rank +
                                executor.getDisplayName() + ChatColor.GRAY + ": " + chatcolour + event.getMessage().substring(1));
                        System.out.println(executor.getName() + ": " + event.getMessage());

                    } else if(!executor.hasPermission("echat.staff")) {
                        player.sendMessage(noperm);
                        event.setCancelled(true);
                    }
                }
            } else if (!executor.hasPermission("echat.staff") || !executor.isOp()) {
                executor.sendMessage(noperm);
                event.setCancelled(true);
            }
        }
    }
}
