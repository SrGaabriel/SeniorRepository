package com.raidplugin.sdk.event.member;

import com.raidplugin.api.prototype.Team;
import com.raidplugin.api.prototype.member.Member;
import com.raidplugin.sdk.event.wrapper.EventWrapper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * This an event to call on member leave in team.
 * You can cancel the event with setCancelled.
 */

public class MemberQuitTeamEvent extends EventWrapper {

    private final Member member;
    private final Team team;

    public MemberQuitTeamEvent(Member member, Team team) {
        this.member = member; this.team = team;

        Bukkit.getPluginManager().callEvent(this);
    }

    public Member getMember() {
        return member;
    }

    public Player getPlayer() {
        return member.getPlayer();
    }

    public Team getTeam() {
        return team;
    }
}
