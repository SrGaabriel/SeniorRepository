package com.raidplugin.sdk.database;

import com.raidplugin.RaidPlugin;
import com.raidplugin.sdk.database.service.DatabaseService;
import com.raidplugin.sdk.database.sustainer.DatabaseSustainer;

import java.io.File;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.concurrent.CompletableFuture.runAsync;

public class DatabaseProvider extends DatabaseSustainer implements DatabaseService {

    private final ExecutorService executorService = Executors.newFixedThreadPool(3);
    private final RaidPlugin raidPlugin = RaidPlugin.getInstance();

    @Override
    public <T> Optional<T> query(String query, Class<T> clazz, DatabaseResult<T> result, Object... objects) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            setGeneric(preparedStatement, objects);

            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next() ? Optional.of(result.apply(resultSet)) : Optional.empty();
        } catch (Exception e) {
            e.printStackTrace();
        } return Optional.empty();
    }

    @Override
    public void update(String query, Object... objects) {
        runAsync(() -> {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                setGeneric(preparedStatement, objects); preparedStatement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, executorService);
    }

    @Override
    public void openConnection() {
        File file = new File(raidPlugin.getDataFolder(), "database.db");

        try {
            Class.forName("org.sqlite.JDBC");

            connection = DriverManager.getConnection("jdbc:sqlite:" + file);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            update("CREATE TABLE IF NOT EXISTS team(uuid CHAR(16), prefix VARCHAR(10), core INT, value LONG)");
        }
    }

    @Override
    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void setGeneric(PreparedStatement preparedStatement, Object... objects) throws SQLException {
        for(int i = 0; i!=objects.length; i++) {
            databaseAdapter.setGeneric(i + 1, preparedStatement, objects[i]);
        }
    }
}
