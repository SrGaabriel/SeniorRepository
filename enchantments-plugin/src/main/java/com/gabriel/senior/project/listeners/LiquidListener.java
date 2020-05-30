package com.gabriel.senior.project.listeners;

import com.gabriel.senior.project.prototypes.Account;
import com.gabriel.senior.project.repositories.impl.AccountRepository;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class LiquidListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.SURVIVAL) {
            return;
        }
        final Account account = AccountRepository.getInstance().retrieve(event.getPlayer().getUniqueId());
        account.setLiquid(account.getLiquid()+1);
    }

}
