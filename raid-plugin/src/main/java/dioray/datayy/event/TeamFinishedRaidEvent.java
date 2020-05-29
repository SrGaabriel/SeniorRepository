package dioray.datayy.event;

import com.intellectualcrafters.plot.object.Plot;
import dioray.datayy.event.wrapper.EventWrapper;
import dioray.datayy.prototype.Team;

public class TeamFinishedRaidEvent extends EventWrapper {

    private final Team victim, attacker;
    private final Plot plot;

    public TeamFinishedRaidEvent(Team victim, Team attacker) {
        this.victim = victim; this.attacker = attacker; plot = victim.getPlot();
    }

}
