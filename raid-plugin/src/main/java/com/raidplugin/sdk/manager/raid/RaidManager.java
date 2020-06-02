package com.raidplugin.sdk.manager.raid;

import com.raidplugin.api.manager.Manager;
import com.raidplugin.api.prototype.Team;
import com.raidplugin.api.prototype.raid.Raid;

import java.util.LinkedList;
import java.util.List;

public class RaidManager implements Manager<Team, Raid> {

    /**
     * That's a manager to change/add raids.
     * You can get raids with:
     *  Victim team;
     *  Attacker team.
     */

    private static RaidManager raidManager;

    public static RaidManager getInstance() {
        return raidManager == null ? (raidManager = new RaidManager()) : raidManager;
    }

    private final List<Raid> raidList = new LinkedList<>();

    @Override
    public List<Raid> getCollection() {
        return raidList;
    }

    @Override
    public void put(Raid value) {
        raidList.add(value);
    }

    @Override
    public void putAll(List<Raid> collection) {
        raidList.addAll(collection);
    }

    @Override
    public void remove(Raid value) {
        raidList.remove(value);
    }

    @Override
    public void removeAll(List<Raid> collection) {
        raidList.removeAll(collection);
    }

    @Override
    public Raid get(Team key) {
        for (Raid raid : raidList) {
            if(isSameTeam(raid.getAttacker(), key) || isSameTeam(raid.getVictim(), key)) return raid;
        } return null;
    }

    public Team get(Raid raid, Team team) {
        return isSameTeam(raid.getVictim(), team) ? raid.getVictim() : raid.getAttacker();
    }

    public boolean isSameTeam(Team target, Team team) {
        return target.getName().compareToIgnoreCase(team.getName()) == 0;
    }
}
