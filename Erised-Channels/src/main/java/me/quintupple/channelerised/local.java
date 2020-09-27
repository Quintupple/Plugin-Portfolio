package me.quintupple.channelerised;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class local implements Listener {

    public channelerised mainClass;

    public local(channelerised main) {
        this.mainClass = main;
    }

    @EventHandler
    public void OnAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {

        event.setCancelled(true);

        String localprefix = mainClass.getConfig().getString("local-prefix").replace('&', '§');
        String noperm = mainClass.getConfig().getString("noperm-local").replace('&', '§');

        Player executor = event.getPlayer();

        Chat ch = Bukkit.getServer().getServicesManager().getRegistration(Chat.class).getProvider();
        String rank = ch.getPlayerPrefix(executor).replace('&', '§');

        //distance
        int blockDistance = 100;

        Location playerLocation = executor.getLocation();

        if (!event.getMessage().startsWith("!") && !event.getMessage().startsWith("%") && !event.getMessage().startsWith("?")
        && !event.getMessage().startsWith("#") && !event.getMessage().startsWith("~") && !event.getMessage().startsWith("+")
        && !event.getMessage().startsWith("@")) {

            if (executor.hasPermission("echat.local")) {
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {

                    if(player.getLocation().distance(playerLocation) <= blockDistance) {
                        if (player.hasPermission("echat.local")) {
                            player.sendMessage(localprefix + rank +
                                    executor.getDisplayName() + ChatColor.GRAY + ": " + ChatColor.WHITE + event.getMessage());
                            System.out.println(executor.getName() + ": " + event.getMessage());

                        }
                    } else if(!executor.hasPermission("echat.local")) {
                        player.sendMessage(noperm);
                        event.setCancelled(true);
                    }
                }
            } else if (!executor.hasPermission("echat.local") || !executor.isOp()) {
                executor.sendMessage(noperm);
                event.setCancelled(true);
            }

        }
    }
}
