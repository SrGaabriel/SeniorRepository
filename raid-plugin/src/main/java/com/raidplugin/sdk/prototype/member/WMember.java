package com.raidplugin.sdk.prototype.member;

import com.raidplugin.api.prototype.Team;
import com.raidplugin.api.prototype.member.Member;
import com.raidplugin.api.prototype.type.Role;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class WMember implements Member {

    private final Team team;
    private final UUID uuid;
    private final String name;

    private Role role;

    public WMember(Player player, Team team, Role role) {
        this.team = team; this.uuid = player.getUniqueId(); this.role = role; this.name = player.getName();
    }

    @Override
    public Team getTeam() {
        return team;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public Role getRole() {
        return role;
    }

    @Override
    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    @Override
    public void setRole(Role role) {
        this.role = role;
    }
}
