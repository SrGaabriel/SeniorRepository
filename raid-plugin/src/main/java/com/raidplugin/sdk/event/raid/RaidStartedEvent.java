package com.raidplugin.sdk.event.raid;

import com.raidplugin.api.prototype.raid.Raid;
import com.raidplugin.sdk.event.wrapper.EventWrapper;
import org.bukkit.Bukkit;

/**
 * This is an event to call on raid was started.
 * You can cancel this on setCancelled.
 */

public class RaidStartedEvent extends EventWrapper {

    private final Raid raid;

    public RaidStartedEvent(Raid raid) {
        this.raid = raid;

        Bukkit.getPluginManager().callEvent(this);
    }

    public Raid getRaid() {
        return raid;
    }
}
