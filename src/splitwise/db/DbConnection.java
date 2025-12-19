package splitwise.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    private DbConnection() {
    }

    public static Connection open() throws SQLException {
        DbConfig config = DbConfig.load();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ignored) {
        }

        return DriverManager.getConnection(config.getUrl(), config.getUsername(), config.getPassword());
    }

    public static void testConnection() {
        try (Connection ignored = open()) {
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to connect to MySQL using db.properties", e);
        }
    }
}
