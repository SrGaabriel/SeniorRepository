package dioray.datayy.database.sustainer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class DatabaseSustainer {

    protected Connection connection;

    public abstract void openConnection();
    public abstract void closeConnection();

    protected DatabaseAdapter databaseAdapter = new DatabaseAdapter();

    protected static class DatabaseAdapter {
        public void setGeneric(int index, PreparedStatement preparedStatement, Object object) throws SQLException {
            switch (object.getClass().getTypeName()) {
                case "String":
                    preparedStatement.setString(index, (String) object); break;
                case "Integer":
                    preparedStatement.setInt(index, (Integer) object); break;
                case "Long":
                    preparedStatement.setLong(index, (Long) object); break;
            }
        }
    }

}
