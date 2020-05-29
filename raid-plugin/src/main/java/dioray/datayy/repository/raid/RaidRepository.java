package dioray.datayy.repository.raid;

import com.google.common.collect.Maps;
import dioray.datayy.prototype.Team;
import dioray.datayy.repository.Repository;

import java.util.Map;

public class RaidRepository implements Repository<Team, Team> {

    private static RaidRepository raidRepository;

    public static RaidRepository getInstance() {
        return raidRepository == null ? (raidRepository = new RaidRepository()) : raidRepository;
    }

    private final Map<Team, Team> raidMap = Maps.newConcurrentMap();

    @Override
    public Map<Team, Team> getMap() {
        return raidMap;
    }

    @Override
    public void put(Team key, Team value) {
        raidMap.put(key, value);
    }

    @Override
    public void remove(Team key, Team value) {
        raidMap.remove(key, value);
    }

    @Override
    public Team get(Team key) {
        return raidMap.get(key);
    }

}
