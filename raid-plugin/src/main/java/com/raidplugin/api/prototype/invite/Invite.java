package com.raidplugin.api.prototype.invite;

import com.raidplugin.api.prototype.Team;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface Invite {

    Team getTarget();

    Player getPlayer();

    UUID getUUID();

    long getTime();

}
