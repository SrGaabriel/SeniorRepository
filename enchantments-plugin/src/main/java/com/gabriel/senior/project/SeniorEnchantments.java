package com.gabriel.senior.project;

import com.gabriel.senior.project.utils.RegistrySession;
import me.saiintbrisson.commands.CommandFrame;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

public class SeniorEnchantments extends JavaPlugin {

    private CommandFrame frame;

    @Override
    public void onEnable() {
        frame = new CommandFrame(this);

        configureFrame();
        new RegistrySession();
    }

    @Override
    public void onDisable() {
    }

    public static SeniorEnchantments getInstance() {
        return SeniorEnchantments.getPlugin(SeniorEnchantments.class);
    }

    public CommandFrame getFrame() {
        return frame;
    }

    @SuppressWarnings("deprecation")
    private void configureFrame() {
        frame.setLackPermMessage("§cYou do not have access to this command.");
        frame.setInGameOnlyMessage("§cThis command is only available to in-game players.");
        frame.setUsageMessage("§cCorrect use: §e/{usage}§c.");

        frame.registerType(OfflinePlayer.class, Bukkit::getOfflinePlayer);
    }

}
