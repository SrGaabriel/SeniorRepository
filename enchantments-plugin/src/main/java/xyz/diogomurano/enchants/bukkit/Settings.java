package xyz.diogomurano.enchants.bukkit;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Settings {

    public static List<String> AUTO_SELL_MESSAGE;

    public static void load(JavaPlugin plugin) {
        File autoSellFile = new File(plugin.getDataFolder(), "autosell.txt");
        if (!autoSellFile.exists()) {
            plugin.saveResource("autosell.txt", false);
        }

        try {
            Settings.AUTO_SELL_MESSAGE = Files
                    .readAllLines(Paths.get(autoSellFile.toURI()))
                    .stream()
                    .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                    .collect(Collectors.toList());

            Settings.AUTO_SELL_MESSAGE = Collections.unmodifiableList(Settings.AUTO_SELL_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}