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

    /** Flag indicating if database connection is established */
    private boolean isConnected;
    /** Flag indicating if system is using mock data instead of real database */
    private boolean useMockData;
    /** Active database connection */
    private Connection connection;
    /** Provider for mock data when database is unavailable */
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
     * @param order      the Order object to be created
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

            // NEW: Validate inventory before processing
            if (!validateInventoryForOrder(orderItems)) {
                throw new SQLException("Insufficient inventory for this order");
            }

            // Get next available order ID
            int orderId = getNextOrderId();
            if (orderId == -1) {
                throw new SQLException("Failed to generate order ID");
            }

            // Insert order with explicit orderID
            String orderQuery = "INSERT INTO orders (orderid, timeoforder, customerid, employeeid, totalcost, orderweek) VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement pstmt = connection.prepareStatement(orderQuery)) {
                pstmt.setInt(1, orderId);
                pstmt.setTimestamp(2, order.getTimeOfOrder());
                pstmt.setObject(3, order.getCustomerID());
                pstmt.setInt(4, order.getEmployeeID());
                pstmt.setDouble(5, order.getTotalCost());
                pstmt.setInt(6, order.getOrderWeek());

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Failed to insert order");
                }
                
                // Update the order object with the generated ID
                order.setOrderID(orderId);
            }

            // Insert order items
            String itemQuery = "INSERT INTO orderitems (orderitemid, orderid, menuitemid, quantity) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(itemQuery)) {
                for (OrderItem item : orderItems) {
                    int orderItemId = getNextOrderItemId();
                    if (orderItemId == -1) {
                        throw new SQLException("Failed to generate order item ID");
                    }
                    
                    pstmt.setInt(1, orderItemId);
                    pstmt.setInt(2, orderId);
                    pstmt.setInt(3, item.getMenuItemID());
                    pstmt.setInt(4, item.getQuantity());
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            }

            // NEW: Update inventory after successful order creation
            if (!updateInventoryForOrder(orderItems)) {
                throw new SQLException("Failed to update inventory");
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

        // First, get the next available menu item ID
        int nextId = getNextMenuItemId();
        if (nextId == -1) {
            System.err.println("Error: Could not generate next menu item ID");
            return false;
        }

        String query = "INSERT INTO menuitems (menuitemid, drinkcategory, menuitemname, price) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, nextId);
            pstmt.setString(2, item.getDrinkCategory());
            pstmt.setString(3, item.getMenuItemName());
            pstmt.setDouble(4, item.getPrice());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error adding menu item: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gets the next available menu item ID by finding the maximum existing ID and
     * adding 1.
     * 
     * @return the next available ID, or -1 if there was an error
     * @author assistant
     */
    private int getNextMenuItemId() {
        String query = "SELECT COALESCE(MAX(menuitemid), 0) + 1 FROM menuitems";

        try (PreparedStatement pstmt = connection.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 1; // If no records exist, start with ID 1

        } catch (SQLException e) {
            System.err.println("Error getting next menu item ID: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Updates the price of an existing menu item.
     * 
     * @param itemId   the ID of the menu item to update
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

        // First, get the next available inventory item ID
        int nextId = getNextInventoryItemId();
        if (nextId == -1) {
            System.err.println("Error: Could not generate next inventory item ID");
            return false;
        }

        String query = "INSERT INTO inventory (ingredientid, ingredientname, ingredientcount) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, nextId);
            pstmt.setString(2, item.getIngredientName());
            pstmt.setInt(3, item.getIngredientCount());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error adding inventory item: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gets the next available inventory item ID by finding the maximum existing ID
     * and adding 1.
     * 
     * @return the next available ID, or -1 if there was an error
     * @author assistant
     */
    private int getNextInventoryItemId() {
        String query = "SELECT COALESCE(MAX(ingredientid), 0) + 1 FROM inventory";

        try (PreparedStatement pstmt = connection.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 1; // If no records exist, start with ID 1

        } catch (SQLException e) {
            System.err.println("Error getting next inventory item ID: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Gets the next available order ID by finding the maximum existing ID and adding 1.
     * 
     * @return the next available ID, or -1 if there was an error
     * @author harry
     */
    private int getNextOrderId() {
        String query = "SELECT COALESCE(MAX(orderid), 0) + 1 FROM orders";

        try (PreparedStatement pstmt = connection.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 1; // If no records exist, start with ID 1

        } catch (SQLException e) {
            System.err.println("Error getting next order ID: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Gets the next available order item ID by finding the maximum existing ID and adding 1.
     * 
     * @return the next available ID, or -1 if there was an error
     * @author harry
     */
    private int getNextOrderItemId() {
        String query = "SELECT COALESCE(MAX(orderitemid), 0) + 1 FROM orderitems";

        try (PreparedStatement pstmt = connection.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 1; // If no records exist, start with ID 1

        } catch (SQLException e) {
            System.err.println("Error getting next order item ID: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Updates the quantity of an existing inventory item.
     * 
     * @param itemId      the ID of the inventory item to update
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

        // First, get the next available employee ID
        int nextId = getNextEmployeeId();
        if (nextId == -1) {
            System.err.println("Error: Could not generate next employee ID");
            return false;
        }

        String query = "INSERT INTO employees (employeeid, employeename, employeerole, hoursworked) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, nextId);
            pstmt.setString(2, employee.getEmployeeName());
            pstmt.setString(3, employee.getEmployeeRole());
            pstmt.setInt(4, employee.getHoursWorked());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error adding employee: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gets the next available employee ID by finding the maximum existing ID and
     * adding 1.
     * 
     * @return the next available ID, or -1 if there was an error
     * @author assistant
     */
    private int getNextEmployeeId() {
        String query = "SELECT COALESCE(MAX(employeeid), 0) + 1 FROM employees";

        try (PreparedStatement pstmt = connection.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 1; // If no records exist, start with ID 1

        } catch (SQLException e) {
            System.err.println("Error getting next employee ID: " + e.getMessage());
            return -1;
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
     * @param endDate   the end date for sales calculation
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

    /**
     * Validates if there's sufficient inventory for an order before processing.
     * 
     * @param orderItems List of OrderItem objects to validate
     * @return true if sufficient inventory exists, false otherwise
     * @author harry
     */
    public boolean validateInventoryForOrder(List<OrderItem> orderItems) {
        if (useMockData) {
            return mockProvider.validateInventoryForOrder(orderItems);
        }

        try {
            for (OrderItem orderItem : orderItems) {
                // Get all ingredients needed for this menu item
                String checkQuery = "SELECT i.ingredientID, i.ingredientCount, mi.ingredientQty " +
                                   "FROM inventory i " +
                                   "INNER JOIN MenuItemIngredients mi ON i.ingredientID = mi.ingredientID " +
                                   "WHERE mi.menuItemID = ?";
                
                try (PreparedStatement stmt = connection.prepareStatement(checkQuery)) {
                    stmt.setInt(1, orderItem.getMenuItemID());
                    
                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            int available = rs.getInt("ingredientCount");
                            int requiredPerDrink = rs.getInt("ingredientQty");
                            int totalRequired = requiredPerDrink * orderItem.getQuantity();
                            
                            if (available < totalRequired) {
                                System.err.println("Insufficient inventory for ingredient ID: " + rs.getInt("ingredientID") + 
                                                 " (Available: " + available + ", Required: " + totalRequired + ")");
                                return false;
                            }
                        }
                    }
                }
            }
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error validating inventory: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates inventory by decrementing ingredient quantities when an order is placed.
     * 
     * @param orderItems List of OrderItem objects from the completed order
     * @return true if inventory update was successful, false otherwise
     * @author harry
     */
    public boolean updateInventoryForOrder(List<OrderItem> orderItems) {
        if (useMockData) {
            return mockProvider.updateInventoryForOrder(orderItems);
        }

        try {
            for (OrderItem orderItem : orderItems) {
                // Get required ingredients for this menu item from MenuItemIngredients table
                String ingredientQuery = "SELECT ingredientID, ingredientQty FROM MenuItemIngredients WHERE menuItemID = ?";
                
                try (PreparedStatement ingredientStmt = connection.prepareStatement(ingredientQuery)) {
                    ingredientStmt.setInt(1, orderItem.getMenuItemID());
                    
                    try (ResultSet rs = ingredientStmt.executeQuery()) {
                        while (rs.next()) {
                            int ingredientID = rs.getInt("ingredientID");
                            int requiredPerDrink = rs.getInt("ingredientQty");
                            int totalNeeded = requiredPerDrink * orderItem.getQuantity();
                            
                            // Update inventory by decrementing
                            String updateQuery = "UPDATE inventory SET ingredientCount = ingredientCount - ? WHERE ingredientID = ?";
                            try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                                updateStmt.setInt(1, totalNeeded);
                                updateStmt.setInt(2, ingredientID);
                                
                                int rowsAffected = updateStmt.executeUpdate();
                                if (rowsAffected == 0) {
                                    throw new SQLException("Ingredient not found: " + ingredientID);
                                }
                                
                                System.out.println("Updated inventory: Ingredient " + ingredientID + 
                                                 " decremented by " + totalNeeded);
                            }
                        }
                    }
                }
            }
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error updating inventory: " + e.getMessage());
            return false;
        }
    }
}
