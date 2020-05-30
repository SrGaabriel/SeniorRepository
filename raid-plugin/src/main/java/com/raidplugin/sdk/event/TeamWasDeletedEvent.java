package com.raidplugin.sdk.event;

import com.raidplugin.api.prototype.Team;
import com.raidplugin.sdk.event.wrapper.EventWrapper;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class TeamWasDeletedEvent extends EventWrapper {

    private final Team team;
    private final CommandSender author;

    public TeamWasDeletedEvent(Team team, CommandSender author) {
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
