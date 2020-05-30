package com.raidplugin.sdk.event;

import com.raidplugin.api.prototype.Team;
import com.raidplugin.api.prototype.member.Member;
import com.raidplugin.sdk.event.wrapper.EventWrapper;
import org.bukkit.Bukkit;

public class MemberTeleportHomeEvent extends EventWrapper {

    private final Member member;
    private final Team team;

    public MemberTeleportHomeEvent(Member member, Team team) {
        this.member = member; this.team = team;

        Bukkit.getPluginManager().callEvent(this);
    }

    public Team getTeam() {
        return team;
    }

    public Member getMember() {
        return member;
    }
}
