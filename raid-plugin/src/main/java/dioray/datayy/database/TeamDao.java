package dioray.datayy.database;

import com.intellectualcrafters.plot.object.Plot;
import dioray.datayy.RaidPlugin;
import dioray.datayy.model.Team;
import dioray.datayy.service.DatabaseService;
import dioray.datayy.service.TeamService;
import dioray.datayy.util.Util;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

public class TeamDao extends Dao<Team> {

    private final DatabaseService databaseService;
    private final TeamService teamService;

    public TeamDao(RaidPlugin main) {
        super(main);

        this.databaseService = main.getService(DatabaseService.class);
        this.teamService = main.getService(TeamService.class);
    }

    @Override
    public void insert(Team team) {
        this.main.getServer().getScheduler().runTaskAsynchronously(this.main, () -> {
            Connection connection = databaseService.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(
                         String.format("INSERT INTO team VALUES (?, X'%s', 0, 0, 0)", Util.toSQL(team.getOwnerUUID()))
            )) {
                stmt.setString(1, team.getTag());

                stmt.executeUpdate();
            } catch (Exception ex) {
                this.main.getLogger().log(Level.SEVERE, "Something went wrong when inserting a team", ex);
            }
        });
    }

    public void updateLastRaid(Team team) {
        this.main.getServer().getScheduler().runTaskAsynchronously(this.main, () -> {
            Connection connection = databaseService.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(
                         "UPDATE team SET lastraid = ? WHERE tag = ?"
            )) {
                stmt.setLong(1, Util.currentTimeSeconds());
                stmt.setString(2, team.getTag());

                stmt.executeUpdate();
            } catch (Exception ex) {
                this.main.getLogger().log(Level.SEVERE, "Something went wrong when deleting a team", ex);
            }
        });
    }

    @Override
    public void update(Team team) {
        this.main.getServer().getScheduler().runTaskAsynchronously(this.main, () -> {
            Connection connection = databaseService.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(
                         "UPDATE team SET corecount = ?, value = ? WHERE tag = ?"
            )) {
                stmt.setInt(1, team.getCorecount());
                stmt.setLong(2, team.getValue());
                stmt.setString(3, team.getTag());

                stmt.executeUpdate();
            } catch (Exception ex) {
                this.main.getLogger().log(Level.SEVERE, "Something went wrong when updating a team", ex);
            }
        });
    }

    @Override
    public void delete(Team team) {
        this.main.getServer().getScheduler().runTaskAsynchronously(this.main, () -> {
            Connection connection = databaseService.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(
                         "DELETE FROM team WHERE tag = ?"
            )) {
                stmt.setString(1, team.getTag());

                stmt.executeUpdate();
            } catch (Exception ex) {
                this.main.getLogger().log(Level.SEVERE, "Something went wrong when deleting a team", ex);
            }
        });
    }

    public List<Team> fetchAll() {
        Connection connection = databaseService.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(
                     "SELECT tag, HEX(ownerid), corecount, value FROM team"
        )) {

            try (ResultSet set = stmt.executeQuery()) {
                List<Team> teamList = new ArrayList<>();

                while (set.next()) {
                    String tag = set.getString(1);
                    UUID ownerId = Util.fromSQL(set.getString(2));
                    Set<Plot> plots = this.main.getPlotArea().getPlots(ownerId);
                    int corecount = set.getInt(3);
                    long value = set.getLong(4);

                    teamList.add(new Team(tag, plots.size() > 0 ? plots.iterator().next() : null, corecount, value));
                }

                return teamList;
            }

        } catch (Exception ex) {
            this.main.getLogger().log(Level.SEVERE, "Something went wrong when deleting a team", ex);
        }

        return null;
    }

    public Team getRandomRaidableTeam(Team team) {
        Connection connection = databaseService.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(
                     "SELECT tag FROM team WHERE strftime('%s', 'now') - lastraid >= 21600 AND tag != ? ORDER BY RANDOM() LIMIT 1"
        )) {
            stmt.setString(1, team.getTag());

            try (ResultSet set = stmt.executeQuery()) {
                if (set.next()) {
                    return teamService.getByTag(set.getString(1));
                }
            }
        } catch (Exception ex) {
            this.main.getLogger().log(Level.SEVERE, "Something went wrong when getting random raidable players", ex);
        }

        return null;
    }

    public List<String> getMemberNames(Team team) {
        Connection connection = databaseService.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(
                     "SELECT name FROM teamplayer WHERE teamtag = ?"
        )) {
            stmt.setString(1, team.getTag());

            try (ResultSet set = stmt.executeQuery()) {
                List<String> memberNameList = new ArrayList<>();

                while (set.next()) {
                    memberNameList.add(set.getString(1));
                }

                return memberNameList;
            }
        } catch (Exception ex) {
            this.main.getLogger().log(Level.SEVERE, "Something went wrong when getting member names", ex);
        }

        return null;
    }

    public List<UUID> getMemberUUIDs(Team team) {
        Connection connection = databaseService.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(
                     "SELECT HEX(uuid) FROM teamplayer WHERE teamtag = ?"
        )) {
            stmt.setString(1, team.getTag());

            try (ResultSet set = stmt.executeQuery()) {
                List<UUID> memberUUIDList = new ArrayList<>();

                while (set.next()) {
                    memberUUIDList.add(Util.fromSQL(set.getString(1)));
                }

                return memberUUIDList;
            }
        } catch (Exception ex) {
            this.main.getLogger().log(Level.SEVERE, "Something went wrong when getting member uuids", ex);
        }

        return null;
    }
}
