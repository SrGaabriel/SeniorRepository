package dioray.datayy.database;

import com.intellectualcrafters.plot.object.Plot;
import com.sk89q.worldedit.PlayerDirection;
import dioray.datayy.RaidPlugin;
import dioray.datayy.model.BlockPosition;
import dioray.datayy.model.PlotWall;
import dioray.datayy.service.DatabaseService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;

public class PlotWallDao extends Dao<PlotWall> {

    private final DatabaseService databaseService;

    public PlotWallDao(RaidPlugin main) {
        super(main);

        this.databaseService = main.getService(DatabaseService.class);
    }

    @Override
    public void insert(PlotWall plotWall) {
        this.main.getServer().getScheduler().runTaskAsynchronously(this.main, () -> {
            Connection connection = databaseService.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(
                         "INSERT INTO plotwall VALUES(?, ?, ?, ?, ?, ?, ?)"
            )) {
                stmt.setInt(1, plotWall.getPlot().getId().x);
                stmt.setInt(2, plotWall.getPlot().getId().y);
                stmt.setInt(3, plotWall.getPosition().getX());
                stmt.setInt(4, plotWall.getPosition().getY());
                stmt.setInt(5, plotWall.getPosition().getZ());
                stmt.setInt(6, plotWall.getLevel());
                stmt.setByte(7, (byte) plotWall.getDirection().ordinal());

                stmt.executeUpdate();
            } catch (Exception ex) {
                this.main.getLogger().log(Level.SEVERE, "Something went wrong when inserting " + plotWall, ex);
            }
        });
    }

    @Override
    public void update(PlotWall plotWall) {
        this.main.getServer().getScheduler().runTaskAsynchronously(this.main, () -> {
            Connection connection = databaseService.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(
                         "UPDATE plotwall SET level = ? WHERE x = ? AND y = ? AND z = ?"
            )) {
                stmt.setInt(1, plotWall.getLevel());
                stmt.setInt(2, plotWall.getPosition().getX());
                stmt.setInt(3, plotWall.getPosition().getY());
                stmt.setInt(4, plotWall.getPosition().getZ());

                stmt.executeUpdate();
            } catch (Exception ex) {
                this.main.getLogger().log(Level.SEVERE, "Something went wrong when updating " + plotWall, ex);
            }
        });
    }

    @Override
    public void delete(PlotWall plotWall) {
        this.main.getServer().getScheduler().runTaskAsynchronously(this.main, () -> {
            Connection connection = databaseService.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(
                         "DELETE FROM plotwall WHERE x = ? AND y = ? AND z = ?"
            )) {
                stmt.setInt(1, plotWall.getPosition().getX());
                stmt.setInt(2, plotWall.getPosition().getY());
                stmt.setInt(3, plotWall.getPosition().getZ());

                stmt.executeUpdate();
            } catch (Exception ex) {
                this.main.getLogger().log(Level.SEVERE, "Something went wrong when deleting " + plotWall, ex);
            }
        });
    }

    public PlotWall select(Plot plot, BlockPosition position) {
        Connection connection = databaseService.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(
                     "SELECT level, direction FROM plotwall WHERE x = ? AND y = ? AND z = ?"
        )) {
            stmt.setInt(1, position.getX());
            stmt.setInt(2, position.getY());
            stmt.setInt(3, position.getZ());

            try (ResultSet set = stmt.executeQuery()) {
                if (set.next()) {
                    int level = set.getInt(1);
                    byte direction = set.getByte(2);

                    return new PlotWall(level, position, plot, getByOrdinal(direction));
                }
            }
        } catch (Exception ex) {
            this.main.getLogger().log(Level.SEVERE, "Something went wrong when selecting plot wall on " + position, ex);
        }

        return null;
    }

    public void fetchAll(Plot plot, Consumer<List<PlotWall>> consumer) {
        this.main.getServer().getScheduler().runTaskAsynchronously(this.main, () -> {
            Connection connection = databaseService.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(
                         "SELECT x, y, z, level, direction FROM plotwall WHERE plotx = ? AND ploty = ?"
            )) {
                stmt.setInt(1, plot.getId().x);
                stmt.setInt(2, plot.getId().y);

                try (ResultSet set = stmt.executeQuery()) {
                    List<PlotWall> valueBlockList = new ArrayList<>();

                    while (set.next()) {
                        BlockPosition position = new BlockPosition(set.getInt(1), set.getInt(2), set.getInt(3));
                        int level = set.getInt(4);
                        byte direction = set.getByte(5);

                        valueBlockList.add(new PlotWall(level, position, plot, getByOrdinal(direction)));
                    }

                    consumer.accept(valueBlockList);
                }
            } catch (Exception ex) {
                this.main.getLogger().log(Level.SEVERE, "Something went wrong when fetching value blocks of " + plot.getId().toCommaSeparatedString(), ex);
            }
        });
    }

    public int getCount(Plot plot) {
        Connection connection = databaseService.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(
                     "SELECT COUNT(*) FROM plotwall WHERE plotx = ? AND ploty = ?"
        )) {
            stmt.setInt(1, plot.getId().x);
            stmt.setInt(2, plot.getId().y);

            try (ResultSet set = stmt.executeQuery()) {
                if (set.next()) {
                    return set.getInt(1);
                }
            }
        } catch (Exception ex) {
            this.main.getLogger().log(Level.SEVERE, "Something went wrong when fetching value blocks of " + plot.getId().toCommaSeparatedString(), ex);
        }

        return 0;
    }

    private static PlayerDirection getByOrdinal(byte ordinal) {
        for (PlayerDirection dir : PlayerDirection.values()) {
            if (dir.ordinal() == ordinal) {
                return dir;
            }
        }

        return null;
    }

    public void deleteAll(Plot plot) {
        Connection connection = databaseService.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM plotwall WHERE plotx = ? AND ploty = ?"
        )) {
            stmt.setInt(1, plot.getId().x);
            stmt.setInt(2, plot.getId().y);

            stmt.executeUpdate();
        } catch (Exception ex) {
            this.main.getLogger().log(Level.SEVERE, "Something went wrong when deleting all plotwalls of " + plot, ex);
        }
    }
}
