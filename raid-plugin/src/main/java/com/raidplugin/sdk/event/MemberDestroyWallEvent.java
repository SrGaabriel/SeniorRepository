package com.raidplugin.sdk.event;

import com.raidplugin.api.prototype.member.Member;
import com.raidplugin.api.prototype.wall.Wall;
import com.raidplugin.sdk.event.wrapper.EventWrapper;
import org.bukkit.Bukkit;

public class MemberDestroyWallEvent extends EventWrapper {

    private final Member member;
    private final Wall wall;

    public MemberDestroyWallEvent(Member member, Wall wall) {
        this.member = member; this.wall = wall;

        Bukkit.getPluginManager().callEvent(this);
    }

    public Wall getWall() {
        return wall;
    }

    public Member getMember() {
        return member;
    }
}
