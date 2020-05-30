package com.gabriel.senior.project.listeners;

import com.gabriel.senior.project.enchantments.AbstractEnchantment;
import com.gabriel.senior.project.prototypes.Account;
import com.gabriel.senior.project.repositories.impl.AccountRepository;
import com.gabriel.senior.project.repositories.impl.EnchantmentRepository;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class EnchantmentExecutorListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        runEnchantment(event.getPlayer(), event);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        AccountRepository.getInstance().reveal().putIfAbsent(event.getPlayer().getUniqueId(), new Account(event.getPlayer().getUniqueId()));
        runEnchantment(event.getPlayer(), event);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        runEnchantment(event.getPlayer(), event);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        runEnchantment(event.getPlayer(), event);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        runEnchantment(event.getPlayer(), event);
    }

    private void runEnchantment(Player player, Event event) {
        AccountRepository.getInstance().reveal().putIfAbsent(player.getUniqueId(), new Account(player.getUniqueId()));
        for (AbstractEnchantment enchantment : EnchantmentRepository.getInstance().reveal().values()) {
            if (event.getClass() != enchantment.getExpectancy()) {
                continue;
            }
            if (player.getInventory().getItemInMainHand() == null || !player.getInventory().getItemInMainHand().hasItemMeta()) {
                return;
            }
            if (!player.getInventory().getItemInMainHand().getItemMeta().hasEnchant(enchantment)) {
                continue;
            }
            enchantment.run(player, player.getInventory().getItemInMainHand());
        }
    }

}
