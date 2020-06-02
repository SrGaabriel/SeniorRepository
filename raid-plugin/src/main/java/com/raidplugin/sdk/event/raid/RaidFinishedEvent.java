package com.raidplugin.sdk.event.raid;

import com.raidplugin.api.prototype.raid.Raid;
import com.raidplugin.sdk.event.wrapper.EventWrapper;
import org.bukkit.Bukkit;

public class RaidFinishedEvent extends EventWrapper {

    private final Raid raid;

    public RaidFinishedEvent(Raid raid) {
        this.raid = raid;

        Bukkit.getPluginManager().callEvent(this);
    }

    public Raid getRaid() {
        return raid;
    }
}
