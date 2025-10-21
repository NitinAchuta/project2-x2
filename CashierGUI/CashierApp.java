package CashierGUI;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Stream;
import java.nio.file.*;

/**
 * Main cashier application for handling food orders and menu items.
 * Provides a graphical interface for:
 * - Displaying available menu items
 * - Managing customer orders
 * - Processing transactions
 * - Tracking order totals
 */
public class CashierApp {

    /** Loads databse enviroment values from .env file. */
    private static final Map<String, String> env = loadEnvFile(".env");
    /** URL used to connet to the databse. */
    private static final String DB_URL = env.get("DB_URL");
    /** Username for databse conection. */
    private static final String DB_USER = env.get("DB_USER");
    /** Password for databse access. */
    private static final String DB_PASS = env.get("DB_PASS");

    /** List showing menu ittems. */
    private JList<String> menuList;
    /** Model for displaying current order items. */
    private DefaultListModel<String> orderListModel;
    /** Label to show total ammount. */
    private JLabel totalLabel;
    /** Field for enterig customer name. */
    private JTextField customerNameField;
    /** Dropdwon box for selecting quantitty. */
    private JComboBox<Integer> quantityBox;

    /** Holds menu item data retrived from DB. */
    private List<MenuItem> menuItems = new ArrayList<>();
    /** Holds items currently in an order. */
    private List<OrderItem> currentOrder = new ArrayList<>();
    /** Keeps track of total cost. */
    private double totalCost = 0.0;

    /**
     * Constructs the cashier app and inits GUI.
     */
    public CashierApp() {
        loadMenuItems();
        createAndShowGUI();
    }

    /**
     * Creates and shows the GUI layout for cashier.
     */
    private void createAndShowGUI() {
        JFrame frame = new JFrame("Cashier - Order System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 500);

        // Left Panel: Menu Items
        JPanel leftPanel = new JPanel(new BorderLayout());
        JLabel menuLabel = new JLabel("Menu Items", JLabel.CENTER);
        menuList = new JList<>(menuItems.stream().map(item -> item.name + " - $" + item.price).toArray(String[]::new));
        JScrollPane menuScrollPane = new JScrollPane(menuList);
        JButton addButton = new JButton("Add to Order");
        addButton.addActionListener(_ -> addToOrder());
        leftPanel.add(menuLabel, BorderLayout.NORTH);
        leftPanel.add(menuScrollPane, BorderLayout.CENTER);
        leftPanel.add(addButton, BorderLayout.SOUTH);

        // Center Panel: Current Order
        JPanel centerPanel = new JPanel(new BorderLayout());
        JLabel orderLabel = new JLabel("Current Order", JLabel.CENTER);
        orderListModel = new DefaultListModel<>();
        JList<String> orderList = new JList<>(orderListModel);
        JScrollPane orderScrollPane = new JScrollPane(orderList);
        totalLabel = new JLabel("Total: $0.00", JLabel.RIGHT);
        JPanel buttonPanel = new JPanel();
        JButton clearButton = new JButton("Clear Order");
        clearButton.addActionListener(_ -> clearOrder());
        JButton submitButton = new JButton("Submit Order");
        submitButton.addActionListener(_ -> submitOrder());
        buttonPanel.add(clearButton);
        buttonPanel.add(submitButton);
        centerPanel.add(orderLabel, BorderLayout.NORTH);
        centerPanel.add(orderScrollPane, BorderLayout.CENTER);
        centerPanel.add(totalLabel, BorderLayout.SOUTH);
        centerPanel.add(buttonPanel, BorderLayout.PAGE_END);

        // Right Panel: Quantity and Customer Name
        JPanel rightPanel = new JPanel(new GridLayout(0, 1));
        JLabel quantityLabel = new JLabel("Quantity", JLabel.CENTER);
        quantityBox = new JComboBox<>();
        for (int i = 1; i <= 10; i++)
            quantityBox.addItem(i);
        JLabel customerLabel = new JLabel("Customer Name", JLabel.CENTER);
        customerNameField = new JTextField();
        rightPanel.add(quantityLabel);
        rightPanel.add(quantityBox);
        rightPanel.add(customerLabel);
        rightPanel.add(customerNameField);

        // Layout setup
        frame.setLayout(new GridLayout(1, 3));
        frame.add(leftPanel);
        frame.add(centerPanel);
        frame.add(rightPanel);
        frame.setVisible(true);
    }

    /**
     * Loads menu items from databse and fills list.
     */
    private void loadMenuItems() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt
                        .executeQuery("SELECT menuitemid, menuitemname, price FROM menuitems ORDER BY menuitemid")) {

            while (rs.next()) {
                menuItems.add(new MenuItem(
                        rs.getInt("menuitemid"),
                        rs.getString("menuitemname"),
                        rs.getDouble("price")));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to load menu items: " + e.getMessage(), "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Adds selected menu item to the current order list.
     */
    private void addToOrder() {
        int selectedIndex = menuList.getSelectedIndex();
        if (selectedIndex < 0) {
            JOptionPane.showMessageDialog(null, "Please select a menu item", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        MenuItem item = menuItems.get(selectedIndex);
        int quantity = (int) quantityBox.getSelectedItem();

        OrderItem orderItem = new OrderItem(item, quantity);
        currentOrder.add(orderItem);

        orderListModel.addElement(quantity + "x " + item.name + " - $" + (item.price * quantity));
        totalCost += item.price * quantity;
        totalLabel.setText("Total: $" + String.format("%.2f", totalCost));
    }

    /**
     * Clears all items from the current order.
     */
    private void clearOrder() {
        currentOrder.clear();
        orderListModel.clear();
        totalCost = 0.0;
        totalLabel.setText("Total: $0.00");
        customerNameField.setText("");
    }

    /**
     * Submits the current order to database and resets the UI.
     * Handles the following operations:
     * - Validates order is not empty
     * - Creates new order record in database
     * - Adds all order items to orderitems table
     * - Uses transaction to ensure data integrity
     * - Shows confirmation message on success
     * - Clears the order form after successful submission
     */
    private void submitOrder() {
        if (currentOrder.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Order is empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            conn.setAutoCommit(false);

            int orderID = getNextID(conn, "orders", "orderid");

            String orderSQL = "INSERT INTO orders (orderid, timeoforder, customerid, employeeid, totalcost, orderweek) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(orderSQL)) {
                pstmt.setInt(1, orderID);
                pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                pstmt.setNull(3, Types.INTEGER);
                pstmt.setInt(4, 1);
                pstmt.setDouble(5, totalCost);
                pstmt.setInt(6, getCurrentWeek());
                pstmt.executeUpdate();
            }

            int orderItemID = getNextID(conn, "orderitems", "orderitemid");
            String itemSQL = "INSERT INTO orderitems (orderitemid, orderid, menuitemid, quantity) VALUES (?, ?, ?, ?)";

            try (PreparedStatement pstmt = conn.prepareStatement(itemSQL)) {
                for (OrderItem item : currentOrder) {
                    pstmt.setInt(1, orderItemID++);
                    pstmt.setInt(2, orderID);
                    pstmt.setInt(3, item.menuItem.id);
                    pstmt.setInt(4, item.quantity);
                    pstmt.executeUpdate();
                }
            }

            conn.commit();
            JOptionPane.showMessageDialog(null, "Order #" + orderID + " submitted succesfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            clearOrder();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to submit order: " + e.getMessage(), "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Gets the next available ID for a given database table.
     * Calculates by finding the maximum current ID and adding 1.
     * 
     * @param conn the database Connection to use
     * @param table the name of the table to query
     * @param idColumn the name of the ID column in the table
     * @return the next available ID number
     * @throws SQLException if there's an error accessing the database
     */
    private int getNextID(Connection conn, String table, String idColumn) throws SQLException {
        String sql = "SELECT COALESCE(MAX(" + idColumn + "), 0) + 1 FROM " + table;
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next())
                return rs.getInt(1);
        }
        return 1;
    }

    /**
     * Retrieves the current week number from calendar.
     * Week numbers are based on the default calendar system
     * and range from 1 to 52/53.
     * 
     * @return the current week of year (1-53)
     */
    private int getCurrentWeek() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        return cal.get(java.util.Calendar.WEEK_OF_YEAR);
    }

    /**
     * Starts the cashier app.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(CashierApp::new);
    }

    /**
     * Represents a menu item entity with its ID, name and price.
     * Used to store and display individual menu items available for ordering.
     */
    static class MenuItem {
        int id;
        String name;
        double price;

        /** Creates a new menu item. */
        MenuItem(int id, String name, double price) {
            this.id = id;
            this.name = name;
            this.price = price;
        }
    }

    /**
     * Represents an item in a customer's order with its quantity.
     * Associates a MenuItem with the quantity ordered.
     * Used to track items in the current order before submission.
     */
    static class OrderItem {
        MenuItem menuItem;
        int quantity;

        /** Creates a new order item record. */
        OrderItem(MenuItem menuItem, int quantity) {
            this.menuItem = menuItem;
            this.quantity = quantity;
        }
    }

    /**
     * Loads environment variables from a file and returns them as a map.
     * Skips empty lines and comments (lines starting with #).
     * Each line should be in the format KEY=VALUE.
     *
     * @param filePath path to the environment file
     * @return Map containing the environment variables
     */
    private static Map<String, String> loadEnvFile(String filePath) {
        Map<String, String> env = new HashMap<>();
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            System.err.println("Enviroment file not found: " + filePath);
            return env;
        }

        try (Stream<String> lines = Files.lines(path)) {
            lines.map(String::trim)
                    .filter(line -> !line.isEmpty() && !line.startsWith("#") && line.contains("="))
                    .forEach(line -> {
                        String[] parts = line.split("=", 2);
                        env.put(parts[0].trim(), parts[1].trim());
                    });
        } catch (IOException e) {
            System.err.println("Error reading enviroment file: " + e.getMessage());
        }
        return env;
    }
}
