package dioray.datayy.service;

import dioray.datayy.RaidPlugin;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.logging.Level;

public class DatabaseService extends Service {

    private Connection connection;

    public DatabaseService(RaidPlugin main) {
        super(main);
    }

    @Override
    public void enable() {
        try {
            String filename = this.main.getConfig().getString("sqlite-filename");
            File file = new File(this.main.getDataFolder(), filename);

            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());

            try (PreparedStatement stmt = this.connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS teamplayer(uuid BLOB(16) not null, teamtag VARCHAR(10), role TINYINT not null, name VARCHAR(16) not null, primary key(uuid))"
            )) {
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = this.connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS plotwall(plotx INT not null, ploty INT not null, x INT not null, y INT not null, z INT not null, level INT not null, direction TINYINT not null, primary key(plotx, ploty, x, y, z))"
            )) {
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = this.connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS team(tag VARCHAR(10) not null, ownerid BLOB(16) not null, lastraid BIGINT not null, corecount INT not null, value BIGINT not null, primary key(tag))"
            )) {
                stmt.executeUpdate();
            }

            this.main.getLogger().info("Database connected with success");
        } catch (Exception ex) {
            this.main.getLogger().log(Level.SEVERE, "Something went wrong when connecting to database", ex);
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    @Override
    public void disable() {
        try {
            this.connection.close();
        } catch (Exception ex) {
            this.main.getLogger().log(Level.SEVERE, "Something went wrong when closing database connection", ex);
        }
    }

}