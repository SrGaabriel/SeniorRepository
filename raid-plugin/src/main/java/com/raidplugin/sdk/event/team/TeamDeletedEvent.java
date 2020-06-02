package com.raidplugin.sdk.event.team;

import com.raidplugin.api.prototype.Team;
import com.raidplugin.sdk.event.wrapper.EventWrapper;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class TeamDeletedEvent extends EventWrapper {

    private final Team team;
    private final CommandSender author;

    public TeamDeletedEvent(Team team, CommandSender author) {
        this.team = team; this.author = author;

        Bukkit.getPluginManager().callEvent(this);
    }

    public Team getTeam() {
        return team;
    }

    public CommandSender getAuthor() {
        return author;
    }
}
