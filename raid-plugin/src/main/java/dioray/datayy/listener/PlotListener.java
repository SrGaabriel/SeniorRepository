package dioray.datayy.listener;

import com.plotsquared.bukkit.events.PlayerClaimPlotEvent;
import dioray.datayy.RaidPlugin;
import dioray.datayy.prototype.player.TeamPlayer;
import dioray.datayy.service.MessageService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlotListener implements Listener {

    private final RaidPlugin main;

    private final MessageService messageService;
    private final TeamPlayerService teamPlayerService;

    public PlotListener(RaidPlugin main) {
        this.main = main;

        this.messageService = main.getService(MessageService.class);
        this.teamPlayerService = main.getService(TeamPlayerService.class);
    }

    @EventHandler
    public void onPlayerClaimPlot(PlayerClaimPlotEvent e) {
        if (this.main.isRaidPlot(e.getPlot())) {
            TeamPlayer teamPlayer = teamPlayerService.getTeamPlayerByPlayer(e.getPlayer());
            Team team = teamPlayer.getTeam();

            if (team == null) {
                messageService.sendMessage(e.getPlayer(), "plot.cannot-claim");
                e.setCancelled(true);
            }
        }
    }
}
