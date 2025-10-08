import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.HashMap;
import java.util.Map;

public class CashierApp extends Application {

    // Database connection details
    private static final String DB_URL = System.getenv("DB_URL");
    private static final String DB_USER = System.getenv("DB_USER");
    private static final String DB_PASS = System.getenv("DB_PASS");

    // UI Components
    private ListView<String> menuListView;
    private ListView<String> orderListView;
    private Label totalLabel;
    private TextField customerNameField;
    private ComboBox<Integer> quantityBox;

    // Data
    private List<MenuItem> menuItems = new ArrayList<>();
    private List<OrderItem> currentOrder = new ArrayList<>();
    private double totalCost = 0.0;

    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.setTitle("Cashier - Order System");

            // Load menu items from database
            loadMenuItems();

            // Create UI
            BorderPane root = new BorderPane();
            root.setPadding(new Insets(10));

            // Left: Menu Items
            VBox leftPanel = new VBox(10);
            leftPanel.setPadding(new Insets(10));
            Label menuLabel = new Label("Menu Items");
            menuLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
            menuListView = new ListView<>();
            menuListView.setPrefHeight(400);
            for (MenuItem item : menuItems) {
                menuListView.getItems().add(item.name + " - $" + item.price);
            }
            Button addButton = new Button("Add to Order");
            addButton.setOnAction(e -> addToOrder());
            leftPanel.getChildren().addAll(menuLabel, menuListView, addButton);

            // Center: Current Order
            VBox centerPanel = new VBox(10);
            centerPanel.setPadding(new Insets(10));
            Label orderLabel = new Label("Current Order");
            orderLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            HBox customerBox = new HBox(10);
            customerBox.getChildren().addAll(new Label("Customer:"), customerNameField = new TextField());
            customerNameField.setPromptText("Optional");

            orderListView = new ListView<>();
            orderListView.setPrefHeight(300);

            HBox totalBox = new HBox(10);
            totalBox.setAlignment(Pos.CENTER_RIGHT);
            totalLabel = new Label("Total: $0.00");
            totalLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
            totalBox.getChildren().add(totalLabel);

            HBox buttonBox = new HBox(10);
            buttonBox.setAlignment(Pos.CENTER);
            Button clearButton = new Button("Clear Order");
            clearButton.setOnAction(e -> clearOrder());
            Button submitButton = new Button("Submit Order");
            submitButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
            submitButton.setOnAction(e -> submitOrder());
            buttonBox.getChildren().addAll(clearButton, submitButton);

            centerPanel.getChildren().addAll(orderLabel, customerBox, orderListView, totalBox, buttonBox);

            // Right: Customization (simple quantity selector)
            VBox rightPanel = new VBox(10);
            rightPanel.setPadding(new Insets(10));
            Label custLabel = new Label("Quantity");
            custLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
            quantityBox = new ComboBox<>();
            for (int i = 1; i <= 10; i++) {
                quantityBox.getItems().add(i);
            }
            quantityBox.setValue(1);
            rightPanel.getChildren().addAll(custLabel, quantityBox);

            // Layout
            root.setLeft(leftPanel);
            root.setCenter(centerPanel);
            root.setRight(rightPanel);

            Scene scene = new Scene(root, 900, 500);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Startup Error");
            alert.setHeaderText("Application failed to start");
            alert.setContentText("Error: " + e.getMessage() + "\n\nCheck console for details.");
            alert.showAndWait();
            System.exit(1);
        }
    }

    private void loadMenuItems() {
        System.out.println("Connecting to database...");
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt
                        .executeQuery("SELECT menuitemid, menuitemname, price FROM menuitems ORDER BY menuitemid")) {

            System.out.println("Database connected! Reading menu items...");
            while (rs.next()) {
                menuItems.add(new MenuItem(
                        rs.getInt("menuitemid"),
                        rs.getString("menuitemname"),
                        rs.getDouble("price")));
            }
            System.out.println("Loaded " + menuItems.size() + " menu items");
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load menu items: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addToOrder() {
        int selectedIndex = menuListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0) {
            showAlert("Error", "Please select a menu item");
            return;
        }

        MenuItem item = menuItems.get(selectedIndex);
        int quantity = quantityBox.getValue();

        OrderItem orderItem = new OrderItem(item, quantity);
        currentOrder.add(orderItem);

        orderListView.getItems().add(quantity + "x " + item.name + " - $" + (item.price * quantity));

        totalCost += item.price * quantity;
        totalLabel.setText("Total: $" + String.format("%.2f", totalCost));
    }

    private void clearOrder() {
        currentOrder.clear();
        orderListView.getItems().clear();
        totalCost = 0.0;
        totalLabel.setText("Total: $0.00");
        customerNameField.clear();
    }

    private void submitOrder() {
        if (currentOrder.isEmpty()) {
            showAlert("Error", "Order is empty");
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
            String itemSQL = "INSERT INTO orderitems (orderitemid, orderid, menuitemid, sugarlevel, icelevel, milktype, "
                    +
                    "boba, lycheejelly, grassjelly, pudding, aloevera, redbean, coffeejelly, coconutjelly, " +
                    "chiaseeds, taroballs, mangostars, rainbowjelly, crystalboba, cheesefoam, whippedcream, " +
                    "oreocrumbs, carameldrizzle, matchafoam, strawberrypoppingboba, mangopoppingboba, " +
                    "blueberrypoppingboba, passionfruitpoppingboba, chocolatechips, peanutcrumble, " +
                    "marshmallows, cinnamondust, honey, mintleaves, quantity) " +
                    "VALUES (?, ?, ?, 50, 2, 'Regular', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, ?)";

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
            showAlert("Success", "Order #" + orderID + " submitted successfully!");
            clearOrder();

        } catch (SQLException e) {
            showAlert("Database Error", "Failed to submit order: " + e.getMessage());
            e.printStackTrace();
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        System.out.println("Starting CashierApp...");
        System.out.println("Database: " + DB_URL);
        System.out.println("User: " + DB_USER);
        try {
            launch(args);
        } catch (Exception e) {
            System.err.println("ERROR launching app: " + e.getMessage());
            e.printStackTrace();
        }
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
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            lines.filter(line -> line.contains("=") && !line.startsWith("#"))
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
