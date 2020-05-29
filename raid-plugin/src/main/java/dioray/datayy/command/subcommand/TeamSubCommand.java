package dioray.datayy.command.subcommand;

import com.intellectualcrafters.plot.object.PlotArea;
import com.intellectualcrafters.plot.object.PlotPlayer;
import dioray.datayy.RaidPlugin;
import dioray.datayy.database.TeamDao;
import dioray.datayy.database.TeamPlayerDao;
import dioray.datayy.model.Team;
import dioray.datayy.model.TeamPlayer;
import dioray.datayy.service.TeamService;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class TeamSubCommand extends SubCommand {

    private final TeamService teamService;
    private final TeamPlayerDao teamPlayerDao;
    private final TeamDao teamDao;

    private final int createTeamPrice;

    public TeamSubCommand(RaidPlugin main) {
        super(main, "team", "Manage teams");

        this.createTeamPrice = main.getConfig().getInt("create-team-price");

        this.teamService = main.getService(TeamService.class);
        this.teamPlayerDao = main.getTeamPlayerDao();
        this.teamDao = main.getTeamDao();
    }

    @Override
    public void run(Player player, String[] args) {
        if (args.length < 1) {
            TeamPlayer teamPlayer = teamPlayerService.getTeamPlayerByPlayer(player);
            Team team = teamPlayer.getTeam();
            if (team == null) {
                messageService.sendMessage(player, "command.raid.team.info.no-team");
                messageService.sendMessage(player, "command.raid.team.info.usage");

                return;
            }

            messageService.sendMessage(player, "command.raid.team.info.1", "team", team.getTag());
            messageService.sendMessage(player, "command.raid.team.info.2", "online", team.getOnlinePlayers().size());
            return;
        }

        if (args[0].equalsIgnoreCase("create")) {
            handleCreate(player, Arrays.copyOfRange(args, 1, args.length));

            return;
        }

        Team team = teamService.getByTag(args[0]);

        if (team == null) {
            messageService.sendMessage(player, "command.raid.team.info.not-found", "team", args[0]);

            return;
        }

        messageService.sendMessage(player, "command.raid.team.info.1", "team", team.getTag());
        messageService.sendMessage(player, "command.raid.team.info.2", "online", team.getOnlinePlayers().size());
    }

    private void handleCreate(Player player, String[] args) {
        TeamPlayer teamPlayer = teamPlayerService.getTeamPlayerByPlayer(player);

        if (teamPlayer.getTeam() != null) {
            messageService.sendMessage(player, "command.raid.team.already-in");

            return;
        }

        if (args.length < 1) {
            messageService.sendMessage(player, "command.raid.team.usage");

            return;
        }

        if (!args[0].matches("\\w+") || args[0].length() > 10 || args[0].equalsIgnoreCase("create")) {
            messageService.sendMessage(player, "command.raid.team.invalid-name");

            return;
        }

        if (teamService.getByTag(args[0]) != null) {
            messageService.sendMessage(player, "command.raid.team.already-exists");

            return;
        }

        EconomyResponse response = this.main.getEconomy().withdrawPlayer(player, this.createTeamPrice);
        if (!response.transactionSuccess()) {
            messageService.sendMessage(player, "command.raid.team.not-enough", "amount", this.createTeamPrice);

            return;
        }

        PlotArea plotArea = this.main.getPlotArea();
        PlotPlayer plotPlayer = PlotPlayer.wrap(player);

        /* TODO Plot freePlot = plotArea.getNextFreePlot(plotPlayer, new PlotId(0, 0));
        plotArea.getPlotManager().claimPlot(plotArea, freePlot);
        freePlot.setOwner(player.getUniqueId());

        SchematicHandler.Schematic schem = SchematicHandler.manager.getSchematic("plots");
        SchematicHandler.manager.paste(schem, freePlot, 0, -1, 0, true, null);

        player.teleport(Util.fromPlotLocation(freePlot.getSide()));

        Team team = teamService.create(args[0], freePlot);
        teamDao.insert(team);

        teamPlayer.setRole(Role.OWNER);
        teamPlayer.setTeam(team);
        teamPlayer.resetCounter();
        teamPlayerDao.update(teamPlayer);

        team.addPlayer(teamPlayer);
        messageService.sendMessage(player, "command.raid.team.success");*/
    }

}
