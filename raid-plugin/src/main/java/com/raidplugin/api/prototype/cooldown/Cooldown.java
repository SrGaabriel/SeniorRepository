package com.raidplugin.api.prototype.cooldown;

import com.raidplugin.api.prototype.Team;
import com.raidplugin.api.prototype.cooldown.type.Reason;

public interface Cooldown {

    Reason getReson();

    Team getVictim();

    Team getAttacker();

    long getTime();

}
