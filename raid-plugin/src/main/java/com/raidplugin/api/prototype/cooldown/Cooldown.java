package com.raidplugin.api.prototype.cooldown;

import com.raidplugin.api.prototype.Team;
import com.raidplugin.api.prototype.cooldown.type.Reason;

/**
 * That's a cooldown. Cooldown is a time to player can't execute any action.
 * You cancel this in raid thread or raid's managers.
 */

public interface Cooldown {

    Reason getReson();

    Team getVictim();

    Team getAttacker();

    long getTime();

}
