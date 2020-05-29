package dioray.datayy.database.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public interface DatabaseService {

    <T> Optional<T> query(String query, Class<T> clazz, DatabaseResult<T> result, Object... objects);
    void update(String query, Object... objects);

    interface DatabaseResult<T> {
        T apply(ResultSet resultSet) throws SQLException;
    }

}
