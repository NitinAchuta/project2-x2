import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DatabaseManager {
    private static final Map<String, String> env = loadEnvFile(".env");
    private static final String DB_URL = env.get("DB_URL");
    private static final String DB_USER = env.get("DB_USER");
    private static final String DB_PASS = env.get("DB_PASS");
    
    private Connection conn = null;
    
    public DatabaseManager() {
        connectToDatabase();
    }
    
    private void connectToDatabase() {
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        } catch (Exception e) {
            throw new RuntimeException("Database connection failed: " + e.getMessage(), e);
        }
    }
    
    public Connection getConnection() {
        return conn;
    }
    
    public void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
    
    private static Map<String, String> loadEnvFile(String filePath) {
        Map<String, String> env = new HashMap<>();
        try {
            Files.lines(Paths.get(filePath))
                    .filter(line -> line.contains("=") && !line.startsWith("#"))
                    .forEach(line -> {
                        String[] parts = line.split("=", 2);
                        env.put(parts[0].trim(), parts[1].trim());
                    });
        } catch (Exception e) {
            System.err.println("Failed to load .env file: " + e.getMessage());
        }
        return env;
    }
}
