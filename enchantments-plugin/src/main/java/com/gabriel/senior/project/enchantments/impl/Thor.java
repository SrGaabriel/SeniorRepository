package com.gabriel.senior.project.enchantments.impl;

import com.gabriel.senior.project.enchantments.AbstractEnchantment;
import org.bukkit.Material;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class Thor extends AbstractEnchantment {

    public Thor() {
        super(5001, PlayerInteractEvent.class);
    }

    @Override
    public void run(Player player, ItemStack tool) {
        if (tool.getType() != Material.WOOD_AXE) {
            return;
        }
        final int level = tool.getEnchantmentLevel(this);
        for (int i = 0; i < level / (level * 0.5); i++) {
            player.getWorld().strikeLightning(player.getTargetBlock(null, 3).getLocation());
        }
    }

    @Override
    public UUID getUniqueId() {
        return UUID.fromString("98297b75-dafc-4db0-b5e4-a67b25810bf2");
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.TOOL;
    }
}
