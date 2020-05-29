package dioray.datayy.command.subcommand;

import dioray.datayy.RaidPlugin;
import dioray.datayy.model.Team;
import dioray.datayy.service.SearchCooldownService;
import dioray.datayy.service.TeamService;
import org.bukkit.entity.Player;

public final class CooldownSubCommand extends SubCommand {

    private final TeamService teamService;
    private final SearchCooldownService searchCooldownService;

    public CooldownSubCommand(RaidPlugin main) {
        super(main, "cooldown", "raid.cooldown", "Bypass raid cooldown");

        this.teamService = main.getService(TeamService.class);
        this.searchCooldownService = main.getService(SearchCooldownService.class);
    }

    @Override
    public void run(Player player, String[] args) {
        if (args.length < 1) {
            messageService.sendMessage(player, "command.raid.cooldown.usage");

            return;
        }

        Team team = teamService.getByTag(args[0]);
        if (team == null) {
            messageService.sendMessage(player, "command.raid.cooldown.not-found", "team", args[0]);

            return;
        }

        searchCooldownService.removeCooldown(team);

        messageService.sendMessage(player, "command.raid.cooldown.success", "team", team.getTag());
    }
}
