package dioray.datayy.repository.team;

import com.google.common.collect.Maps;
import com.intellectualcrafters.plot.object.Plot;
import dioray.datayy.prototype.Team;
import dioray.datayy.prototype.player.TeamPlayer;
import dioray.datayy.prototype.wall.PlotWall;
import dioray.datayy.repository.Repository;
import org.bukkit.Location;
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

    public PlotWall get(Location location) {
        for(Team team : teamMap.values()) {
            for(PlotWall wall : team.getWalls()) {
                if(isSameLocation(wall, location)) return wall;
            }
        } return null;
    }

    public Team get(Plot plot) {
        for(Team team : teamMap.values()) {
            if(team.getPlot().guessOwner().compareTo(plot.guessOwner()) == 0) return team;
        } return null;
    }

    private boolean isSameLocation(PlotWall plotWall, Location location) {
        return plotWall.getX() == location.getBlockX() && plotWall.getY() == location.getBlockY() && plotWall.getZ() == location.getBlockZ();
    }

}
