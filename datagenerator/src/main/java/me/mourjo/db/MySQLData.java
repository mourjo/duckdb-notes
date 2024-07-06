package me.mourjo.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Set;
import me.mourjo.entities.Order;

public class MySQLData {

    private static final String url = "jdbc:mysql://localhost:3306/flock";
    private static final String user = "swan";
    private static final String password = "mallard";

    public static void createTable() {
        String tableDrop = """
            DROP TABLE IF EXISTS orders;
            """;
        String tableCreation = """
            CREATE TABLE orders (
                id VARCHAR(50) PRIMARY KEY,
                source VARCHAR(50),
                destination VARCHAR(50),
                created_by VARCHAR(50),
                total_amount DECIMAL(10,2),
                delivery_charge DECIMAL(10,2),
                tax_amount DECIMAL(10,2),
                currency CHAR(3),
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                delivered_at TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;

        try (Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement dropStatement = conn.prepareStatement(tableDrop);
            PreparedStatement creationStatement = conn.prepareStatement(tableCreation)) {

            dropStatement.execute();
            creationStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertOrders(Set<Order> orders) {
        String sql = """
            INSERT into orders (id, source, destination, created_by, total_amount, delivery_charge, tax_amount, currency, created_at, delivered_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = conn.prepareStatement(sql)) {
            for (Order order : orders.stream().toList()) {
                statement.setString(1, order.id());
                statement.setString(2, order.source());
                statement.setString(3, order.destination());
                statement.setString(4, order.createdBy());
                statement.setDouble(5, order.totalAmount());
                statement.setDouble(6, order.deliveryCharge());
                statement.setDouble(7, order.taxAmount());
                statement.setString(8, order.currency());
                statement.setTimestamp(9, Timestamp.from(order.createdAt().toInstant()));
                statement.setTimestamp(10, Timestamp.from(order.deliveredAt().toInstant()));
                statement.setTimestamp(11, Timestamp.from(order.updatedAt().toInstant()));
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
