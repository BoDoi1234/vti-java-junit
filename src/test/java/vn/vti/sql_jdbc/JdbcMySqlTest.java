package vn.vti.sql_jdbc;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import vn.vti.DatabaseConnection;
import vn.vti.DatabaseType;

import java.sql.*;

public class JdbcMySqlTest {
    private static Connection sqlConnection;

    @BeforeAll
    public static void setUpEnvironment() throws SQLException {
        // Init database connection
        sqlConnection = (Connection) new DatabaseConnection().initConnectionToDb(DatabaseType.MYSQL, "school_management", "root", "Admin123");
    }

    @AfterAll
    static void tearDown() throws SQLException {
        // Close DB connection
        sqlConnection.close();
    }

    @Test
    void test_create_table() throws SQLException {
        // Create statement
        Statement statement = sqlConnection.createStatement();
        statement.execute("CREATE TABLE schools ( school_id INT AUTO_INCREMENT PRIMARY KEY, school_name VARCHAR(150) NOT NULL, address VARCHAR(255), city VARCHAR(100), state VARCHAR(50), postal_code VARCHAR(20), phone_number VARCHAR(20), email VARCHAR(100) UNIQUE, created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP );");
    }

    @Test
    void test_insert_into() throws SQLException {
        // Use prepareStatement to avoid SQL Injection && increase performance
        String insertSql = "INSERT INTO schools (school_name, address, city, state, postal_code, phone_number, email)\n" +
                "VALUES (?, ?, ? , ?, ?, ?, ?);\n";

        PreparedStatement preparedStatement = sqlConnection.prepareStatement(insertSql);
        preparedStatement.setString(1, "Greenwood High School");
        preparedStatement.setString(2, "123 Education Lane");
        preparedStatement.setString(3, "Austin");
        preparedStatement.setString(4, "TX");
        preparedStatement.setString(5, "78701");
        preparedStatement.setString(6, "512-555-0199");
        preparedStatement.setString(7, "info@greenwoodhigh.edu");

        preparedStatement.execute();
    }

    @Test
    void test_delete() throws SQLException {
        String deleteSql = "DELETE FROM schools WHERE city = ?";
        PreparedStatement preparedStatement = sqlConnection.prepareStatement(deleteSql);
        preparedStatement.setString(1, "Austin");

        preparedStatement.executeUpdate();
    }

    @Test
    void test_update() throws SQLException {
        String updateSql = "UPDATE school SET school_name = ?, email = ?";
        PreparedStatement preparedStatement = sqlConnection.prepareStatement(updateSql);
        preparedStatement.setString(1, "Harvard");
        preparedStatement.setString(2, "info@Harvard.com");

        preparedStatement.executeUpdate();
    }

    @Test
    void test_query() throws SQLException {
        String querySql = "";
        Statement statement = sqlConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(querySql);
        while (resultSet.next()) {
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            int points_earned = resultSet.getInt("points_earned");

            Assertions.assertAll(() -> {
                Assertions.assertFalse(firstName.isEmpty());
                Assertions.assertFalse(lastName.isEmpty());
                Assertions.assertTrue(points_earned >= 0);
            });
        }
    }

    @Test
    void test_query_1() throws SQLException {
        // Call api to add new student
        // Verify api response
        // Verify Database
        String query = "SELECT * FROM students limit 1";
        ResultSet set = sqlConnection.createStatement().executeQuery(query);
    }
}
