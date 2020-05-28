package dioray.datayy.command.subcommand;

import dioray.datayy.RaidPlugin;
import dioray.datayy.database.TeamPlayerDao;
import dioray.datayy.model.Role;
import dioray.datayy.model.Team;
import dioray.datayy.model.TeamPlayer;
import dioray.datayy.service.MessageService;
import dioray.datayy.service.TeamPlayerService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class KickSubCommand extends SubCommand {

    private final TeamPlayerDao teamPlayerDao;

    public KickSubCommand(RaidPlugin main) {
        super(main, "kick", "Kick a player from a team");

        this.teamPlayerDao = main.getTeamPlayerDao();
    }

    @Override
    public void run(Player player, String[] args) {
        TeamPlayer teamPlayer = teamPlayerService.getTeamPlayerByPlayer(player);
        Team team = teamPlayer.getTeam();

        if (team == null) {
            messageService.sendMessage(player, "command.raid.kick.no-team");

            return;
        }

        if (teamPlayer.getRole() == Role.MEMBER) {
            messageService.sendMessage(player, "command.raid.kick.no-permission");

            return;
        }

        if (args.length < 1) {
            messageService.sendMessage(player, "command.raid.kick.usage");

            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            messageService.sendMessage(player, "command.raid.kick.offline");

            return;
        }

        if (target.getUniqueId().equals(player.getUniqueId())) {
            messageService.sendMessage(player, "command.raid.kick.yourself");

            return;
        }

        TeamPlayer targetTeamPlayer = teamPlayerService.getTeamPlayerByPlayer(target);
        if (targetTeamPlayer.getTeam() == null || !targetTeamPlayer.getTeam().equals(team)) {
            messageService.sendMessage(player, "command.raid.kick.error", "player", target.getName());

            return;
        }

        targetTeamPlayer.setRole(Role.MEMBER);
        targetTeamPlayer.setTeam(null);
        teamPlayerDao.update(targetTeamPlayer);

        team.removePlayer(targetTeamPlayer);

        messageService.sendMessage(player, "command.raid.kick.success", "player", target.getName());
        messageService.sendMessage(target, "command.raid.kick.kicked", "player", player.getName());
    }
}