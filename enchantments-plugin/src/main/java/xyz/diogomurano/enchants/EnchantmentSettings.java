package xyz.diogomurano.enchants;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.function.Consumer;

public interface EnchantmentSettings {

    void createFiles();

    void loadFiles();

    FileConfiguration getEnchantsConfiguration();

    EnchantmentSettings with(Consumer<EnchantmentSettings> consumer);

}
