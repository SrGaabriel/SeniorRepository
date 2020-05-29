package dioray.datayy.command.subcommand;

import dioray.datayy.RaidPlugin;
import dioray.datayy.prototype.Role;
import dioray.datayy.prototype.Team;
import dioray.datayy.prototype.TeamPlayer;
import dioray.datayy.service.RaidService;
import dioray.datayy.service.SearchCooldownService;
import org.bukkit.entity.Player;

public final class SearchSubCommand extends SubCommand {

    private final SearchCooldownService searchCooldownService;
    private final RaidService raidService;

    public SearchSubCommand(final RaidPlugin main) {
        super(main, "search", "Search for raids");

        this.searchCooldownService = main.getService(SearchCooldownService.class);
        this.raidService = main.getService(RaidService.class);
    }

    @Override
    public final void run(final Player player, final String[] args) {
        final TeamPlayer teamPlayer = teamPlayerService.getTeamPlayerByPlayer(player);

        final Team team = teamPlayer.getTeam();
        if (team == null) {
            messageService.sendMessage(player, "command.raid.search.no-team");

            return;
        }

        if (teamPlayer.getRole() == Role.MEMBER) {
            messageService.sendMessage(player, "command.raid.search.no-permission");

            return;
        }

        if (team.isRaiding()) {
            messageService.sendMessage(player, "command.raid.search.already-raiding");
            return;
        }

        if (searchCooldownService.inCooldown(team)) {
            String remaining = searchCooldownService.formatRemaining(team);
            messageService.sendMessage(player, "command.raid.search.cooldown", "time", remaining);
            return;
        }

        raidService.search(player, team);
    }
}
