package com.raidplugin.sdk.prototype.cooldown;

import com.raidplugin.api.prototype.Team;
import com.raidplugin.api.prototype.cooldown.Cooldown;
import com.raidplugin.api.prototype.cooldown.type.Reason;

public class WCooldown implements Cooldown {

    private final Reason reason;
    private final long time;
    private final Team victim, attacker;

    public WCooldown(Team victim, Team attacker, Reason reason, long time) {
        this.reason = reason; this.time = time; this.attacker = attacker; this.victim = victim;
    }

    @Override
    public Reason getReson() {
        return reason;
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
