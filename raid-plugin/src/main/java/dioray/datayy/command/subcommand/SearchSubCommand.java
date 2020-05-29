package dioray.datayy.command.subcommand;

import dioray.datayy.RaidPlugin;
import dioray.datayy.model.Role;
import dioray.datayy.model.Team;
import dioray.datayy.model.TeamPlayer;
import dioray.datayy.service.RaidService;
import dioray.datayy.service.SearchCooldownService;
import org.bukkit.entity.Player;

public class SearchSubCommand extends SubCommand {

    private final SearchCooldownService searchCooldownService;
    private final RaidService raidService;

    public SearchSubCommand(RaidPlugin main) {
        super(main, "search", "Search for raids");

        this.searchCooldownService = main.getService(SearchCooldownService.class);
        this.raidService = main.getService(RaidService.class);
    }

    @Override
    public void run(Player player, String[] args) {
        TeamPlayer teamPlayer = teamPlayerService.getTeamPlayerByPlayer(player);

        Team team = teamPlayer.getTeam();
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
