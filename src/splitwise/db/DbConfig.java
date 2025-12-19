package splitwise.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DbConfig {
    private final String url;
    private final String username;
    private final String password;

    private DbConfig(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public static DbConfig load() {
        Properties props = new Properties();

        try (InputStream in = new FileInputStream("db.properties")) {
            props.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load db.properties from project root", e);
        }

        String url = required(props, "db.url");
        String username = required(props, "db.username");
        String password = props.getProperty("db.password", "");

        return new DbConfig(url, username, password);
    }

    private static String required(Properties props, String key) {
        String value = props.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException("Missing required property: " + key);
        }
        return value.trim();
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
