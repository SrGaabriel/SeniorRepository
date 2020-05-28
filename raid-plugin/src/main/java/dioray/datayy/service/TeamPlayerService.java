package dioray.datayy.service;

import dioray.datayy.RaidPlugin;
import dioray.datayy.model.Role;
import dioray.datayy.model.Team;
import dioray.datayy.model.TeamPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TeamPlayerService extends Service {

    private final List<TeamPlayer> teamPlayerList;

    public TeamPlayerService(RaidPlugin main) {
        super(main);

        this.teamPlayerList = new ArrayList<>();
    }

    public TeamPlayer getTeamPlayerByPlayer(Player player) {
        return getTeamPlayerByUUID(player.getUniqueId());
    }

    public TeamPlayer getTeamPlayerByUUID(UUID uuid) {
        for (TeamPlayer teamPlayer : teamPlayerList) {
            if (teamPlayer.getUUID().equals(uuid)) {
                return teamPlayer;
            }
        }

        return null;
    }

    public Team getTeamByPlayer(Player player) {
        TeamPlayer teamPlayer = this.getTeamPlayerByPlayer(player);
        if (teamPlayer == null) return null;

        return teamPlayer.getTeam();
    }

    public void removeTeamPlayer(TeamPlayer teamPlayer) {
        this.teamPlayerList.removeIf(oTeamPlayer -> oTeamPlayer.equals(teamPlayer));
    }

    public void addTeamPlayer(TeamPlayer teamPlayer) {
        this.teamPlayerList.add(teamPlayer);
    }
}
