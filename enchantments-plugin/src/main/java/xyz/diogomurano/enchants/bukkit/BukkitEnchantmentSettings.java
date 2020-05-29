package xyz.diogomurano.enchants.bukkit;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import xyz.diogomurano.enchants.EnchantmentSettings;

import java.io.File;
import java.util.function.Consumer;

public class BukkitEnchantmentSettings implements EnchantmentSettings {

    private final BukkitEnchantmentPlugin plugin;
    @Getter
    private FileConfiguration enchantsConfiguration;

    public BukkitEnchantmentSettings(BukkitEnchantmentPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final void createFiles() {
        plugin.saveResource("enchants.yml", false);
    }

    @Override
    public void loadFiles() {
        this.enchantsConfiguration = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "enchants.yml"));
    }

    @Override
    public final EnchantmentSettings with(Consumer<EnchantmentSettings> consumer) {
        consumer.accept(this);
        return this;
    }

}
