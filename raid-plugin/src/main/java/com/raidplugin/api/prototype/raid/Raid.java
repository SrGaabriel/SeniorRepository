package com.raidplugin.api.prototype.raid;

import com.raidplugin.api.prototype.Team;

/**
 * That's a raid. Raid are attacks.
 * You can't change the values, but in raid's events yes.
 */

public interface Raid {

    Team getVictim();

    Team getAttacker();

    long getTime();



}
