package dioray.datayy.placeholder;

import dioray.datayy.RaidPlugin;
import dioray.datayy.model.Team;
import dioray.datayy.model.TeamPlayer;
import dioray.datayy.service.TeamPlayerService;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class RaidExpansion extends PlaceholderExpansion {

    private final TeamPlayerService teamPlayerService;

    public RaidExpansion(RaidPlugin main) {
        this.teamPlayerService = main.getService(TeamPlayerService.class);
    }

    @Override
    public String getIdentifier() {
        return "raidplugin";
    }

    @Override
    public String getAuthor() {
        return "Braayy";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, String name) {
        if (player == null) return "";

        if (name.equalsIgnoreCase("value")) {
            TeamPlayer teamPlayer = this.teamPlayerService.getTeamPlayerByPlayer(player);
            Team team = teamPlayer.getTeam();

            if (team == null) return "N/A";

            return String.valueOf(team.getValue());
        }

        return "";
    }
}
