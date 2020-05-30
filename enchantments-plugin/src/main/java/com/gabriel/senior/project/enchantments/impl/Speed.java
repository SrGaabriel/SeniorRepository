package com.gabriel.senior.project.enchantments.impl;

import com.gabriel.senior.project.enchantments.AbstractEnchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class Speed extends AbstractEnchantment {

    public Speed() {
        super(5001, PlayerMoveEvent.class);
    }

    @Override
    public void run(Player player, ItemStack tool) {
        if (!tool.getItemMeta().hasEnchant(this)) {
            return;
        }
        player.setWalkSpeed(tool.getEnchantmentLevel(this) * 0.02F);
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ARMOR_FEET;
    }

    @Override
    public UUID getUniqueId() {
        return UUID.fromString("58959b06-178a-4abd-a86d-30f52924306c");
    }
}
