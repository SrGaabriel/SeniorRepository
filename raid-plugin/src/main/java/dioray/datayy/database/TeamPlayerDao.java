package dioray.datayy.database;

import dioray.datayy.RaidPlugin;
import dioray.datayy.model.Role;
import dioray.datayy.model.TeamPlayer;
import dioray.datayy.service.DatabaseService;
import dioray.datayy.service.TeamService;
import dioray.datayy.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;
import java.util.logging.Level;

public class TeamPlayerDao extends Dao<TeamPlayer> {

    private final DatabaseService databaseService;
    private final TeamService teamService;

    public TeamPlayerDao(RaidPlugin main) {
        super(main);

        this.databaseService = main.getService(DatabaseService.class);
        this.teamService = main.getService(TeamService.class);
    }

    @Override
    public void insert(TeamPlayer teamPlayer) {
        Connection connection = databaseService.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(
                     String.format("INSERT INTO teamplayer VALUES (X'%s', ?, ?, ?)", Util.toSQL(teamPlayer.getUUID()))
        )) {
            stmt.setString(1, teamPlayer.getTeam() != null ? teamPlayer.getTeam().getTag() : null);
            stmt.setByte(2, (byte) teamPlayer.getRole().ordinal());
            stmt.setString(3, teamPlayer.getPlayer().getName());

            stmt.executeUpdate();
        } catch (Exception ex) {
            this.main.getLogger().log(Level.SEVERE, "Something went wrong when inserting teamplayer", ex);
        }
    }

    public String getName(UUID uuid) {
        Connection connection = databaseService.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(
                     String.format("SELECT name FROM teamplayer WHERE uuid = X'%s'", Util.toSQL(uuid))
        )) {

            try (ResultSet set = stmt.executeQuery()) {
                if (set.next()) {
                    return set.getString(1);
                }
            }
        } catch (Exception ex) {
            this.main.getLogger().log(Level.SEVERE, "Something went wrong when selecting name of teamplayer", ex);
        }

        return null;
    }

    @Override
    public void update(TeamPlayer teamPlayer) {
        this.main.getServer().getScheduler().runTaskAsynchronously(this.main, () -> {
            Connection connection = databaseService.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(
                         String.format("UPDATE teamplayer SET teamtag = ?, role = ? WHERE uuid = X'%s'", Util.toSQL(teamPlayer.getUUID()))
            )) {
                stmt.setString(1, teamPlayer.getTeam() != null ? teamPlayer.getTeam().getTag() : null);
                stmt.setByte(2, (byte) teamPlayer.getRole().ordinal());

                stmt.executeUpdate();
            } catch (Exception ex) {
                this.main.getLogger().log(Level.SEVERE, "Something went wrong when updating teamplayer", ex);
            }
        });
    }

    @Override
    public void delete(TeamPlayer teamPlayer) {}

    public TeamPlayer select(UUID uuid) {
        Connection connection = databaseService.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(
                     String.format("SELECT teamtag, role FROM teamplayer WHERE uuid = X'%s'", Util.toSQL(uuid))
        )) {

            try (ResultSet set = stmt.executeQuery()) {
                if (set.next()) {
                    String teamTag = set.getString(1);
                    byte role = set.getByte(2);

                    return new TeamPlayer(uuid, teamTag != null ? teamService.getByTag(teamTag) : null, Role.getByOrdinal(role));
                }
            }
        } catch (Exception ex) {
            this.main.getLogger().log(Level.SEVERE, "Something went wrong when selecting teamplayer", ex);
        }

        return null;
    }

}