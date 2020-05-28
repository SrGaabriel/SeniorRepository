package dioray.datayy.command.subcommand;

import dioray.datayy.RaidPlugin;
import dioray.datayy.model.Team;
import dioray.datayy.model.TeamPlayer;
import dioray.datayy.service.InviteService;
import dioray.datayy.service.MessageService;
import org.bukkit.entity.Player;

public class DenySubCommand extends SubCommand {

    private final InviteService inviteService;

    public DenySubCommand(RaidPlugin main) {
        super(main, "deny", "Deny a team invite");

        this.inviteService = main.getService(InviteService.class);
    }

    @Override
    public void run(Player player, String[] args) {
        Team inviteTeam = inviteService.getInvite(player);
        if (inviteTeam == null) {
            messageService.sendMessage(player, "command.raid.deny.no-invite");

            return;
        }

        messageService.sendMessage(player, "command.raid.deny.denied");
        for (TeamPlayer oTeamPlayer : inviteTeam.getOnlinePlayers()) {
            messageService.sendMessage(oTeamPlayer.getPlayer(), "command.raid.accept.team-denied", "player", player.getName());
        }
    }
}
