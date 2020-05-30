package com.gabriel.senior.project.enchantments;

import com.gabriel.senior.project.prototypes.Prototype;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public abstract class AbstractEnchantment extends Enchantment implements Prototype {

    private final Class<? extends Event> expectancy;

    public AbstractEnchantment(int id, Class<? extends Event> expectancy) {
        super(id);
        this.expectancy = expectancy;
    }

    public abstract void run(Player player, ItemStack tool);

    @Override
    public final String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public final int getStartLevel() {
        return 0;
    }

    @Override
    public final int getMaxLevel() {
        return 5000;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public final boolean isCursed() {
        return false;
    }

    @Override
    public final boolean conflictsWith(Enchantment other) {
        return false;
    }

    @Override
    public final boolean canEnchantItem(ItemStack item) {
        return false;
    }

    public Class<? extends Event> getExpectancy() {
        return expectancy;
    }
}
