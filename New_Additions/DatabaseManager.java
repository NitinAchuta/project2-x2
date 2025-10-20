package New_Additions;

import java.sql.*;
import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * DatabaseManager handles database connections and provides methods for data
 * access. Supports both real database connections and mock data mode.
 * 
 * @author harry
 * @version 1.0
 * @since 2024
 */
public class DatabaseManager {

    private boolean isConnected;
    private boolean useMockData;
    private Connection connection;
    private MockDataProvider mockProvider;

    /**
     * Constructs a new DatabaseManager instance.
     * Attempts to establish a database connection, falls back to mock data mode
     * if connection fails.
     * 
     * @author harry
     */
    public DatabaseManager() {
        this.mockProvider = new MockDataProvider();
        try {
            initializeConnection();
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            System.out.println("Switching to mock data mode...");
            this.useMockData = true;
            this.isConnected = false;
        }
    }

    /**
     * Initializes the database connection using environment variables.
     * 
     * @throws SQLException if database connection fails
     * @author harry
     */
    private void initializeConnection() throws SQLException {
        Map<String, String> env = loadEnvironment();

        if (env.containsKey("DB_URL") && env.containsKey("DB_USER") && env.containsKey("DB_PASS")) {
            String url = env.get("DB_URL");
            String user = env.get("DB_USER");
            String password = env.get("DB_PASS");

            try {
                Class.forName("org.postgresql.Driver");
                this.connection = DriverManager.getConnection(url, user, password);
                this.isConnected = true;
                this.useMockData = false;
                System.out.println("Successfully connected to database: " + url);
            } catch (ClassNotFoundException e) {
                System.err.println("PostgreSQL driver not found. Using mock data.");
                this.useMockData = true;
                this.isConnected = false;
            }
        } else {
            System.out.println("Database credentials not found in .env file. Using mock data.");
            this.useMockData = true;
            this.isConnected = false;
        }
    }

    /**
     * Checks if the database is currently connected.
     * 
     * @return true if connected to database, false otherwise
     * @author harry
     */
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * Checks if the system is currently using mock data.
     * 
     * @return true if using mock data, false if using real database
     * @author harry
     */
    public boolean isUsingMockData() {
        return useMockData;
    }

    /**
     * Gets the current connection status as a string.
     * 
     * @return "Connected to Database" if connected, "Using Mock Data" otherwise
     * @author harry
     */
    public String getConnectionStatus() {
        if (isConnected) {
            return "Connected to Database";
        } else {
            return "Using Mock Data";
        }
    }

    /**
     * Retrieves all menu items from the database or mock data.
     * 
     * @return List of MenuItem objects containing all menu items
     * @author harry
     */
    public List<MenuItem> getAllMenuItems() {
        if (useMockData) {
            return mockProvider.getAllMenuItems();
        }

        List<MenuItem> items = new ArrayList<>();
        String query = "SELECT menuitemid, drinkcategory, menuitemname, price FROM menuitems ORDER BY menuitemname";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                MenuItem item = new MenuItem(
                        rs.getInt("menuitemid"),
                        rs.getString("drinkcategory"),
                        rs.getString("menuitemname"),
                        rs.getDouble("price"));
                items.add(item);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching menu items: " + e.getMessage());
            return mockProvider.getAllMenuItems();
        }

        return items;
    }

    /**
     * Retrieves all inventory items from the database or mock data.
     * 
     * @return List of Inventory objects containing all inventory items
     * @author harry
     */
    public List<Inventory> getAllInventory() {
        if (useMockData) {
            return mockProvider.getAllInventory();
        }

        List<Inventory> items = new ArrayList<>();
        String query = "SELECT ingredientid, ingredientname, ingredientcount FROM inventory ORDER BY ingredientname";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Inventory item = new Inventory(
                        rs.getInt("ingredientid"),
                        rs.getString("ingredientname"),
                        rs.getInt("ingredientcount"));
                items.add(item);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching inventory: " + e.getMessage());
            return mockProvider.getAllInventory();
        }

        return items;
    }

    /**
     * Retrieves all employees from the database or mock data.
     * 
     * @return List of Employee objects containing all employees
     * @author harry
     */
    public List<Employee> getAllEmployees() {
        if (useMockData) {
            return mockProvider.getAllEmployees();
        }

        List<Employee> employees = new ArrayList<>();
        String query = "SELECT employeeid, employeename, employeerole, hoursworked FROM employees ORDER BY employeename";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Employee emp = new Employee(
                        rs.getInt("employeeid"),
                        rs.getString("employeename"),
                        rs.getString("employeerole"),
                        rs.getInt("hoursworked"));
                employees.add(emp);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching employees: " + e.getMessage());
            return mockProvider.getAllEmployees();
        }

        return employees;
    }

    /**
     * Retrieves all orders from the database or mock data.
     * 
     * @return List of Order objects containing all orders
     * @author harry
     */
    public List<Order> getAllOrders() {
        if (useMockData) {
            return mockProvider.getAllOrders();
        }

        List<Order> orders = new ArrayList<>();
        String query = "SELECT orderid, timeoforder, customerid, employeeid, totalcost, orderweek FROM orders ORDER BY timeoforder DESC";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Order order = new Order(
                        rs.getInt("orderid"),
                        rs.getTimestamp("timeoforder"),
                        rs.getObject("customerid", Integer.class),
                        rs.getInt("employeeid"),
                        rs.getDouble("totalcost"),
                        rs.getInt("orderweek"));
                orders.add(order);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching orders: " + e.getMessage());
            return mockProvider.getAllOrders();
        }

        return orders;
    }

    /**
     * Creates a new order with associated order items.
     * 
     * @param order the Order object to be created
     * @param orderItems List of OrderItem objects for the order
     * @return true if order creation was successful, false otherwise
     * @author harry
     */
    public boolean createOrder(Order order, List<OrderItem> orderItems) {
        if (useMockData) {
            return mockProvider.createOrder(order, orderItems);
        }

        try {
            connection.setAutoCommit(false);

            // Insert order
            String orderQuery = "INSERT INTO orders (timeoforder, customerid, employeeid, totalcost, orderweek) VALUES (?, ?, ?, ?, ?) RETURNING orderid";
            int orderId;

            try (PreparedStatement pstmt = connection.prepareStatement(orderQuery)) {
                pstmt.setTimestamp(1, order.getTimeOfOrder());
                pstmt.setObject(2, order.getCustomerID());
                pstmt.setInt(3, order.getEmployeeID());
                pstmt.setDouble(4, order.getTotalCost());
                pstmt.setInt(5, order.getOrderWeek());

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        orderId = rs.getInt(1);
                    } else {
                        throw new SQLException("Failed to get order ID");
                    }
                }
            }

            // Insert order items
            String itemQuery = "INSERT INTO orderitems (orderid, menuitemid, quantity) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(itemQuery)) {
                for (OrderItem item : orderItems) {
                    pstmt.setInt(1, orderId);
                    pstmt.setInt(2, item.getMenuItemID());
                    pstmt.setInt(3, item.getQuantity());
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            }

            connection.commit();
            connection.setAutoCommit(true);
            return true;

        } catch (SQLException e) {
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException rollbackEx) {
                System.err.println("Error rolling back transaction: " + rollbackEx.getMessage());
            }
            System.err.println("Error creating order: " + e.getMessage());
            return false;
        }
    }

    /**
     * Adds a new menu item to the database or mock data.
     * 
     * @param item the MenuItem object to be added
     * @return true if menu item was added successfully, false otherwise
     * @author harry
     */
    public boolean addMenuItem(MenuItem item) {
        if (useMockData) {
            return mockProvider.addMenuItem(item);
        }

        String query = "INSERT INTO menuitems (drinkcategory, menuitemname, price) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, item.getDrinkCategory());
            pstmt.setString(2, item.getMenuItemName());
            pstmt.setDouble(3, item.getPrice());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error adding menu item: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates the price of an existing menu item.
     * 
     * @param itemId the ID of the menu item to update
     * @param newPrice the new price for the menu item
     * @return true if update was successful, false otherwise
     * @author harry
     */
    public boolean updateMenuItemPrice(int itemId, double newPrice) {
        if (useMockData) {
            return mockProvider.updateMenuItemPrice(itemId, newPrice);
        }

        String query = "UPDATE menuitems SET price = ? WHERE menuitemid = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setDouble(1, newPrice);
            pstmt.setInt(2, itemId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating menu item price: " + e.getMessage());
            return false;
        }
    }

    /**
     * Adds a new inventory item to the database or mock data.
     * 
     * @param item the Inventory object to be added
     * @return true if inventory item was added successfully, false otherwise
     * @author harry
     */
    public boolean addInventoryItem(Inventory item) {
        if (useMockData) {
            return mockProvider.addInventoryItem(item);
        }

        String query = "INSERT INTO inventory (ingredientname, ingredientcount) VALUES (?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, item.getIngredientName());
            pstmt.setInt(2, item.getIngredientCount());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error adding inventory item: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates the quantity of an existing inventory item.
     * 
     * @param itemId the ID of the inventory item to update
     * @param newQuantity the new quantity for the inventory item
     * @return true if update was successful, false otherwise
     * @author harry
     */
    public boolean updateInventoryQuantity(int itemId, int newQuantity) {
        if (useMockData) {
            return mockProvider.updateInventoryQuantity(itemId, newQuantity);
        }

        String query = "UPDATE inventory SET ingredientcount = ? WHERE ingredientid = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, newQuantity);
            pstmt.setInt(2, itemId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating inventory quantity: " + e.getMessage());
            return false;
        }
    }

    /**
     * Adds a new employee to the database or mock data.
     * 
     * @param employee the Employee object to be added
     * @return true if employee was added successfully, false otherwise
     * @author harry
     */
    public boolean addEmployee(Employee employee) {
        if (useMockData) {
            return mockProvider.addEmployee(employee);
        }

        String query = "INSERT INTO employees (employeename, employeerole, hoursworked) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, employee.getEmployeeName());
            pstmt.setString(2, employee.getEmployeeRole());
            pstmt.setInt(3, employee.getHoursWorked());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error adding employee: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates an existing employee in the database or mock data.
     * 
     * @param employee the Employee object with updated information
     * @return true if update was successful, false otherwise
     * @author harry
     */
    public boolean updateEmployee(Employee employee) {
        if (useMockData) {
            return mockProvider.updateEmployee(employee);
        }

        String query = "UPDATE employees SET employeename = ?, employeerole = ?, hoursworked = ? WHERE employeeid = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, employee.getEmployeeName());
            pstmt.setString(2, employee.getEmployeeRole());
            pstmt.setInt(3, employee.getHoursWorked());
            pstmt.setInt(4, employee.getEmployeeID());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating employee: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes an employee from the database or mock data.
     * 
     * @param employeeId the ID of the employee to delete
     * @return true if employee was deleted successfully, false otherwise
     * @author harry
     */
    public boolean deleteEmployee(int employeeId) {
        if (useMockData) {
            return mockProvider.deleteEmployee(employeeId);
        }

        String query = "DELETE FROM employees WHERE employeeid = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, employeeId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting employee: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves product usage data for charts and analytics.
     * 
     * @return Map with product names as keys and usage counts as values
     * @author harry
     */
    public Map<String, Integer> getProductUsageData() {
        if (useMockData) {
            return mockProvider.getProductUsageData();
        }

        Map<String, Integer> usage = new HashMap<>();
        String query = """
                SELECT m.menuitemname, SUM(oi.quantity) as total_sold
                FROM menuitems m
                JOIN orderitems oi ON m.menuitemid = oi.menuitemid
                JOIN orders o ON oi.orderid = o.orderid
                WHERE DATE(o.timeoforder) >= CURRENT_DATE - INTERVAL '30 days'
                GROUP BY m.menuitemname
                ORDER BY total_sold DESC
                """;

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                usage.put(rs.getString("menuitemname"), rs.getInt("total_sold"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching product usage data: " + e.getMessage());
            return mockProvider.getProductUsageData();
        }

        return usage;
    }

    /**
     * Calculates total sales for a given date range.
     * 
     * @param startDate the start date for sales calculation
     * @param endDate the end date for sales calculation
     * @return total sales amount for the specified date range
     * @author harry
     */
    public double getTotalSales(java.sql.Date startDate, java.sql.Date endDate) {
        if (useMockData) {
            return mockProvider.getTotalSales(startDate, endDate);
        }

        String query = "SELECT COALESCE(SUM(totalcost), 0) as total FROM orders WHERE DATE(timeoforder) BETWEEN ? AND ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setDate(1, startDate);
            pstmt.setDate(2, endDate);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching total sales: " + e.getMessage());
            return mockProvider.getTotalSales(startDate, endDate);
        }

        return 0.0;
    }

    /**
     * Closes the database connection.
     * This method is expected by ManagerUI.
     * 
     * @author harry
     */
    public void closeConnection() {
        close();
    }

    /**
     * Closes the database connection.
     * 
     * @author harry
     */
    public void close() {
        if (connection != null && !useMockData) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }

    /**
     * Loads environment variables from .env file.
     * 
     * @return Map containing environment variables
     * @author harry
     */
    private Map<String, String> loadEnvironment() {
        Map<String, String> env = new HashMap<>();
        Path envPath = Paths.get(".env");

        if (!Files.exists(envPath)) {
            System.out.println("No .env file found. Using mock data mode.");
            return env;
        }

        try (Stream<String> lines = Files.lines(envPath)) {
            lines.filter(line -> !line.trim().isEmpty() && !line.startsWith("#"))
                    .forEach(line -> {
                        String[] parts = line.split("=", 2);
                        if (parts.length == 2) {
                            env.put(parts[0].trim(), parts[1].trim());
                        }
                    });
        } catch (IOException e) {
            System.err.println("Error reading environment file: " + e.getMessage());
        }

        return env;
    }
}
