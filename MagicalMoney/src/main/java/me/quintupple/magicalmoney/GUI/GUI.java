package me.quintupple.magicalmoney.GUI;

import me.quintupple.magicalmoney.MagicalMain;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

import static me.quintupple.magicalmoney.MagicalMain.econ;

public class GUI implements CommandExecutor, Listener {

    public Inventory inv;
    public Inventory invanvil;

    public MagicalMain main;
    private OfflinePlayer offline;

    public GUI(MagicalMain main) {
        this.main = main;
    }

    public static boolean isNumeric(String strNum) {
        if(strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @EventHandler
    public void onClickEvent(InventoryClickEvent event) {

        String prefix = main.getConfig().getString("prefix").replace('&', '§');
        String currency = main.getConfig().getString("currency").replace('&', '§');

        Player player = (Player) event.getWhoClicked();

        if(event.getView().getTitle().equalsIgnoreCase(ChatColor.RED + "Bank")) {
            if(event.getCurrentItem().getType().equals(Material.BARRIER)) {
                event.setCancelled(true);
                player.closeInventory();
            } else if(event.getCurrentItem().getType().equals(Material.PLAYER_HEAD)) {
                event.setCancelled(true);
            } else if(event.getSlot() == 22) { //withdraw galleons
                event.setCancelled(true);
                player.closeInventory();
                new AnvilGUI.Builder()
                        .onComplete((p, text) -> {           //called when the inventory output slot is clicked
                            if(isNumeric(text)) {
                                long value = (long) Math.round(Integer.parseInt(text));
                                Double balance = econ.getBalance(player);
                                if(balance >= value) {
                                    econ.withdrawPlayer(player, value);
                                    //player.performCommand("eco take " + player.getName() + " " + value);
                                    player.sendMessage(prefix + ChatColor.YELLOW + "You have withdrawn " + currency + value + ".");
                                    player.getInventory().addItem(new ItemStack(Material.GOLD_NUGGET, (int) value));
                                } else {
                                    player.sendMessage(prefix + ChatColor.RED + "You do not have the money required.");
                                }
                                    return AnvilGUI.Response.close();
                            } else {
                                return AnvilGUI.Response.text("Insufficient Value");
                            }
                        })

                        .text("Amount")     //sets the text the GUI should start with
                        .item(new ItemStack(Material.GOLD_NUGGET)) //use a custom item for the first slot
                        .title("Enter Amount")              //set the title of the GUI (only works in 1.14+)
                        .plugin(main)                 //set the plugin instance
                        .open(player);                          //opens the GUI for the player provided

            } else if(event.getSlot() == 31) { //withdraw galleons
                event.setCancelled(true);
                player.closeInventory();

                PlayerInventory inv = player.getInventory();

                for(ItemStack is : inv.all(Material.GOLD_NUGGET).values()) {
                    if(is != null && is.getType() == Material.GOLD_NUGGET) {
                        int count = 0;
                        count = is.getAmount();

                        econ.depositPlayer(player, count);
                        player.getInventory().remove(Material.GOLD_NUGGET);
                        player.sendMessage(prefix + ChatColor.YELLOW + "You have deposited " + currency + count + ".");
                    } else if(is == null) {
                        player.sendMessage("Null");
                    }

                }
            } else if(event.getCurrentItem().getType().equals(Material.BLACK_STAINED_GLASS_PANE)) {
                event.setCancelled(false);
                player.closeInventory();
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        String prefix = main.getConfig().getString("prefix").replace('&', '§');
        String noPerm = main.getConfig().getString("noPerm").replace('&', '§');

        Player player = (Player) sender;

            if(command.getName().equalsIgnoreCase("pmoney")) {
                if(player.hasPermission("magicalmoney.pmoney") || player.isOp()) {
                    GUI(player);
                    return true;
                } else {
                    player.sendMessage(prefix + noPerm);
                }
            }

        return false;
    }

    public void GUI(Player player) {
        //inventory
        inv = Bukkit.createInventory(null, 54, ChatColor.RED + "Bank");

        //items
        ItemStack phead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta playermeta = (SkullMeta) phead.getItemMeta();
        playermeta.setDisplayName(player.getDisplayName());
        playermeta.setLore(Arrays.asList(ChatColor.RED + "Balance: " + econ.format(econ.getBalance(player.getName()))));
        playermeta.setOwningPlayer(player);
        phead.setItemMeta(playermeta);
        inv.setItem(4, phead);

        ItemStack closeMenu = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = closeMenu.getItemMeta();
        closeMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Close Menu");
        closeMenu.setItemMeta(closeMeta);
        inv.setItem(49, closeMenu);

        ItemStack withdrawGalleons = new ItemStack(Material.GOLD_NUGGET);
        ItemMeta withdrawGalleonsMeta = withdrawGalleons.getItemMeta();
        withdrawGalleonsMeta.setDisplayName(ChatColor.RED + "Withdraw Galleons");
        withdrawGalleons.setItemMeta(withdrawGalleonsMeta);
        inv.setItem(22, withdrawGalleons);

        ItemStack depositGalleons = new ItemStack(Material.IRON_NUGGET);
        ItemMeta depositGallMeta = depositGalleons.getItemMeta();
        depositGallMeta.setDisplayName(ChatColor.RED + "Deposit Galleons");
        depositGalleons.setItemMeta(depositGallMeta);
        inv.setItem(31, depositGalleons);

        ItemStack glassBorder = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glassBorder.getItemMeta();
        glassMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "---");
        glassBorder.setItemMeta(glassMeta);

        inv.setItem(0, glassBorder);
        inv.setItem(1, glassBorder);
        inv.setItem(2, glassBorder);
        inv.setItem(3, glassBorder);
        inv.setItem(5, glassBorder);
        inv.setItem(6, glassBorder);
        inv.setItem(7, glassBorder);
        inv.setItem(8, glassBorder);
        inv.setItem(45, glassBorder);
        inv.setItem(46, glassBorder);
        inv.setItem(47, glassBorder);
        inv.setItem(48, glassBorder);
        inv.setItem(50, glassBorder);
        inv.setItem(51, glassBorder);
        inv.setItem(52, glassBorder);
        inv.setItem(53, glassBorder);


        player.openInventory(inv);
    }
}
