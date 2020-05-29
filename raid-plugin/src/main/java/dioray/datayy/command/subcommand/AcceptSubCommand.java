package dioray.datayy.command.subcommand;

import dioray.datayy.RaidPlugin;
import dioray.datayy.database.TeamPlayerDao;
import dioray.datayy.model.Role;
import dioray.datayy.model.Team;
import dioray.datayy.model.TeamPlayer;
import dioray.datayy.service.InviteService;
import org.bukkit.entity.Player;

public final class AcceptSubCommand extends SubCommand {

    private final InviteService inviteService;
    private final TeamPlayerDao teamPlayerDao;

    public AcceptSubCommand(RaidPlugin main) {
        super(main, "accept", "Accept a team invite");

        this.inviteService = main.getService(InviteService.class);
        this.teamPlayerDao = main.getTeamPlayerDao();
    }

    @Override
    public void run(final Player player, final String[] args) {
        final Team inviteTeam = inviteService.getInvite(player);
        if (inviteTeam == null) {
            messageService.sendMessage(player, "command.raid.accept.no-invite");
            return;
        }

        if (inviteTeam.getOnlinePlayers().size() >= 5) {
            messageService.sendMessage(player, "command.raid.accept.max-limit");

            return;
        }


        final TeamPlayer teamPlayer = teamPlayerService.getTeamPlayerByPlayer(player);
        teamPlayer.setRole(Role.MEMBER);
        teamPlayer.setTeam(inviteTeam);
        teamPlayer.resetCounter();

        teamPlayerDao.update(teamPlayer);

        inviteTeam.addPlayer(teamPlayer);

        messageService.sendMessage(player, "command.raid.accept.accepted");
        for (TeamPlayer oTeamPlayer : inviteTeam.getOnlinePlayers()) {
            messageService.sendMessage(oTeamPlayer.getPlayer(), "command.raid.accept.team-accepted", "player", player.getName());
        }
    }
}
