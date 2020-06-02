package com.raidplugin.api.prototype.invite;

import com.raidplugin.api.prototype.Team;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * That's invite. Invite is invite.
 * But, you can't manage the values, only in events can.
 */

public interface Invite {

    Team getTarget();

    Player getPlayer();

    UUID getUUID();

    long getTime();

}
