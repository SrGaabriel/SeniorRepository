package com.raidplugin.api.prototype.member;

import com.raidplugin.api.prototype.Team;
import com.raidplugin.api.prototype.member.type.Role;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * That's a member. Member is a team's player.
 * You can manage the values.
 */

public interface Member {

    Team getTeam();

    String getName();

    UUID getUUID();

    Role getRole();

    Player getPlayer();

    void setRole(Role role);

}
