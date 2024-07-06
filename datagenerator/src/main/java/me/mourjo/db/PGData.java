package me.mourjo.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import me.mourjo.entities.User;

public class PGData {

    private static final String url = "jdbc:postgresql://localhost:5432/flock";
    private static final String user = "swan";
    private static final String password = "mallard";

    public static void createTable() {
        String sql = """
            DROP TABLE IF EXISTS users;
            CREATE TABLE users (
                id VARCHAR(50) PRIMARY KEY,
                name VARCHAR(100),
                city VARCHAR(20),
                tier VARCHAR(50),
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            );
            """;

        try (Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertData(List<User> users) {
        String sql = """
            INSERT into users (
              id,
              name,
              city,
              tier,
              created_at,
              updated_at
            )
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = conn.prepareStatement(sql)) {
            for (User u : users) {
                statement.setString(1, u.id());
                statement.setString(2, u.name());
                statement.setString(3, u.city());
                statement.setString(4, u.tier());
                statement.setTimestamp(5, Timestamp.from(u.createdAt().toInstant()));
                statement.setTimestamp(6, Timestamp.from(u.updatedAt().toInstant()));
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
