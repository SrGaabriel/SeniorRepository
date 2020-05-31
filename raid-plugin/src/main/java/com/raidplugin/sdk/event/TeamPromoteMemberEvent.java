package com.raidplugin.sdk.event;

import com.raidplugin.api.prototype.Team;
import com.raidplugin.api.prototype.member.Member;
import com.raidplugin.sdk.event.wrapper.EventWrapper;
import org.bukkit.Bukkit;

public class TeamPromoteMemberEvent extends EventWrapper {

    private final Team team;
    private final Member member;

    public TeamPromoteMemberEvent(Team team, Member member) {
        this.team = team; this.member = member;

        Bukkit.getPluginManager().callEvent(this);
    }

    public Member getMember() {
        return member;
    }

    public Team getTeam() {
        return team;
    }
}
