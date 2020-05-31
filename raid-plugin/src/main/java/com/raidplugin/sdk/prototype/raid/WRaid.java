package com.raidplugin.sdk.prototype.raid;

import com.raidplugin.api.prototype.Team;
import com.raidplugin.api.prototype.raid.Raid;

public class WRaid implements Raid {

    private final Team victim, attacker;
    private final long time;

    public WRaid(Team victim, Team attacker, long time) {
        this.victim = victim; this.attacker = attacker; this.time = time;
    }

    @Override
    public Team getVictim() {
        return victim;
    }

    @Override
    public Team getAttacker() {
        return attacker;
    }

    @Override
    public long getTime() {
        return time;
    }
}
