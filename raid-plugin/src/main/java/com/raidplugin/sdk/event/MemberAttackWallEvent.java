package com.raidplugin.sdk.event;

import com.raidplugin.api.prototype.member.Member;
import com.raidplugin.api.prototype.wall.Wall;
import com.raidplugin.sdk.event.wrapper.EventWrapper;
import org.bukkit.Bukkit;

public class MemberAttackWallEvent extends EventWrapper {

    private final Member member;
    private int damage;
    private final Wall wall;

    public MemberAttackWallEvent(Member member, int damage, Wall wall) {
        this.member = member; this.damage = damage; this.wall = wall;

        Bukkit.getPluginManager().callEvent(this);
    }

    public Member getMember() {
        return member;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    public Wall getWall() {
        return wall;
    }
}
