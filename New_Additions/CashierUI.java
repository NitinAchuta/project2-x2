package New_Additions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * CashierUI provides the interface for cashiers to process customer orders
 * Modular design with separation of concerns
 */
public class CashierUI extends JFrame {

    // Services
    private DatabaseManager dbManager;

    // UI Components
    private JList<String> menuList;
    private DefaultListModel<String> orderListModel;
    private JLabel totalLabel;
    private JTextField customerNameField;
    private JComboBox<Integer> quantityBox;

    // Data
    private List<MenuItem> menuItems = new ArrayList<>();
    private List<OrderItemDisplay> currentOrder = new ArrayList<>();
    private double totalCost = 0.0;

    public CashierUI() {
        initializeServices();
        loadMenuItems();
        createAndShowGUI();
    }

    private void initializeServices() {
        try {
            dbManager = new DatabaseManager();
            if (dbManager.isUsingMockData()) {
                System.out.println("Cashier UI: Using mock data mode");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Failed to initialize database: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void createAndShowGUI() {
        setTitle("Cashier - Order System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        // Create main panel with better layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Content panel
        JPanel contentPanel = new JPanel(new GridLayout(1, 3, 10, 10));

        // Left Panel: Menu Items
        JPanel leftPanel = createMenuPanel();
        contentPanel.add(leftPanel);

        // Center Panel: Current Order
        JPanel centerPanel = createOrderPanel();
        contentPanel.add(centerPanel);

        // Right Panel: Quantity and Customer Info
        JPanel rightPanel = createCustomerPanel();
        contentPanel.add(rightPanel);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Status panel
        JPanel statusPanel = createStatusPanel();
        mainPanel.add(statusPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);

        // Add back to landing page option
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(
                        CashierUI.this,
                        "Return to main menu?",
                        "Confirm Exit",
                        JOptionPane.YES_NO_OPTION);

                if (option == JOptionPane.YES_OPTION) {
                    new LandingPage();
                    dispose();
                } else {
                    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JLabel titleLabel = new JLabel("Cashier Order System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton backButton = new JButton("← Back to Menu");
        backButton.addActionListener(e -> {
            new LandingPage();
            dispose();
        });

        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        return headerPanel;
    }

    private JPanel createMenuPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Menu Items"));

        // Menu list
        menuList = new JList<>(menuItems.stream()
                .map(item -> item.getMenuItemName() + " - $" + String.format("%.2f", item.getPrice()))
                .toArray(String[]::new));
        menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        menuList.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane menuScrollPane = new JScrollPane(menuList);

        leftPanel.add(menuScrollPane, BorderLayout.CENTER);

        // Add button
        JButton addButton = new JButton("Add to Order");
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.addActionListener(e -> addToOrder());
        leftPanel.add(addButton, BorderLayout.SOUTH);

        return leftPanel;
    }

    private JPanel createOrderPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Current Order"));

        // Order list
        orderListModel = new DefaultListModel<>();
        JList<String> orderList = new JList<>(orderListModel);
        orderList.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane orderScrollPane = new JScrollPane(orderList);
        centerPanel.add(orderScrollPane, BorderLayout.CENTER);

        // Total and buttons panel
        JPanel bottomPanel = new JPanel(new BorderLayout());

        totalLabel = new JLabel("Total: $0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        totalLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        bottomPanel.add(totalLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));

        JButton clearButton = new JButton("Clear Order");
        clearButton.setBackground(new Color(255, 200, 200));
        clearButton.addActionListener(e -> clearOrder());

        JButton submitButton = new JButton("Submit Order");
        submitButton.setBackground(new Color(200, 255, 200));
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.addActionListener(e -> submitOrder());

        buttonPanel.add(clearButton);
        buttonPanel.add(submitButton);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        centerPanel.add(bottomPanel, BorderLayout.SOUTH);

        return centerPanel;
    }

    private JPanel createCustomerPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Order Details"));

        // Quantity selection
        JPanel quantityPanel = new JPanel(new BorderLayout());
        quantityPanel.add(new JLabel("Quantity:"), BorderLayout.NORTH);
        quantityBox = new JComboBox<>();
        for (int i = 1; i <= 10; i++) {
            quantityBox.addItem(i);
        }
        quantityBox.setFont(new Font("Arial", Font.PLAIN, 14));
        quantityPanel.add(quantityBox, BorderLayout.CENTER);

        // Customer name
        JPanel customerPanel = new JPanel(new BorderLayout());
        customerPanel.add(new JLabel("Customer Name (Optional):"), BorderLayout.NORTH);
        customerNameField = new JTextField();
        customerNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        customerPanel.add(customerNameField, BorderLayout.CENTER);

        // Add spacing
        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(quantityPanel);
        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(customerPanel);
        rightPanel.add(Box.createVerticalGlue());

        return rightPanel;
    }

    private JPanel createStatusPanel() {
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        String statusText = dbManager.isUsingMockData() ? "Status: Running in demo mode (mock data)"
                : "Status: Connected to database";

        JLabel statusLabel = new JLabel(statusText);
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        statusLabel.setForeground(dbManager.isUsingMockData() ? Color.ORANGE : Color.BLACK);

        statusPanel.add(statusLabel, BorderLayout.WEST);

        return statusPanel;
    }

    private void loadMenuItems() {
        try {
            menuItems = dbManager.getAllMenuItems();
            if (menuItems.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No menu items found in database",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Failed to load menu items: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addToOrder() {
        int selectedIndex = menuList.getSelectedIndex();
        if (selectedIndex < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select a menu item",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        MenuItem item = menuItems.get(selectedIndex);
        int quantity = (int) quantityBox.getSelectedItem();

        OrderItemDisplay orderItem = new OrderItemDisplay(item, quantity);
        currentOrder.add(orderItem);

        orderListModel.addElement(quantity + "x " + item.getMenuItemName() +
                " - $" + String.format("%.2f", item.getPrice() * quantity));

        totalCost += item.getPrice() * quantity;
        updateTotalDisplay();
    }

    private void clearOrder() {
        currentOrder.clear();
        orderListModel.clear();
        totalCost = 0.0;
        updateTotalDisplay();
        customerNameField.setText("");
    }

    private void submitOrder() {
        if (currentOrder.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Order is empty",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Create order object
            Order order = new Order();
            order.setTimeOfOrder(new Timestamp(System.currentTimeMillis()));
            order.setCustomerID(null); // Walk-in customer
            order.setEmployeeID(1); // Default cashier
            order.setTotalCost(totalCost);
            order.setOrderWeek(getCurrentWeek());

            // Create order items
            List<OrderItem> orderItems = new ArrayList<>();
            for (OrderItemDisplay displayItem : currentOrder) {
                OrderItem orderItem = new OrderItem();
                orderItem.setMenuItemID(displayItem.getMenuItem().getMenuItemID());
                orderItem.setQuantity(displayItem.getQuantity());
                orderItems.add(orderItem);
            }

            // Submit to database
            boolean success = dbManager.createOrder(order, orderItems);

            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Order #" + order.getOrderID() + " submitted successfully!\nTotal: $" +
                                String.format("%.2f", totalCost),
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                clearOrder();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to submit order",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Failed to submit order: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTotalDisplay() {
        totalLabel.setText("Total: $" + String.format("%.2f", totalCost));
    }

    private int getCurrentWeek() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * Helper class for displaying order items in the UI
     */
    private static class OrderItemDisplay {
        private final MenuItem menuItem;
        private final int quantity;

        public OrderItemDisplay(MenuItem menuItem, int quantity) {
            this.menuItem = menuItem;
            this.quantity = quantity;
        }

        public MenuItem getMenuItem() {
            return menuItem;
        }

        public int getQuantity() {
            return quantity;
        }
    }
}