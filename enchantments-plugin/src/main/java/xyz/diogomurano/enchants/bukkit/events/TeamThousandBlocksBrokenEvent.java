package xyz.diogomurano.enchants.bukkit.events;

import dioray.datayy.prototype.Team;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class TeamThousandBlocksBrokenEvent extends Event {

    private final Team team;
    private final HandlerList handlers = new HandlerList();

    public TeamThousandBlocksBrokenEvent(Team team) {
        this.team = team;
    }

    public final Team getTeam() {
        return team;
    }

    @Override
    public final HandlerList getHandlers() {
        return handlers;
    }
}
