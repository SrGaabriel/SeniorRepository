package com.raidplugin.sdk.repository;

import com.google.common.collect.Maps;
import com.intellectualcrafters.plot.object.Plot;
import com.raidplugin.api.prototype.Team;
import com.raidplugin.api.prototype.member.Member;
import com.raidplugin.api.repository.Repository;
import org.bukkit.entity.Player;

import java.util.Map;

public class TeamRepository implements Repository<String, Team> {

    private static TeamRepository teamRepository;

    public static TeamRepository getInstance() {
        return teamRepository == null ? (teamRepository = new TeamRepository()) : teamRepository;
    }

    private final Map<String, Team> teamMap = Maps.newConcurrentMap();

    @Override
    public Map<String, Team> getMap() {
        return teamMap;
    }

    @Override
    public void put(String key, Team value) {
        teamMap.put(key, value);
    }

    @Override
    public void remove(String key, Team value) {
        teamMap.remove(key, value);
    }

    @Override
    public Team get(String key) {
        return teamMap.get(key);
    }

    public Team get(Plot plot) {
        for(Team team : teamMap.values()) {
            if(isSamePlot(team, plot)) return team;
        } return null;
    }

    public boolean isSamePlot(Team team, Plot plot) {
        return team.getPlot().guessOwner().compareTo(plot.guessOwner()) == 0;
    }

}
