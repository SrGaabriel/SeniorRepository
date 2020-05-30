package com.raidplugin;

import com.raidplugin.sdk.database.DatabaseProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class RaidPlugin extends JavaPlugin {

    public static RaidPlugin getInstance() {
        return getPlugin(RaidPlugin.class);
    }

    private DatabaseProvider databaseProvider;

    @Override
    public void onLoad() {
        databaseProvider = new DatabaseProvider();

        databaseProvider.openConnection();
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public DatabaseProvider getProvider() {
        return databaseProvider;
    }
}
