package com.gabriel.senior.project.listeners;

import com.gabriel.senior.project.enchantments.AbstractEnchantment;
import com.gabriel.senior.project.repositories.impl.EnchantmentRepository;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;

public class EnchantmentExecutorListener implements Listener {

    @EventHandler
    public void onPlayerEvent(PlayerEvent event) {
        for (AbstractEnchantment enchantment : EnchantmentRepository.getInstance().reveal().values()) {
            if (event.getClass() != enchantment.getExpectancy()) {
                continue;
            }
            enchantment.run(event.getPlayer(), event.getPlayer().getInventory().getItemInMainHand());
        }
    }

}
