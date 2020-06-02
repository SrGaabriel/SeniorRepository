package com.raidplugin.sdk.event.team;

import com.raidplugin.api.prototype.Team;
import com.raidplugin.api.prototype.member.Member;
import com.raidplugin.sdk.event.wrapper.EventWrapper;
import org.bukkit.Bukkit;

/**
 * This is an event to call on member was kicked.
 * You can cancel this on setCancelled.
 */

public class TeamKickMemberEvent extends EventWrapper {

    private final Team team;
    private final Member member;

    public TeamKickMemberEvent(Team team, Member member) {
        this.team = team; this.member = member;

        Bukkit.getPluginManager().callEvent(this);
    }

    public Team getTeam() {
        return team;
    }

    public Member getMember() {
        return member;
    }
}
