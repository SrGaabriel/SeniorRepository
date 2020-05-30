package com.raidplugin.sdk.provider;

import com.raidplugin.api.prototype.Team;
import com.raidplugin.sdk.provider.configuration.ConfigurationProvider;

public class TeamProvider {

    private final ConfigurationProvider configuration = ConfigurationProvider.getInstance();

    public boolean isPossible(Team team) {
        return team.getPower() >= getNeed(team);
    }

    public int getNeed(Team team) {
        return configuration.get(Integer.class, "team-multiplicate-power") * team.getLevel();
    }

}
