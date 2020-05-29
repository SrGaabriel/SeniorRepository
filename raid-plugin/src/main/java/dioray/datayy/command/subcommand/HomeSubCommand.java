package dioray.datayy.command.subcommand;

import dioray.datayy.RaidPlugin;
import dioray.datayy.model.Team;
import dioray.datayy.model.TeamPlayer;
import dioray.datayy.util.Util;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class HomeSubCommand extends SubCommand {

    public HomeSubCommand(RaidPlugin main) {
        super(main, "home", "Go to your teams's home");
    }

    @Override
    public void run(Player player, String[] args) {
        TeamPlayer teamPlayer = teamPlayerService.getTeamPlayerByPlayer(player);
        Team team = teamPlayer.getTeam();

        if (team == null) {
            messageService.sendMessage(player, "command.raid.home.no-team");

            return;
        }

        Location side = Util.fromPlotLocation(team.getPlot().getSide());
        player.teleport(side);
    }

}
