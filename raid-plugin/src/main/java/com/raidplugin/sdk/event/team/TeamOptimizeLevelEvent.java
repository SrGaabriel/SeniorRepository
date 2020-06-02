package com.raidplugin.sdk.event.team;

import com.raidplugin.api.prototype.Team;
import com.raidplugin.sdk.event.wrapper.EventWrapper;
import org.bukkit.Bukkit;

/**
 * This is an event to call in upgrade of team.
 * You can change power, level and event!
 * To cancel, use setCancelled.
 */

public class TeamOptimizeLevelEvent extends EventWrapper {

    private final Team team;
    private int power;
    private int level;

    public TeamOptimizeLevelEvent(Team team, int power, int level) {
        this.team = team; this.power = power; this.level = level;

        Bukkit.getPluginManager().callEvent(this);
    }

    public Team getTeam() {
        return team;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getPower() {
        return power;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
