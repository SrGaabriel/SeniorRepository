package dioray.datayy.command.subcommand;

import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotArea;
import dioray.datayy.RaidPlugin;
import dioray.datayy.model.Team;
import dioray.datayy.model.TeamPlayer;
import dioray.datayy.service.MessageService;
import dioray.datayy.service.TeamPlayerService;
import dioray.datayy.service.TeamService;
import dioray.datayy.util.Util;
import org.bukkit.entity.Player;

public class SeeSubCommand extends SubCommand {

    private final TeamService teamService;

    public SeeSubCommand(RaidPlugin main) {
        super(main, "see", "See your plot being raided");

        this.teamService = main.getService(TeamService.class);
    }

    @Override
    public void run(Player player, String[] args) {
        TeamPlayer teamPlayer = teamPlayerService.getTeamPlayerByPlayer(player);
        Team team = teamPlayer.getTeam();
        if (team == null) {
            messageService.sendMessage(player, "command.raid.see.no-team");

            return;
        }

        Plot plot = team.getPlot();

        Team raidingTeam = teamService.getRaidingTeam(plot);

        if (raidingTeam == null) {
            messageService.sendMessage(player, "command.raid.see.not-being-raided");

            return;
        }

        for (TeamPlayer oTeamPlayer : raidingTeam.getOnlinePlayers()) {
            oTeamPlayer.getPlayer().hidePlayer(this.main, player);
        }

        player.teleport(Util.fromPlotLocation(plot.getSide()));
    }

}
