package xyz.diogomurano.enchants.bukkit.item;

import org.bukkit.configuration.file.FileConfiguration;
import xyz.diogomurano.enchants.bukkit.BukkitEnchantmentPlugin;

public class Backpack {

    private static int maxLevel;
    private static int blockSet;

    public static void init() {
        FileConfiguration config = BukkitEnchantmentPlugin.getSettings().getEnchantsConfiguration();
        Backpack.maxLevel = config.getInt("enchants.Backpack.max-level");
        Backpack.blockSet = config.getInt("enchants.Backpack.block-set");
    }

    public static int getCountToUpgrade() {
        return Backpack.blockSet / Backpack.maxLevel;
    }

    public static int getBlockSet() {
        return blockSet;
    }

    public static int getMaxLevel() {
        return maxLevel;
    }
}