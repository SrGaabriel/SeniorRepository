package com.gabriel.senior.project.listeners;

import com.gabriel.senior.project.prototypes.Account;
import com.gabriel.senior.project.repositories.impl.AccountRepository;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class BackpackListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryClickEvent event) {
        final Account account = AccountRepository.getInstance().retrieve(event.getWhoClicked().getUniqueId());

        if (event.getInventory() != account.getBackpack().getInventory()) {
            return;
        }
        if (event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().getDisplayName().equals("§b§lMINING BACKPACK")){
            event.setCancelled(true);
            // Do something
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        final Account account = AccountRepository.getInstance().retrieve(event.getPlayer().getUniqueId());

        if (event.getInventory() == account.getBackpack().getInventory()) {
            account.getBackpack().getInventory().setContents(event.getInventory().getContents());
        }
    }

}
