package dioray.datayy.placeholder;

import dioray.datayy.prototype.player.TeamPlayer;
import dioray.datayy.repository.team.TeamRepository;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class RaidExpansion extends PlaceholderExpansion {

    private final TeamRepository teamRepository = TeamRepository.getInstance();

    @Override
    public String getIdentifier() {
        return "raidplugin";
    }

    @Override
    public String getAuthor() {
        return "Wizard";
    }

    @Override
    public String getVersion() {
        return "2.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, String name) {
        if(!name.equalsIgnoreCase("team-value")) return " ";

        TeamPlayer teamPlayer = teamRepository.get(player); if(teamPlayer == null) return "N/A";

        return String.valueOf(teamPlayer.getTeam().getValue());
    }

}
