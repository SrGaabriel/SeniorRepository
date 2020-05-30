package com.gabriel.senior.project;

import com.gabriel.senior.project.utils.RegistrySession;
import org.bukkit.plugin.java.JavaPlugin;

public class SeniorEnchantments extends JavaPlugin {

    @Override
    public void onEnable() {
        new RegistrySession();
    }

    @Override
    public void onDisable() {
    }

    public static SeniorEnchantments getInstance() {
        return SeniorEnchantments.getPlugin(SeniorEnchantments.class);
    }
}
