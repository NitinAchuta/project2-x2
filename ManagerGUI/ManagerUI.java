import javax.swing.*;
import java.awt.*;
import java.sql.*;

/**
 * ManagerUI provides the interface for managers to handle operations
 * Modular design with improved organization and cleaner UI
 */
public class ManagerUI extends JFrame {
  // Services
  private DatabaseManager dbManager;

  // UI Components
  private JTextArea menuDisplayArea;
  private JTextArea inventoryDisplayArea;
  private JTextArea employeeDisplayArea;
  private JTextArea reportsDisplayArea;

  public ManagerUI() {
      initializeServices();
      createAndShowGUI();
  }

  private void initializeServices() {
      try {
          dbManager = new DatabaseManager();
          if (dbManager.isUsingMockData()) {
              System.out.println("Manager UI: Using mock data mode");
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
      setTitle("Boba Shop Manager System");
      setSize(1200, 800);
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      setLocationRelativeTo(null);

      // Main panel
      JPanel mainPanel = new JPanel(new BorderLayout());
      mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

      // Header panel
      JPanel headerPanel = createHeaderPanel();
      mainPanel.add(headerPanel, BorderLayout.NORTH);

      // Create tabbed pane
      JTabbedPane tabbedPane = new JTabbedPane();
      tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));

      // Add tabs
      tabbedPane.addTab("Menu Management", createMenuManagementTab());
      tabbedPane.addTab("Inventory Management", createInventoryManagementTab());
      tabbedPane.addTab("Employee Management", createEmployeeManagementTab());
      tabbedPane.addTab("Reports & Analytics", createReportsTab());

      mainPanel.add(tabbedPane, BorderLayout.CENTER);

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
                      ManagerUI.this,
                      "Return to main menu?",
                      "Confirm Exit",
                      JOptionPane.YES_NO_OPTION);

              if (option == JOptionPane.YES_OPTION) {
                  cleanup();
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

    JLabel titleLabel = new JLabel("Manager Dashboard");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
    titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

    JButton backButton = new JButton("← Back to Menu");
    backButton.addActionListener(e -> {
        cleanup();
        new LandingPage();
        dispose();
    });

    headerPanel.add(backButton, BorderLayout.WEST);
    headerPanel.add(titleLabel, BorderLayout.CENTER);

    return headerPanel;
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

  private JPanel createMenuManagementTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Title
        JLabel titleLabel = new JLabel("Menu Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Display area
        menuDisplayArea = new JTextArea();
        menuDisplayArea.setEditable(false);
        menuDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(menuDisplayArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        panel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JButton viewButton = createStyledButton("View Menu Items", "View all menu items");
        viewButton.addActionListener(e -> viewMenuItems());
        buttonPanel.add(viewButton);

        JButton addButton = createStyledButton("Add Menu Item", "Add a new menu item");
        addButton.addActionListener(e -> addMenuItem());
        buttonPanel.add(addButton);

        JButton updateButton = createStyledButton("Update Price", "Update menu item price");
        updateButton.addActionListener(e -> updateMenuItemPrice());
        buttonPanel.add(updateButton);

        JButton seasonalButton = createStyledButton("Add Seasonal Item", "Add new seasonal menu item");
        seasonalButton.addActionListener(e -> addSeasonalMenuItem());
        buttonPanel.add(seasonalButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Show initial message
        showInitialMessage(menuDisplayArea, "Menu Management",
                "Manage your boba shop menu items, prices, and categories.");

        return panel;
    }
    private JPanel createInventoryManagementTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Title
        JLabel titleLabel = new JLabel("Inventory Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Display area
        inventoryDisplayArea = new JTextArea();
        inventoryDisplayArea.setEditable(false);
        inventoryDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(inventoryDisplayArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JButton viewButton = createStyledButton("View Inventory", "View all inventory items");
        viewButton.addActionListener(e -> viewInventory());
        buttonPanel.add(viewButton);

        JButton addButton = createStyledButton("Add Item", "Add new inventory item");
        addButton.addActionListener(e -> addInventoryItem());
        buttonPanel.add(addButton);

        JButton updateButton = createStyledButton("Update Quantity", "Update item quantity");
        updateButton.addActionListener(e -> updateInventoryQuantity());
        buttonPanel.add(updateButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Show initial message
        showInitialMessage(inventoryDisplayArea, "Inventory Management",
                "Track and manage ingredients, supplies, and stock levels.");

        return panel;
    }

    private JPanel createEmployeeManagementTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Title
        JLabel titleLabel = new JLabel("Employee Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Display area
        employeeDisplayArea = new JTextArea();
        employeeDisplayArea.setEditable(false);
        employeeDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(employeeDisplayArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JButton viewButton = createStyledButton("View Employees", "View all employees");
        viewButton.addActionListener(e -> viewEmployees());
        buttonPanel.add(viewButton);

        JButton addButton = createStyledButton("Add Employee", "Add new employee");
        addButton.addActionListener(e -> addEmployee());
        buttonPanel.add(addButton);

        JButton updateButton = createStyledButton("Update Employee", "Update employee info");
        updateButton.addActionListener(e -> updateEmployee());
        buttonPanel.add(updateButton);

        JButton deleteButton = createStyledButton("Remove Employee", "Remove employee");
        deleteButton.setBackground(new Color(255, 200, 200));
        deleteButton.addActionListener(e -> removeEmployee());
        buttonPanel.add(deleteButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Show initial message
        showInitialMessage(employeeDisplayArea, "Employee Management",
                "Manage staff information, roles, and work schedules.");

        return panel;
    }
    private JPanel createReportsTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Title
        JLabel titleLabel = new JLabel("Reports & Analytics");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Display area
        reportsDisplayArea = new JTextArea();
        reportsDisplayArea.setEditable(false);
        reportsDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(reportsDisplayArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Button panel with multiple rows
        JPanel buttonPanel = new JPanel(new GridLayout(3, 4, 10, 10));

        // Sales Reports
        buttonPanel.add(createReportButton("Sales Today", this::generateSalesTodayReport));
        buttonPanel.add(createReportButton("Top Sellers", this::generateTopSellersReport));
        buttonPanel.add(createReportButton("Revenue Report", this::generateRevenueReport));
        buttonPanel.add(createReportButton("Product Usage", this::generateUsageReport));

        // Inventory Reports
        buttonPanel.add(createReportButton("Low Stock", this::generateLowStockReport));
        buttonPanel.add(createReportButton("Inventory Value", this::generateInventoryValueReport));
        buttonPanel.add(createReportButton("Restock Report", this::generateRestockReport));
        buttonPanel.add(createReportButton("X-Report", this::generateXReport));

        // Employee Reports
        buttonPanel.add(createReportButton("Staff Hours", this::generateStaffHoursReport));
        buttonPanel.add(createReportButton("Performance", this::generatePerformanceReport));
        buttonPanel.add(createReportButton("Z-Report", this::generateZReport));
        buttonPanel.add(createReportButton("Export Report", this::exportCurrentReport));

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Show initial message
        showInitialMessage(reportsDisplayArea, "Reports & Analytics",
                "Generate business intelligence reports and analytics.");

        return panel;
    }


    private JButton createStyledButton(String text, String tooltip) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 12));
        button.setToolTipText(tooltip);
        button.setBackground(new Color(200, 220, 255));
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        return button;
    }
     private JButton createReportButton(String text, Runnable action) {
        JButton button = createStyledButton(text, "Generate " + text + " report");
        button.addActionListener(e -> action.run());
        return button;
    }

    private void showInitialMessage(JTextArea area, String title, String description) {
        area.setText("✓ System initialized successfully\n");
        area.append("=".repeat(60) + "\n\n");
        area.append(title.toUpperCase() + "\n");
        area.append(description + "\n\n");
        area.append("Use the buttons below to get started.\n");
        if (dbManager.isUsingMockData()) {
            area.append("\n⚠ Note: Running in demo mode with sample data.\n");
        }
    }

    // Menu Management Methods
    private void viewMenuItems() {
        menuDisplayArea.setText("");
        try {
            java.util.List<MenuItem> menuItems = dbManager.getAllMenuItems();

            menuDisplayArea.append("MENU ITEMS\n");
            menuDisplayArea.append("=".repeat(80) + "\n");
            menuDisplayArea.append(String.format("%-12s %-20s %-30s %-10s\n",
                    "Item ID", "Category", "Item Name", "Price"));
            menuDisplayArea.append("-".repeat(80) + "\n");

            for (MenuItem item : menuItems) {
                menuDisplayArea.append(String.format("%-12d %-20s %-30s $%-9.2f\n",
                        item.getMenuItemID(),
                        item.getDrinkCategory(),
                        item.getMenuItemName(),
                        item.getPrice()));
            }

        } catch (Exception e) {
            menuDisplayArea.append("\nERROR: " + e.getMessage() + "\n");
        }
    }
    private void addMenuItem() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        JTextField categoryField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();

        panel.add(new JLabel("Drink Category:"));
        panel.add(categoryField);
        panel.add(new JLabel("Menu Item Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Add New Menu Item", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            menuDisplayArea.setText("");
            try {
                String category = categoryField.getText().trim();
                String name = nameField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());

                if (category.isEmpty() || name.isEmpty()) {
                    throw new IllegalArgumentException("Category and name cannot be empty");
                }

                MenuItem item = new MenuItem(0, category, name, price);
                boolean success = dbManager.addMenuItem(item);

                if (success) {
                    menuDisplayArea.append("✓ Menu item added successfully!\n");
                    menuDisplayArea.append("=".repeat(60) + "\n");
                    menuDisplayArea.append(String.format("Category: %s\n", category));
                    menuDisplayArea.append(String.format("Name: %s\n", name));
                    menuDisplayArea.append(String.format("Price: $%.2f\n", price));
                } else {
                    menuDisplayArea.append("\nERROR: Failed to add menu item\n");
                }

            } catch (NumberFormatException e) {
                menuDisplayArea.append("\nERROR: Invalid price format. Please enter a valid number.\n");
            } catch (Exception e) {
                menuDisplayArea.append("\nERROR: " + e.getMessage() + "\n");
            }
        }
    }

    private void updateMenuItemPrice() {
        try {
            java.util.List<MenuItem> menuItems = dbManager.getAllMenuItems();

            StringBuilder menuList = new StringBuilder("Current Menu Items:\n\n");
            for (MenuItem item : menuItems) {
                menuList.append(String.format("ID %d: %s - $%.2f\n",
                        item.getMenuItemID(),
                        item.getMenuItemName(),
                        item.getPrice()));
            }

            JPanel panel = new JPanel(new BorderLayout(10, 10));

            JTextArea menuArea = new JTextArea(menuList.toString());
            menuArea.setEditable(false);
            menuArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
            JScrollPane scrollPane = new JScrollPane(menuArea);
            scrollPane.setPreferredSize(new Dimension(400, 200));

            JTextField idField = new JTextField();
            JTextField priceField = new JTextField();

            panel.add(scrollPane, BorderLayout.NORTH);

            JPanel fieldsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
            fieldsPanel.add(new JLabel("Menu Item ID:"));
            fieldsPanel.add(idField);
            fieldsPanel.add(new JLabel("New Price:"));
            fieldsPanel.add(priceField);

            panel.add(fieldsPanel, BorderLayout.CENTER);

            int result = JOptionPane.showConfirmDialog(this, panel,
                    "Update Menu Item Price", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                menuDisplayArea.setText("");
                int itemId = Integer.parseInt(idField.getText().trim());
                double newPrice = Double.parseDouble(priceField.getText().trim());

                boolean success = dbManager.updateMenuItemPrice(itemId, newPrice);

                if (success) {
                    menuDisplayArea.append("✓ Menu item price updated successfully!\n");
                    menuDisplayArea.append("=".repeat(60) + "\n");
                    menuDisplayArea.append(String.format("Item ID: %d\n", itemId));
                    menuDisplayArea.append(String.format("New Price: $%.2f\n", newPrice));
                } else {
                    menuDisplayArea.append("\nERROR: Menu item ID not found or update failed.\n");
                }
            }

        } catch (NumberFormatException e) {
            menuDisplayArea.setText("");
            menuDisplayArea.append("\nERROR: Invalid input format. Please enter valid numbers.\n");
        } catch (Exception e) {
            menuDisplayArea.setText("");
            menuDisplayArea.append("\nERROR: " + e.getMessage() + "\n");
        }
    }

    private void addSeasonalMenuItem() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));

        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField seasonField = new JTextField("Fall 2025");
        JTextArea descriptionArea = new JTextArea(3, 20);
        descriptionArea.setBorder(BorderFactory.createLoweredBevelBorder());
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);

        panel.add(new JLabel("Seasonal Item Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Season/Period:"));
        panel.add(seasonField);
        panel.add(new JLabel("Description:"));
        panel.add(descScrollPane);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Add New Seasonal Menu Item", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            menuDisplayArea.setText("");
            try {
                String name = nameField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                String season = seasonField.getText().trim();
                String description = descriptionArea.getText().trim();

                if (name.isEmpty()) {
                    throw new IllegalArgumentException("Item name cannot be empty");
                }

                // Create seasonal menu item with "Seasonal" category
                MenuItem seasonalItem = new MenuItem(0, "Seasonal", name, price);
                boolean success = dbManager.addMenuItem(seasonalItem);

                if (success) {
                    menuDisplayArea.append("✓ NEW SEASONAL MENU ITEM ADDED!\n");
                    menuDisplayArea.append("=".repeat(60) + "\n");
                    menuDisplayArea.append(String.format("Item Name: %s\n", name));
                    menuDisplayArea.append(String.format("Category: Seasonal\n"));
                    menuDisplayArea.append(String.format("Price: $%.2f\n", price));
                    menuDisplayArea.append(String.format("Season: %s\n", season));
                    menuDisplayArea.append(String.format("Description: %s\n", description));
                    menuDisplayArea.append("\n• This item has been added to the POS system\n");
                    menuDisplayArea.append("• Staff can now take orders for this seasonal item\n");
                    menuDisplayArea.append("• Remember to update inventory with required ingredients\n");
                } else {
                    menuDisplayArea.append("\nERROR: Failed to add seasonal menu item.\n");
                }

            } catch (NumberFormatException e) {
                menuDisplayArea.append("\nERROR: Invalid price format. Please enter a valid number.\n");
            } catch (Exception e) {
                menuDisplayArea.append("\nERROR: " + e.getMessage() + "\n");
            }
        }
    }

    // Inventory Management Methods
    private void viewInventory() {
        inventoryDisplayArea.setText("");
        try {
            java.util.List<Inventory> inventory = dbManager.getAllInventory();

            inventoryDisplayArea.append("INVENTORY\n");
            inventoryDisplayArea.append("=".repeat(70) + "\n");
            inventoryDisplayArea.append(String.format("%-15s %-35s %-15s\n",
                    "Ingredient ID", "Ingredient Name", "Quantity"));
            inventoryDisplayArea.append("-".repeat(70) + "\n");

            for (Inventory item : inventory) {
                inventoryDisplayArea.append(String.format("%-15d %-35s %-15d\n",
                        item.getIngredientID(),
                        item.getIngredientName(),
                        item.getIngredientCount()));
            }

        } catch (Exception e) {
            inventoryDisplayArea.append("\nERROR: " + e.getMessage() + "\n");
        }
    }

    private void addInventoryItem() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        JTextField nameField = new JTextField();
        JTextField quantityField = new JTextField();

        panel.add(new JLabel("Ingredient Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Initial Quantity:"));
        panel.add(quantityField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Add New Inventory Item", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            inventoryDisplayArea.setText("");
            try {
                String name = nameField.getText().trim();
                int quantity = Integer.parseInt(quantityField.getText().trim());

                if (name.isEmpty()) {
                    throw new IllegalArgumentException("Ingredient name cannot be empty");
                }

                Inventory item = new Inventory(0, name, quantity);
                boolean success = dbManager.addInventoryItem(item);

                if (success) {
                    inventoryDisplayArea.append("✓ Inventory item added successfully!\n");
                    inventoryDisplayArea.append("=".repeat(60) + "\n");
                    inventoryDisplayArea.append(String.format("Name: %s\n", name));
                    inventoryDisplayArea.append(String.format("Quantity: %d\n", quantity));
                } else {
                    inventoryDisplayArea.append("\nERROR: Failed to add inventory item\n");
                }

            } catch (NumberFormatException e) {
                inventoryDisplayArea.append("\nERROR: Invalid quantity format. Please enter a valid number.\n");
            } catch (Exception e) {
                inventoryDisplayArea.append("\nERROR: " + e.getMessage() + "\n");
            }
        }
    }

    private void updateInventoryQuantity() {
        try {
            java.util.List<Inventory> inventory = dbManager.getAllInventory();

            StringBuilder inventoryList = new StringBuilder("Current Inventory:\n\n");
            for (Inventory item : inventory) {
                inventoryList.append(String.format("ID %d: %s - Quantity: %d\n",
                        item.getIngredientID(),
                        item.getIngredientName(),
                        item.getIngredientCount()));
            }

            JPanel panel = new JPanel(new BorderLayout(10, 10));

            JTextArea inventoryArea = new JTextArea(inventoryList.toString());
            inventoryArea.setEditable(false);
            inventoryArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
            JScrollPane scrollPane = new JScrollPane(inventoryArea);
            scrollPane.setPreferredSize(new Dimension(400, 200));

            JTextField idField = new JTextField();
            JTextField quantityField = new JTextField();

            panel.add(scrollPane, BorderLayout.NORTH);

            JPanel fieldsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
            fieldsPanel.add(new JLabel("Ingredient ID:"));
            fieldsPanel.add(idField);
            fieldsPanel.add(new JLabel("New Quantity:"));
            fieldsPanel.add(quantityField);

            panel.add(fieldsPanel, BorderLayout.CENTER);

            int result = JOptionPane.showConfirmDialog(this, panel,
                    "Update Inventory Quantity", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                inventoryDisplayArea.setText("");
                int ingredientId = Integer.parseInt(idField.getText().trim());
                int newQuantity = Integer.parseInt(quantityField.getText().trim());

                boolean success = dbManager.updateInventoryQuantity(ingredientId, newQuantity);

                if (success) {
                    inventoryDisplayArea.append("✓ Inventory quantity updated successfully!\n");
                    inventoryDisplayArea.append("=".repeat(60) + "\n");
                    inventoryDisplayArea.append(String.format("Ingredient ID: %d\n", ingredientId));
                    inventoryDisplayArea.append(String.format("New Quantity: %d\n", newQuantity));
                } else {
                    inventoryDisplayArea.append("\nERROR: Ingredient ID not found or update failed.\n");
                }
            }

        } catch (NumberFormatException e) {
            inventoryDisplayArea.setText("");
            inventoryDisplayArea.append("\nERROR: Invalid input format. Please enter valid numbers.\n");
        } catch (Exception e) {
            inventoryDisplayArea.setText("");
            inventoryDisplayArea.append("\nERROR: " + e.getMessage() + "\n");
        }
    }
    // Employee Management Methods
    private void viewEmployees() {
        employeeDisplayArea.setText("");
        try {
            java.util.List<Employee> employees = dbManager.getAllEmployees();

            employeeDisplayArea.append("EMPLOYEES\n");
            employeeDisplayArea.append("=".repeat(80) + "\n");
            employeeDisplayArea.append(String.format("%-12s %-25s %-20s %-15s\n",
                    "Employee ID", "Employee Name", "Role", "Hours Worked"));
            employeeDisplayArea.append("-".repeat(80) + "\n");

            for (Employee employee : employees) {
                employeeDisplayArea.append(String.format("%-12d %-25s %-20s %-15d\n",
                        employee.getEmployeeID(),
                        employee.getEmployeeName(),
                        employee.getEmployeeRole(),
                        employee.getHoursWorked()));
            }

        } catch (Exception e) {
            employeeDisplayArea.append("\nERROR: " + e.getMessage() + "\n");
        }
    }
    private void addEmployee() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        JTextField nameField = new JTextField();
        JTextField roleField = new JTextField();
        JTextField hoursField = new JTextField("0");

        panel.add(new JLabel("Employee Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Role:"));
        panel.add(roleField);
        panel.add(new JLabel("Hours Worked:"));
        panel.add(hoursField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Add New Employee", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            employeeDisplayArea.setText("");
            try {
                String name = nameField.getText().trim();
                String role = roleField.getText().trim();
                int hours = Integer.parseInt(hoursField.getText().trim());

                if (name.isEmpty() || role.isEmpty()) {
                    throw new IllegalArgumentException("Name and role cannot be empty");
                }

                Employee employee = new Employee(0, name, role, hours);
                boolean success = dbManager.addEmployee(employee);

                if (success) {
                    employeeDisplayArea.append("✓ Employee added successfully!\n");
                    employeeDisplayArea.append("=".repeat(60) + "\n");
                    employeeDisplayArea.append(String.format("Name: %s\n", name));
                    employeeDisplayArea.append(String.format("Role: %s\n", role));
                    employeeDisplayArea.append(String.format("Hours Worked: %d\n", hours));
                } else {
                    employeeDisplayArea.append("\nERROR: Failed to add employee\n");
                }

            } catch (NumberFormatException e) {
                employeeDisplayArea.append("\nERROR: Invalid hours format. Please enter a valid number.\n");
            } catch (Exception e) {
                employeeDisplayArea.append("\nERROR: " + e.getMessage() + "\n");
            }
        }
    }

     private void updateEmployee() {
        try {
            java.util.List<Employee> employees = dbManager.getAllEmployees();

            StringBuilder employeeList = new StringBuilder("Current Employees:\n\n");
            for (Employee employee : employees) {
                employeeList.append(String.format("ID %d: %s - %s (Hours: %d)\n",
                        employee.getEmployeeID(),
                        employee.getEmployeeName(),
                        employee.getEmployeeRole(),
                        employee.getHoursWorked()));
            }

            JPanel panel = new JPanel(new BorderLayout(10, 10));

            JTextArea employeeArea = new JTextArea(employeeList.toString());
            employeeArea.setEditable(false);
            employeeArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
            JScrollPane scrollPane = new JScrollPane(employeeArea);
            scrollPane.setPreferredSize(new Dimension(500, 200));

            JTextField idField = new JTextField();
            JTextField nameField = new JTextField();
            JTextField roleField = new JTextField();
            JTextField hoursField = new JTextField();

            panel.add(scrollPane, BorderLayout.NORTH);

            JPanel fieldsPanel = new JPanel(new GridLayout(4, 2, 10, 10));
            fieldsPanel.add(new JLabel("Employee ID:"));
            fieldsPanel.add(idField);
            fieldsPanel.add(new JLabel("New Name (leave empty to keep):"));
            fieldsPanel.add(nameField);
            fieldsPanel.add(new JLabel("New Role (leave empty to keep):"));
            fieldsPanel.add(roleField);
            fieldsPanel.add(new JLabel("New Hours (leave empty to keep):"));
            fieldsPanel.add(hoursField);

            panel.add(fieldsPanel, BorderLayout.CENTER);

            int result = JOptionPane.showConfirmDialog(this, panel,
                    "Update Employee", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                employeeDisplayArea.setText("");
                int employeeId = Integer.parseInt(idField.getText().trim());

                // Find existing employee
                Employee existingEmployee = null;
                for (Employee emp : employees) {
                    if (emp.getEmployeeID() == employeeId) {
                        existingEmployee = emp;
                        break;
                    }
                }

                if (existingEmployee == null) {
                    employeeDisplayArea.append("\nERROR: Employee ID not found.\n");
                    return;
                }

                // Update fields
                String newName = nameField.getText().trim();
                if (newName.isEmpty()) {
                    newName = existingEmployee.getEmployeeName();
                }

                String newRole = roleField.getText().trim();
                if (newRole.isEmpty()) {
                    newRole = existingEmployee.getEmployeeRole();
                }

                int newHours = existingEmployee.getHoursWorked();
                if (!hoursField.getText().trim().isEmpty()) {
                    newHours = Integer.parseInt(hoursField.getText().trim());
                }

                Employee updatedEmployee = new Employee(employeeId, newName, newRole, newHours);
                boolean success = dbManager.updateEmployee(updatedEmployee);

                if (success) {
                    employeeDisplayArea.append("✓ Employee updated successfully!\n");
                    employeeDisplayArea.append("=".repeat(60) + "\n");
                    employeeDisplayArea.append(String.format("Employee ID: %d\n", employeeId));
                    employeeDisplayArea.append(String.format("Name: %s\n", newName));
                    employeeDisplayArea.append(String.format("Role: %s\n", newRole));
                    employeeDisplayArea.append(String.format("Hours Worked: %d\n", newHours));
                } else {
                    employeeDisplayArea.append("\nERROR: Failed to update employee\n");
                }
            }

        } catch (NumberFormatException e) {
            employeeDisplayArea.setText("");
            employeeDisplayArea.append("\nERROR: Invalid input format. Please enter valid numbers.\n");
        } catch (Exception e) {
            employeeDisplayArea.setText("");
            employeeDisplayArea.append("\nERROR: " + e.getMessage() + "\n");
        }
    }
  private void removeEmployee() {
        try {
            java.util.List<Employee> employees = dbManager.getAllEmployees();

            StringBuilder employeeList = new StringBuilder("Current Employees:\n\n");
            for (Employee employee : employees) {
                employeeList.append(String.format("ID %d: %s - %s\n",
                        employee.getEmployeeID(),
                        employee.getEmployeeName(),
                        employee.getEmployeeRole()));
            }

            JPanel panel = new JPanel(new BorderLayout(10, 10));

            JTextArea employeeArea = new JTextArea(employeeList.toString());
            employeeArea.setEditable(false);
            employeeArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
            JScrollPane scrollPane = new JScrollPane(employeeArea);
            scrollPane.setPreferredSize(new Dimension(400, 200));

            JTextField idField = new JTextField();

            panel.add(scrollPane, BorderLayout.NORTH);

            JPanel fieldPanel = new JPanel(new GridLayout(1, 2, 10, 10));
            fieldPanel.add(new JLabel("Employee ID to Remove:"));
            fieldPanel.add(idField);

            panel.add(fieldPanel, BorderLayout.CENTER);

            int result = JOptionPane.showConfirmDialog(this, panel,
                    "Remove Employee", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                employeeDisplayArea.setText("");
                int employeeId = Integer.parseInt(idField.getText().trim());

                // Find employee info before deletion
                Employee employeeToDelete = null;
                for (Employee emp : employees) {
                    if (emp.getEmployeeID() == employeeId) {
                        employeeToDelete = emp;
                        break;
                    }
                }

                if (employeeToDelete == null) {
                    employeeDisplayArea.append("\nERROR: Employee ID not found.\n");
                    return;
                }

                // Confirm deletion
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to remove:\n" +
                                employeeToDelete.getEmployeeName() + " (" + employeeToDelete.getEmployeeRole() + ")?",
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION) {
                    boolean success = dbManager.deleteEmployee(employeeId);

                    if (success) {
                        employeeDisplayArea.append("✓ Employee removed successfully!\n");
                        employeeDisplayArea.append("=".repeat(60) + "\n");
                        employeeDisplayArea.append(String.format("Removed: %s (ID: %d)\n",
                                employeeToDelete.getEmployeeName(), employeeId));
                    } else {
                        employeeDisplayArea.append("\nERROR: Failed to remove employee\n");
                    }
                } else {
                    employeeDisplayArea.append("Employee removal cancelled.\n");
                }
            }

        } catch (NumberFormatException e) {
            employeeDisplayArea.setText("");
            employeeDisplayArea.append("\nERROR: Invalid input format. Please enter a valid number.\n");
        } catch (Exception e) {
            employeeDisplayArea.setText("");
            employeeDisplayArea.append("\nERROR: " + e.getMessage() + "\n");
        }
    }
  // Report Methods (simplified for Phase 3, will be expanded in Phase 4)
    private void generateSalesTodayReport() {
        reportsDisplayArea.setText("");
        reportsDisplayArea.append("SALES TODAY REPORT\n");
        reportsDisplayArea.append("=".repeat(50) + "\n");
        reportsDisplayArea.append("Date: " + java.time.LocalDate.now() + "\n\n");

        if (dbManager.isUsingMockData()) {
            reportsDisplayArea.append("Sample Data:\n");
            reportsDisplayArea.append("Orders Today: 15\n");
            reportsDisplayArea.append("Total Revenue: $247.50\n");
            reportsDisplayArea.append("Average Order: $16.50\n");
        } else {
            reportsDisplayArea.append("This feature will be enhanced when database is connected.\n");
        }
    }
  private void generateTopSellersReport() {
        reportsDisplayArea.setText("");
        reportsDisplayArea.append("TOP SELLING ITEMS REPORT\n");
        reportsDisplayArea.append("=".repeat(50) + "\n");

        if (dbManager.isUsingMockData()) {
            reportsDisplayArea.append("1. Brown Sugar Boba - 45 orders\n");
            reportsDisplayArea.append("2. Classic Milk Tea - 38 orders\n");
            reportsDisplayArea.append("3. Taro Milk Tea - 32 orders\n");
            reportsDisplayArea.append("4. Mango Smoothie - 28 orders\n");
            reportsDisplayArea.append("5. Matcha Milk Tea - 25 orders\n");
        } else {
            reportsDisplayArea.append("This feature will be enhanced when database is connected.\n");
        }
    }
  private void generateRevenueReport() {
        reportsDisplayArea.setText("");
        reportsDisplayArea.append("REVENUE REPORT\n");
        reportsDisplayArea.append("=".repeat(50) + "\n");

        if (dbManager.isUsingMockData()) {
            reportsDisplayArea.append("Total Revenue (All Time): $12,450.75\n");
            reportsDisplayArea.append("This Month: $3,247.50\n");
            reportsDisplayArea.append("This Week: $987.25\n");
            reportsDisplayArea.append("Today: $247.50\n");
        } else {
            reportsDisplayArea.append("This feature will be enhanced when database is connected.\n");
        }
    }
  private void generateUsageReport() {
        reportsDisplayArea.setText("");
        reportsDisplayArea.append("PRODUCT USAGE CHART\n");
        reportsDisplayArea.append("=".repeat(50) + "\n");
        reportsDisplayArea.append("Period: Last 7 days\n\n");

        if (dbManager.isUsingMockData()) {
            reportsDisplayArea.append("Tapioca Pearls: 250 servings\n");
            reportsDisplayArea.append("Milk Tea Base: 180 servings\n");
            reportsDisplayArea.append("Fruit Syrups: 145 servings\n");
            reportsDisplayArea.append("Toppings (Mixed): 320 servings\n");
        } else {
            reportsDisplayArea.append("This feature will be enhanced when database is connected.\n");
        }
    }
   private void generateLowStockReport() {
        reportsDisplayArea.setText("");
        reportsDisplayArea.append("LOW STOCK ALERT\n");
        reportsDisplayArea.append("=".repeat(50) + "\n");

        java.util.List<Inventory> inventory = dbManager.getAllInventory();
        boolean hasLowStock = false;

        for (Inventory item : inventory) {
            if (item.getIngredientCount() < 50) { // Threshold of 50
                hasLowStock = true;
                reportsDisplayArea.append(String.format("⚠ %s: %d remaining\n",
                        item.getIngredientName(), item.getIngredientCount()));
            }
        }

        if (!hasLowStock) {
            reportsDisplayArea.append("✓ All items are adequately stocked!\n");
        }
    }
  private void generateInventoryValueReport() {
        reportsDisplayArea.setText("");
        reportsDisplayArea.append("INVENTORY VALUE REPORT\n");
        reportsDisplayArea.append("=".repeat(50) + "\n");

        if (dbManager.isUsingMockData()) {
            reportsDisplayArea.append("Total Inventory Items: 28\n");
            reportsDisplayArea.append("Estimated Total Value: $3,450.00\n");
            reportsDisplayArea.append("High-Value Items: 12\n");
            reportsDisplayArea.append("Low-Stock Items: 3\n");
        } else {
            reportsDisplayArea.append("This feature will be enhanced when database is connected.\n");
        }
    }
    private void generateRestockReport() {
        reportsDisplayArea.setText("");
        reportsDisplayArea.append("RESTOCK RECOMMENDATIONS\n");
        reportsDisplayArea.append("=".repeat(50) + "\n");

        if (dbManager.isUsingMockData()) {
            reportsDisplayArea.append("Items to Restock:\n");
            reportsDisplayArea.append("- Oat Milk: Current 45, Recommended 100\n");
            reportsDisplayArea.append("- Aloe Vera: Current 120, Recommended 200\n");
            reportsDisplayArea.append("- Cup Sleeves: Current 500, Recommended 800\n");
        } else {
            reportsDisplayArea.append("This feature will be enhanced when database is connected.\n");
        }
    }
    private void generateXReport() {
        reportsDisplayArea.setText("");
        reportsDisplayArea.append("X-REPORT (HOURLY SALES)\n");
        reportsDisplayArea.append("=".repeat(50) + "\n");
        reportsDisplayArea.append("Date: " + java.time.LocalDate.now() + "\n");
        reportsDisplayArea.append("Time: " + java.time.LocalTime.now().toString().substring(0, 8) + "\n\n");

        if (dbManager.isUsingMockData()) {
            reportsDisplayArea.append("Sales by Hour:\n");
            reportsDisplayArea.append("9 AM:  $45.50\n");
            reportsDisplayArea.append("10 AM: $67.25\n");
            reportsDisplayArea.append("11 AM: $89.75\n");
            reportsDisplayArea.append("12 PM: $125.00\n");
            reportsDisplayArea.append("1 PM:  $98.50\n");
            reportsDisplayArea.append("2 PM:  $76.25\n");
            reportsDisplayArea.append("\nTotal So Far: $502.25\n");
        } else {
            reportsDisplayArea.append("This feature will be enhanced when database is connected.\n");
        }
    }
    private void generateStaffHoursReport() {
        reportsDisplayArea.setText("");
        reportsDisplayArea.append("STAFF HOURS REPORT\n");
        reportsDisplayArea.append("=".repeat(50) + "\n");

        java.util.List<Employee> employees = dbManager.getAllEmployees();

        for (Employee employee : employees) {
            reportsDisplayArea.append(String.format("%s (%s): %d hours\n",
                    employee.getEmployeeName(),
                    employee.getEmployeeRole(),
                    employee.getHoursWorked()));
        }
    }
    private void generatePerformanceReport() {
        reportsDisplayArea.setText("");
        reportsDisplayArea.append("PERFORMANCE REPORT\n");
        reportsDisplayArea.append("=".repeat(50) + "\n");

        if (dbManager.isUsingMockData()) {
            reportsDisplayArea.append("Team Performance Metrics:\n");
            reportsDisplayArea.append("- Average Order Processing Time: 3.2 minutes\n");
            reportsDisplayArea.append("- Customer Satisfaction: 4.5/5\n");
            reportsDisplayArea.append("- Daily Order Volume: 85 orders\n");
            reportsDisplayArea.append("- Peak Hour Efficiency: 92%\n");
        } else {
            reportsDisplayArea.append("This feature will be enhanced when database is connected.\n");
        }
    }
    private void generateZReport() {
        reportsDisplayArea.setText("");
        reportsDisplayArea.append("Z-REPORT (END OF DAY)\n");
        reportsDisplayArea.append("=".repeat(50) + "\n");
        reportsDisplayArea.append("Date: " + java.time.LocalDate.now() + "\n\n");

        if (dbManager.isUsingMockData()) {
            reportsDisplayArea.append("Daily Summary:\n");
            reportsDisplayArea.append("Total Orders: 67\n");
            reportsDisplayArea.append("Total Sales: $1,245.75\n");
            reportsDisplayArea.append("Cash: $345.25\n");
            reportsDisplayArea.append("Card: $900.50\n");
            reportsDisplayArea.append("Returns: $0.00\n");
            reportsDisplayArea.append("Voids: $15.50\n");
            reportsDisplayArea.append("\n⚠ End-of-day totals reset after this report\n");
        } else {
            reportsDisplayArea.append("This feature will be enhanced when database is connected.\n");
        }
    }
     private void exportCurrentReport() {
        try {
            String reportContent = reportsDisplayArea.getText();
            if (reportContent.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No report to export. Generate a report first.",
                        "Export Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            java.time.LocalDateTime now = java.time.LocalDateTime.now();
            String timestamp = now.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String filename = "boba_shop_report_" + timestamp + ".txt";

            java.nio.file.Files.write(java.nio.file.Paths.get(filename), reportContent.getBytes());

            JOptionPane.showMessageDialog(this,
                    "Report exported successfully to: " + filename,
                    "Export Success",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Failed to export report: " + e.getMessage(),
                    "Export Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void cleanup() {
        if (dbManager != null) {
            dbManager.closeConnection();
        }
    }

}