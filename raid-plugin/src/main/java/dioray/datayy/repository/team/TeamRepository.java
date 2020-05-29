package dioray.datayy.repository.team;

import com.google.common.collect.Maps;
import dioray.datayy.prototype.Team;
import dioray.datayy.prototype.player.TeamPlayer;
import dioray.datayy.repository.Repository;
import org.bukkit.entity.Player;

import java.util.Map;

public class TeamRepository implements Repository<String, Team> {

    private static TeamRepository teamRepository;

    public static TeamRepository getInstance() {
        return teamRepository == null ? (teamRepository = new TeamRepository()) : teamRepository;
    }

    private final Map<String, Team> teamMap = Maps.newLinkedHashMap();

    @Override
    public Map<String, Team> getMap() {
        return teamMap;
    }

    @Override
    public void put(String key, Team value) {
        teamMap.put(key, value);
    }

    @Override
    public void remove(String key, Team value) {
        teamMap.remove(key, value);
    }

    @Override
    public Team get(String key) {
        return teamMap.get(key);
    }

    public TeamPlayer get(Player player) {
        for(Team team : teamMap.values()) {
            for(TeamPlayer teamPlayer : team.getPlayers()) {
                if(teamPlayer.getUuid().compareTo(player.getUniqueId()) == 0) return teamPlayer;
            }
        } return null;
    }

}
