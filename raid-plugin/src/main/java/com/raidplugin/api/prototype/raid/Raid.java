package com.raidplugin.api.prototype.raid;

import com.raidplugin.api.prototype.Team;

public interface Raid {

    Team getVictim();

    Team getAttacker();

    long getTime();



}
