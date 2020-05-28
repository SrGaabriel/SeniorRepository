package xyz.diogomurano.enchants.bukkit;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import xyz.diogomurano.enchants.EnchantmentSettings;

import java.io.File;
import java.util.function.Consumer;

public class BukkitEnchantmentSettings implements EnchantmentSettings {

    private BukkitEnchantmentPlugin plugin;
    private FileConfiguration enchantsConfiguration;

    public BukkitEnchantmentSettings(BukkitEnchantmentPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void createFiles() {
        plugin.saveResource("enchants.yml", false);
    }

    @Override
    public void loadFiles() {
        this.enchantsConfiguration = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "enchants.yml"));
    }

    @Override
    public FileConfiguration getEnchantsConfiguration() {
        return enchantsConfiguration;
    }

    @Override
    public EnchantmentSettings with(Consumer<EnchantmentSettings> consumer) {
        consumer.accept(this);
        return this;
    }

}
