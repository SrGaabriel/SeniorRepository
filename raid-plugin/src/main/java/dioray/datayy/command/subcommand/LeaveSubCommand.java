package dioray.datayy.command.subcommand;

import dioray.datayy.RaidPlugin;
import dioray.datayy.database.PlotWallDao;
import dioray.datayy.database.TeamDao;
import dioray.datayy.database.TeamPlayerDao;
import dioray.datayy.model.Role;
import dioray.datayy.model.Team;
import dioray.datayy.model.TeamPlayer;
import dioray.datayy.service.MessageService;
import dioray.datayy.service.TeamPlayerService;
import dioray.datayy.service.TeamService;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LeaveSubCommand extends SubCommand {

    private final TeamPlayerDao teamPlayerDao;
    private final PlotWallDao plotWallDao;

    private final List<UUID> confirmedList;

    public LeaveSubCommand(RaidPlugin main) {
        super(main, "leave", "Leave from your current team");

        this.confirmedList = new ArrayList<>();

        this.teamPlayerDao = main.getTeamPlayerDao();
        this.plotWallDao = main.getPlotWallDao();
    }

    @Override
    public void run(Player player, String[] args) {
        TeamPlayer teamPlayer = teamPlayerService.getTeamPlayerByPlayer(player);
        Team team = teamPlayer.getTeam();
        if (team == null) {
            messageService.sendMessage(player, "command.raid.leave.no-team");

            return;
        }

        if (this.confirmedList.contains(player.getUniqueId())) {
            this.confirmedList.remove(player.getUniqueId());

            if (args.length < 1) return;
            String response = args[0];

            if (response.equalsIgnoreCase("yes")) {
                messageService.sendMessage(player, "command.raid.leave.success");

                if (teamPlayer.getRole() == Role.OWNER) {
                    TeamService teamService = this.main.getService(TeamService.class);

                    for (TeamPlayer oTeamPlayer : team.getOnlinePlayers()) {
                        oTeamPlayer.setRole(Role.MEMBER);
                        oTeamPlayer.setTeam(null);
                        this.main.getTeamPlayerDao().update(oTeamPlayer);

                        messageService.sendMessage(oTeamPlayer.getPlayer(), "command.raid.leave.team-removed");
                    }

                    plotWallDao.deleteAll(team.getPlot());

                    team.getPlot().deletePlot(() -> {
                        this.main.getLogger().info(team.getTag() + " had it plot deleted due to owner leave");
                    });

                    TeamDao teamDao = this.main.getTeamDao();

                    teamService.removeTeam(team);
                    teamDao.delete(team);
                } else {
                    teamPlayer.setRole(Role.MEMBER);
                    teamPlayer.setTeam(null);
                    teamPlayerDao.update(teamPlayer);

                    team.removePlayer(teamPlayer);

                    for (TeamPlayer oTeamPlayer : team.getOnlinePlayers()) {
                        messageService.sendMessage(oTeamPlayer.getPlayer(), "command.raid.leave.leaved", "player", player.getName());
                    }
                }
            }
        } else {
            if (args.length > 0) return;
            this.confirmedList.add(player.getUniqueId());

            messageService.sendMessage(player, "command.raid.leave.confirm");
            TextComponent component = new TextComponent();
            TextComponent yes = new TextComponent(messageService.get("command.raid.leave.yes"));
            yes.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/raid leave yes"));
            yes.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[] {
                    new TextComponent(messageService.get("command.raid.leave.yes"))
            }));

            TextComponent no = new TextComponent(messageService.get("command.raid.leave.no"));
            no.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/raid leave no"));
            no.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[] {
                    new TextComponent(messageService.get("command.raid.leave.no"))
            }));

            component.addExtra(yes);
            component.addExtra(" ");
            component.addExtra(no);

            player.spigot().sendMessage(component);
        }
    }
}
