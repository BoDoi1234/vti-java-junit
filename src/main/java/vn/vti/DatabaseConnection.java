package vn.vti;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public Object initConnectionToDb(DatabaseType databaseType, String databaseName, String username, String password) throws SQLException {
        Object result;
        Log.info(DatabaseConnection.class, "Connecting to Database", databaseName, "in", databaseType, "...");

        switch (databaseType) {
            case MYSQL:
                try {
                    result = DriverManager.getConnection(
                            "jdbc:mysql://127.0.0.1:3306/" + databaseName,
                            username,
                            password);
                } catch (SQLException e) {
                    throw new SQLException(e);
                }
                break;
            default:
                throw new RuntimeException("Not support connection to " + databaseType.name());
        }

        Log.info(DatabaseConnection.class, "Connected to ", databaseName, "in", databaseType, "successfully!");
        return result;
    }
}
