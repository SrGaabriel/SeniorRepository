package dioray.datayy.event;

import com.intellectualcrafters.plot.object.Plot;
import dioray.datayy.event.wrapper.EventWrapper;
import dioray.datayy.prototype.Team;
import org.bukkit.Bukkit;

public class TeamStartedRaidEvent extends EventWrapper {

    private final Team victim, attacker;
    private final Plot plot;

    public TeamStartedRaidEvent(Team victim, Team attacker, Plot plot) {
        this.victim = victim; this.attacker = attacker; this.plot = plot;

        Bukkit.getPluginManager().callEvent(this);
    }

    public Plot getPlot() {
        return plot;
    }

    public Team getAttacker() {
        return attacker;
    }

    public Team getVictim() {
        return victim;
    }
}
