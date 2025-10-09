import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CashierApp {

    // Database connection details
    private static final Map<String, String> env = loadEnvFile(".env");
    private static final String DB_URL = env.get("DB_URL");
    private static final String DB_USER = env.get("DB_USER");
    private static final String DB_PASS = env.get("DB_PASS");

    // UI Components
    private JList<String> menuList;
    private DefaultListModel<String> orderListModel;
    private JLabel totalLabel;
    private JTextField customerNameField;
    private JComboBox<Integer> quantityBox;

    // Data
    private List<MenuItem> menuItems = new ArrayList<>();
    private List<OrderItem> currentOrder = new ArrayList<>();
    private double totalCost = 0.0;

    public CashierApp() {
        loadMenuItems();
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Cashier - Order System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 500);

        // Left Panel: Menu Items
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        JLabel menuLabel = new JLabel("Menu Items", JLabel.CENTER);
        menuList = new JList<>(menuItems.stream().map(item -> item.name + " - $" + item.price).toArray(String[]::new));
        JScrollPane menuScrollPane = new JScrollPane(menuList);
        JButton addButton = new JButton("Add to Order");
        addButton.addActionListener(_ -> addToOrder());
        leftPanel.add(menuLabel, BorderLayout.NORTH);
        leftPanel.add(menuScrollPane, BorderLayout.CENTER);
        leftPanel.add(addButton, BorderLayout.SOUTH);

        // Center Panel: Current Order
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
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
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(0, 1));
        JLabel quantityLabel = new JLabel("Quantity", JLabel.CENTER);
        quantityBox = new JComboBox<>();
        for (int i = 1; i <= 10; i++) {
            quantityBox.addItem(i);
        }
        JLabel customerLabel = new JLabel("Customer Name", JLabel.CENTER);
        customerNameField = new JTextField();
        rightPanel.add(quantityLabel);
        rightPanel.add(quantityBox);
        rightPanel.add(customerLabel);
        rightPanel.add(customerNameField);

        // Layout
        frame.setLayout(new GridLayout(1, 3));
        frame.add(leftPanel);
        frame.add(centerPanel);
        frame.add(rightPanel);

        frame.setVisible(true);
    }

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

    private void clearOrder() {
        currentOrder.clear();
        orderListModel.clear();
        totalCost = 0.0;
        totalLabel.setText("Total: $0.00");
        customerNameField.setText("");
    }

    private void submitOrder() {
        if (currentOrder.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Order is empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            conn.setAutoCommit(false);

            // Get next order ID
            int orderID = getNextID(conn, "orders", "orderid");

            // Insert order
            String orderSQL = "INSERT INTO orders (orderid, timeoforder, customerid, employeeid, totalcost, orderweek) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(orderSQL)) {
                pstmt.setInt(1, orderID);
                pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                pstmt.setNull(3, Types.INTEGER); // No customer ID for walk-in
                pstmt.setInt(4, 1); // Employee ID = 1 (cashier)
                pstmt.setDouble(5, totalCost);
                pstmt.setInt(6, getCurrentWeek());
                pstmt.executeUpdate();
            }

            // Insert order items
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
            JOptionPane.showMessageDialog(null, "Order #" + orderID + " submitted successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            clearOrder();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to submit order: " + e.getMessage(), "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private int getNextID(Connection conn, String table, String idColumn) throws SQLException {
        String sql = "SELECT COALESCE(MAX(" + idColumn + "), 0) + 1 FROM " + table;
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 1;
    }

    private int getCurrentWeek() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        return cal.get(java.util.Calendar.WEEK_OF_YEAR);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CashierApp::new);
    }

    // Simple data classes
    static class MenuItem {
        int id;
        String name;
        double price;

        MenuItem(int id, String name, double price) {
            this.id = id;
            this.name = name;
            this.price = price;
        }
    }

    static class OrderItem {
        MenuItem menuItem;
        int quantity;

        OrderItem(MenuItem menuItem, int quantity) {
            this.menuItem = menuItem;
            this.quantity = quantity;
        }
    }

    private static Map<String, String> loadEnvFile(String filePath) {
        Map<String, String> env = new HashMap<>();

        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            System.err.println("Environment file not found: " + filePath);
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
            System.err.println("Error reading environment file: " + e.getMessage());
        }

        return env;
    }

}
