package com.raidplugin.sdk.event;

import com.raidplugin.api.prototype.Team;
import com.raidplugin.sdk.event.wrapper.EventWrapper;
import org.bukkit.Bukkit;

public class TeamIncreaseLevelEvent extends EventWrapper {

    private final Team team;
    private int power;
    private int level;

    public TeamIncreaseLevelEvent(Team team, int power, int level) {
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
