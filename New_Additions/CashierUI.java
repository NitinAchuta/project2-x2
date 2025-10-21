package New_Additions;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * CashierUI provids an interface for cashiers to proces orders.
 * Has modular desing and seperation of concerns.
 */
public class CashierUI extends JFrame {

    /** Manages all databse related operation. */
    private DatabaseManager dbManager;

    /** List of menu items shown to the cashier. */
    private JList<String> menuList;

    /** Model that holds items currently in order. */
    private DefaultListModel<String> orderListModel;

    /** Shows total cost of order. */
    private JLabel totalLabel;

    /** Field for customer name input. */
    private JTextField customerNameField;

    /** Dropdown for selecting item quantitie. */
    private JComboBox<Integer> quantityBox;

    /** Holds all menu items retrived from DB. */
    private List<MenuItem> menuItems = new ArrayList<>();

    /** Keeps track of current order items dispalyed. */
    private List<OrderItemDisplay> currentOrder = new ArrayList<>();

    /** Stores total cost of curent order. */
    private double totalCost = 0.0;

    /** Constrctor initializes and builds UI. */
    public CashierUI() {
        initializeServices();
        loadMenuItems();
        createAndShowGUI();
    }

    /** Intializ the database manager and check mode. */
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

    /** Builds the full UI layout and event listeners. */
    private void createAndShowGUI() {
        setTitle("Cashier - Order System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        /** Creates main container panel. */
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        /** Adds header at top of window. */
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        /** Creates center section with 3 panels. */
        JPanel contentPanel = new JPanel(new GridLayout(1, 3, 10, 10));

        /** Left panel with menu item list. */
        JPanel leftPanel = createMenuPanel();
        contentPanel.add(leftPanel);

        /** Center panel showing order details. */
        JPanel centerPanel = createOrderPanel();
        contentPanel.add(centerPanel);

        /** Right panel for customer info. */
        JPanel rightPanel = createCustomerPanel();
        contentPanel.add(rightPanel);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        /** Adds bottom status section. */
        JPanel statusPanel = createStatusPanel();
        mainPanel.add(statusPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);

        /** Handle closing window event. */
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

    /** Create header panel with title and back button. */
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

    /** Builds the panel that shows menu items list. */
    private JPanel createMenuPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Menu Items"));

        /** Display menu item names and prices. */
        menuList = new JList<>(menuItems.stream()
                .map(item -> item.getMenuItemName() + " - $" + String.format("%.2f", item.getPrice()))
                .toArray(String[]::new));
        menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        menuList.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane menuScrollPane = new JScrollPane(menuList);
        leftPanel.add(menuScrollPane, BorderLayout.CENTER);

        /** Button to add item to order. */
        JButton addButton = new JButton("Add to Order");
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.addActionListener(e -> addToOrder());
        leftPanel.add(addButton, BorderLayout.SOUTH);

        return leftPanel;
    }

    /** Creates order list display and total section. */
    private JPanel createOrderPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Current Order"));

        /** Shows items currently in the order. */
        orderListModel = new DefaultListModel<>();
        JList<String> orderList = new JList<>(orderListModel);
        orderList.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane orderScrollPane = new JScrollPane(orderList);
        centerPanel.add(orderScrollPane, BorderLayout.CENTER);

        /** Adds total label and buttons below list. */
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

    /** Makes right-side panel for custmer info. */
    private JPanel createCustomerPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Order Details"));

        /** Quantity selector combo box. */
        JPanel quantityPanel = new JPanel(new BorderLayout());
        quantityPanel.add(new JLabel("Quantity:"), BorderLayout.NORTH);
        quantityBox = new JComboBox<>();
        for (int i = 1; i <= 10; i++) {
            quantityBox.addItem(i);
        }
        quantityBox.setFont(new Font("Arial", Font.PLAIN, 14));
        quantityPanel.add(quantityBox, BorderLayout.CENTER);

        /** Customer name input textfield. */
        JPanel customerPanel = new JPanel(new BorderLayout());
        customerPanel.add(new JLabel("Customer Name (Optional):"), BorderLayout.NORTH);
        customerNameField = new JTextField();
        customerNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        customerPanel.add(customerNameField, BorderLayout.CENTER);

        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(quantityPanel);
        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(customerPanel);
        rightPanel.add(Box.createVerticalGlue());

        return rightPanel;
    }

    /** Create panel showing database status message. */
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

    /** Loads all menu items from database. */
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

    /** Adds selected menu item to curent order. */
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

    /** Clears the order and reset input feilds. */
    private void clearOrder() {
        currentOrder.clear();
        orderListModel.clear();
        totalCost = 0.0;
        updateTotalDisplay();
        customerNameField.setText("");
    }

    /** Submits the current order to databse. */
    private void submitOrder() {
        if (currentOrder.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Order is empty",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            /** Converts displayed items to database order items. */
            List<OrderItem> orderItems = new ArrayList<>();
            for (OrderItemDisplay displayItem : currentOrder) {
                OrderItem orderItem = new OrderItem();
                orderItem.setMenuItemID(displayItem.getMenuItem().getMenuItemID());
                orderItem.setQuantity(displayItem.getQuantity());
                orderItems.add(orderItem);
            }

            /** Creates and populate new order object. */
            Order order = new Order();
            order.setTimeOfOrder(new Timestamp(System.currentTimeMillis()));
            order.setCustomerID(null);
            order.setEmployeeID(1);
            order.setTotalCost(totalCost);
            order.setOrderWeek(getCurrentWeek());

            /** Sends order to database. */
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
                        "Failed to submit order. Please check inventory levels.",
                        "Order Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            // Check if it's an inventory-related error
            if (e.getMessage() != null && e.getMessage().contains("Insufficient inventory")) {
                JOptionPane.showMessageDialog(this,
                        "Cannot fulfill this order due to insufficient inventory.\n" +
                        "Please check stock levels and try again.",
                        "Inventory Error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to submit order: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /** Updates total label display on screen. */
    private void updateTotalDisplay() {
        totalLabel.setText("Total: $" + String.format("%.2f", totalCost));
    }

    /** Returns current week num from calendar. */
    private int getCurrentWeek() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    /** Helper class to show order items in UI. */
    private static class OrderItemDisplay {
        private final MenuItem menuItem;
        private final int quantity;

        /** Simple constructor for holding menu item data. */
        public OrderItemDisplay(MenuItem menuItem, int quantity) {
            this.menuItem = menuItem;
            this.quantity = quantity;
        }

        /** Gets the menu item object. */
        public MenuItem getMenuItem() {
            return menuItem;
        }

        /** Gets the quantity of this item. */
        public int getQuantity() {
            return quantity;
        }
    }
}
