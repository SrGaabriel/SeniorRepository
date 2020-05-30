package com.gabriel.senior.project.listeners;

import com.gabriel.senior.project.prototypes.Account;
import com.gabriel.senior.project.repositories.impl.AccountRepository;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class BackpackListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        final Account account = AccountRepository.getInstance().retrieve(event.getPlayer().getUniqueId());

        if (event.getInventory() == account.getBackpack().getInventory()) {
            account.getBackpack().getInventory().setContents(event.getInventory().getContents());
        }
    }

}
