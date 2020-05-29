package dioray.datayy.command.subcommand;

import dioray.datayy.RaidPlugin;
import dioray.datayy.prototype.player.type.Role;
import dioray.datayy.prototype.player.TeamPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PromoteSubCommand extends SubCommand {

    private final TeamPlayerDao teamPlayerDao;

    public PromoteSubCommand(RaidPlugin main) {
        super(main, "promote", "Promote a player of your team");

        this.teamPlayerDao = main.getTeamPlayerDao();
    }

    @Override
    public void run(Player player, String[] args) {
        TeamPlayer teamPlayer = teamPlayerService.getTeamPlayerByPlayer(player);
        Team team = teamPlayer.getTeam();

        if (team == null) {
            messageService.sendMessage(player, "command.raid.promote.no-team");

            return;
        }

        if (teamPlayer.getRole() != Role.OWNER) {
            messageService.sendMessage(player, "command.raid.promote.no-permission");

            return;
        }

        if (args.length < 1) {
            messageService.sendMessage(player, "command.raid.promote.usage");

            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            messageService.sendMessage(player, "command.raid.promote.offline", "player", args[0]);

            return;
        }

        if (target.getUniqueId().equals(player.getUniqueId())) {
            messageService.sendMessage(player, "command.raid.promote.yourself");

            return;
        }

        TeamPlayer targetTeamPlayer = teamPlayerService.getTeamPlayerByPlayer(target);
        if (targetTeamPlayer.getTeam() == null || !targetTeamPlayer.getTeam().equals(team)) {
            messageService.sendMessage(player, "command.raid.promote.error", "player", target.getName());

            return;
        }

        if (targetTeamPlayer.getRole() != Role.MEMBER) {
            messageService.sendMessage(player, "command.raid.promote.already-promoted", "player", target.getName());

            return;
        }

        targetTeamPlayer.setRole(Role.MOD);
        teamPlayerDao.update(targetTeamPlayer);

        messageService.sendMessage(player, "command.raid.promote.promoted", "player", target.getName());
        messageService.sendMessage(target, "command.raid.promote.being-promoted");
    }
}
