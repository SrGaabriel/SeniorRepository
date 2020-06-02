package com.raidplugin.sdk;

import com.raidplugin.api.RaidAPI;
import com.raidplugin.api.manager.Manager;
import com.raidplugin.api.prototype.Team;
import com.raidplugin.api.prototype.invite.Invite;
import com.raidplugin.api.prototype.member.Member;
import com.raidplugin.api.prototype.raid.Raid;
import com.raidplugin.api.prototype.wall.Wall;
import com.raidplugin.api.repository.Repository;
import com.raidplugin.sdk.manager.WallManager;
import com.raidplugin.sdk.manager.invite.InviteManager;
import com.raidplugin.sdk.manager.raid.RaidManager;
import com.raidplugin.sdk.repository.TeamRepository;
import com.raidplugin.sdk.repository.member.MemberRepository;

import java.util.UUID;

public class WRaidAPI implements RaidAPI {

    @Override
    public Manager<Integer[], Wall> getWalls() {
        return WallManager.getInstance();
    }

    @Override
    public Manager<Team, Raid> getRaids() {
        return RaidManager.getInstance();
    }

    @Override
    public Repository<String, Team> getTeams() {
        return TeamRepository.getInstance();
    }

    @Override
    public Repository<UUID, Member> getMembers() {
        return MemberRepository.getInstance();
    }

    @Override
    public Manager<UUID, Invite> getInvites() {
        return InviteManager.getInstance();
    }
}
