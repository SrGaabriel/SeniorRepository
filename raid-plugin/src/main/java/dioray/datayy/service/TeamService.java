package dioray.datayy.service;

import com.intellectualcrafters.plot.object.Plot;
import dioray.datayy.RaidPlugin;
import dioray.datayy.database.TeamDao;
import dioray.datayy.model.Team;
import dioray.datayy.model.TeamPlayer;
import dioray.datayy.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TeamService extends Service {

    private final List<Team> teamList;

    public TeamService(RaidPlugin main) {
        super(main);

        this.teamList = new ArrayList<>();
    }

    @Override
    public void enable() {
        this.main.getServer().getScheduler().runTaskLaterAsynchronously(this.main, () -> {
            TeamDao teamDao = this.main.getTeamDao();
            List<Team> teams = teamDao.fetchAll();

            if (teams != null) {
                this.teamList.addAll(teams);
            }
        }, 20 * 10);

        this.main.getServer().getScheduler().runTaskTimer(this.main, () -> {
            for (Team team : this.teamList) {
                long diff = team.getRemainingTime() - Util.currentTimeSeconds();
                if (diff > 0) {
                    byte minutes = (byte) (diff / 60);
                    byte seconds = (byte) (diff % 60);

                    String time = String.format("%02dm %02ds", minutes, seconds);
                    if (minutes == 0) {
                        time = String.format("%02ds", seconds);
                    }

                    for (TeamPlayer teamPlayer : team.getOnlinePlayers()) {
                        Player player = teamPlayer.getPlayer();

                        if (team.isRaiding()) {
                            if (minutes == 0 && seconds <= 10) {
                                player.sendTitle(ChatColor.RED + "The raid will end in", ChatColor.RED + time, 0, 40, 0);
                            }
                        } else {
                            player.sendTitle(ChatColor.RED + "The raid will start in", ChatColor.RED + time, 0, 40, 0);
                        }
                    }

                    continue;
                }

                if (diff == 0) {
                    RaidService raidService = this.main.getService(RaidService.class);

                    if (team.isRaiding()) {
                        raidService.endRaid(team);
                    } else {
                        raidService.startRaid(team);
                    }
                }
            }
        }, 0, 20L);
    }

    public Team create(String tag, Plot plot) {
        Team team = new Team(tag, plot, 0, 0);
        this.teamList.add(team);

        return team;
    }

    public Team getByTag(String tag) {
        for (Team team : this.teamList) {
            if (team.getTag().equalsIgnoreCase(tag)) {
                return team;
            }
        }

        return null;
    }

    public Team getByPlot(Plot plot) {
        for (Team team : this.teamList) {
            if (team.getPlot().equals(plot)) {
                return team;
            }
        }

        return null;
    }

    public Team getRaidingTeam(Plot plot) {
        for (Team team : this.teamList) {
            if (team.isRaiding() && plot.getId().toString().equalsIgnoreCase(team.getSelectedPlot())) {
                return team;
            }
        }

        return null;
    }

    public void removeTeam(Team team) {
        this.teamList.removeIf(oTeam -> oTeam.equals(team));
    }

    public List<Team> getTop() {
        List<Team> top = new ArrayList<>(this.teamList);

        top.sort((a, b) -> (int) (b.getValue() - a.getValue()));

        return top.size() > 9 ? top.subList(0, 9) : top;
    }

}
