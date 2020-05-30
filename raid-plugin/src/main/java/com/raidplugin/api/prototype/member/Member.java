package com.raidplugin.api.prototype.member;

import com.raidplugin.api.prototype.Team;
import com.raidplugin.api.prototype.type.Role;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface Member {

    Team getTeam();

    UUID getUUID();

    Role getRole();

    Player getPlayer();

    void setRole(Role role);

}
