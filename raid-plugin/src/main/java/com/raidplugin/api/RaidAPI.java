package com.raidplugin.api;

import com.raidplugin.api.manager.Manager;
import com.raidplugin.api.prototype.Team;
import com.raidplugin.api.prototype.member.Member;
import com.raidplugin.api.prototype.raid.Raid;
import com.raidplugin.api.prototype.wall.Wall;
import com.raidplugin.api.repository.Repository;

import java.util.UUID;

public interface RaidAPI {

    Manager<Integer[], Wall> getWalls();

    Manager<Team, Raid> getRaids();

    Repository<String, Team> getTeams();

    Repository<UUID, Member> getMembers();

}
