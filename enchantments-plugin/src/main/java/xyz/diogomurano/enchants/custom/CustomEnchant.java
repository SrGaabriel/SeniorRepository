package xyz.diogomurano.enchants.custom;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public interface CustomEnchant extends Listener {

    UUID getUniqueId();

    String getName();

    Integer getMaxLevel();

    int getBlockSet();

    int getEnchantmentLevel(ItemStack stack);

    int getCountToUpgrade();

    List<String> getLore();

    boolean hasEnchantment(ItemStack stack);

    void addEnchantment(ItemStack stack, Integer level);

    void maxEnchantment(ItemStack stack);

    void removeEnchantment(ItemStack stack);

    int upgradeEnchantment(ItemStack stack, int amount);

    void reload();

    void run(Player player, Block block, int level);

}
