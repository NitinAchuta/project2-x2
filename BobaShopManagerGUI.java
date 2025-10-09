import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BobaShopManagerGUI extends JFrame {
    // Database connection details
    private static final Map<String, String> env = loadEnvFile(".env");
    private static final String DB_URL = env.get("DB_URL");
    private static final String DB_USER = env.get("DB_USER");
    private static final String DB_PASS = env.get("DB_PASS");

    private Connection conn = null;
    private JTextArea displayArea;
    private JTextArea inventoryDisplayArea;
    private JTextArea employeeDisplayArea;
    
    public BobaShopManagerGUI() {
        setTitle("Boba Shop Manager System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Connect to database
        connectToDatabase();
        
        // Create tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Tab 1: Menu Management
        JPanel menuManagementTab = createMenuManagementTab();
        tabbedPane.addTab("Menu Management", menuManagementTab);
        
        // Tab 2: Inventory Management
        JPanel inventoryManagementTab = createInventoryManagementTab();
        tabbedPane.addTab("Inventory Management", inventoryManagementTab);
        
        // Tab 3: Empty for now
        JPanel employeeManagementTab = createEmployeeManagementTab();
        tabbedPane.addTab("Employee Management", employeeManagementTab);
        
        // Tab 4: Empty for now
        JPanel tab4 = createEmptyTab("Tab 4");
        tabbedPane.addTab("Tab 4", tab4);
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Create status panel
        JPanel statusPanel = new JPanel();
        JLabel statusLabel = new JLabel("Ready");
        statusPanel.add(statusLabel);
        add(statusPanel, BorderLayout.SOUTH);
        
        setVisible(true);
    }
    
    private void connectToDatabase() {
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(
                DB_URL, DB_USER, DB_PASS);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Database connection failed: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }
    
    private JPanel createMenuManagementTab() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Display area
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(displayArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        
        JButton viewButton = new JButton("View Menu Items");
        viewButton.addActionListener(e -> viewMenuItems());
        buttonPanel.add(viewButton);
        
        JButton addButton = new JButton("Add Menu Item");
        addButton.addActionListener(e -> addMenuItem());
        buttonPanel.add(addButton);
        
        JButton updateButton = new JButton("Update Item Price");
        updateButton.addActionListener(e -> updateMenuItemPrice());
        buttonPanel.add(updateButton);
        
        panel.add(buttonPanel, BorderLayout.NORTH);
        
        // Show initial message
        displayArea.append("✓ Connected to database successfully\n");
        displayArea.append("=".repeat(60) + "\n\n");
        displayArea.append("Menu Management\n");
        displayArea.append("Use the buttons above to view, add, or update menu items.\n");
        
        return panel;
    }

    private JPanel createEmployeeManagementTab() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Display area for employees
        employeeDisplayArea = new JTextArea();
        employeeDisplayArea.setEditable(false);
        employeeDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(employeeDisplayArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        
        JButton viewButton = new JButton("View Employees");
        viewButton.addActionListener(e -> viewEmployees());
        buttonPanel.add(viewButton);
        
        JButton addButton = new JButton("Add Employee");
        addButton.addActionListener(e -> addEmployee());
        buttonPanel.add(addButton);
        
        JButton updateButton = new JButton("Update Employee");
        updateButton.addActionListener(e -> updateEmployee());
        buttonPanel.add(updateButton);
        
        JButton deleteButton = new JButton("Remove Employee");
        deleteButton.addActionListener(e -> removeEmployee());
        buttonPanel.add(deleteButton);
        
        panel.add(buttonPanel, BorderLayout.NORTH);
        
        // Show initial message
        employeeDisplayArea.append("✓ Connected to database successfully\n");
        employeeDisplayArea.append("=".repeat(60) + "\n\n");
        employeeDisplayArea.append("Employee Management\n");
        employeeDisplayArea.append("Use the buttons above to view, add, update, or manage employees.\n");
        
        return panel;
    }
    
    private JPanel createEmptyTab(String tabName) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(tabName + " - Coming Soon", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setForeground(Color.GRAY);
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createInventoryManagementTab() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Display area for inventory
        inventoryDisplayArea = new JTextArea();
        inventoryDisplayArea.setEditable(false);
        inventoryDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(inventoryDisplayArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        
        JButton viewButton = new JButton("View Inventory");
        viewButton.addActionListener(e -> viewInventory());
        buttonPanel.add(viewButton);
        
        JButton addButton = new JButton("Add Inventory Item");
        addButton.addActionListener(e -> addInventoryItem());
        buttonPanel.add(addButton);
        
        JButton updateButton = new JButton("Update Quantity");
        updateButton.addActionListener(e -> updateInventoryQuantity());
        buttonPanel.add(updateButton);
        
        panel.add(buttonPanel, BorderLayout.NORTH);
        
        // Show initial message
        inventoryDisplayArea.append("✓ Connected to database successfully\n");
        inventoryDisplayArea.append("=".repeat(60) + "\n\n");
        inventoryDisplayArea.append("Inventory Management\n");
        inventoryDisplayArea.append("Use the buttons above to view, add, or update inventory items.\n");
        
        return panel;
    }
    
    private void viewMenuItems() {
        displayArea.setText("");
        try {
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM menuitems ORDER BY drinkcategory, menuitemname";
            ResultSet rs = stmt.executeQuery(sql);
            
            displayArea.append("MENU ITEMS\n");
            displayArea.append("=".repeat(80) + "\n");
            displayArea.append(String.format("%-12s %-20s %-30s %-10s\n",
                "Item ID", "Category", "Item Name", "Price"));
            displayArea.append("-".repeat(80) + "\n");
            
            while (rs.next()) {
                displayArea.append(String.format("%-12d %-20s %-30s $%-9.2f\n",
                    rs.getInt("menuitemid"),
                    rs.getString("drinkcategory"),
                    rs.getString("menuitemname"),
                    rs.getDouble("price")));
            }
            
            rs.close();
            stmt.close();
        } catch (Exception e) {
            displayArea.append("\nERROR: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }
    
    private void viewInventory() {
        inventoryDisplayArea.setText("");
        try {
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM inventory ORDER BY ingredientname";
            ResultSet rs = stmt.executeQuery(sql);
            
            inventoryDisplayArea.append("INVENTORY\n");
            inventoryDisplayArea.append("=".repeat(70) + "\n");
            inventoryDisplayArea.append(String.format("%-15s %-35s %-15s\n",
                "Ingredient ID", "Ingredient Name", "Quantity"));
            inventoryDisplayArea.append("-".repeat(70) + "\n");
            
            while (rs.next()) {
                inventoryDisplayArea.append(String.format("%-15d %-35s %-15d\n",
                    rs.getInt("ingredientid"),
                    rs.getString("ingredientname"),
                    rs.getInt("ingredientcount")));
            }
            
            rs.close();
            stmt.close();
        } catch (Exception e) {
            inventoryDisplayArea.append("\nERROR: " + e.getMessage() + "\n");
            e.printStackTrace();
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
                
                // Get the next ingredientid
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT MAX(ingredientid) as maxid FROM inventory");
                int nextId = 1;
                if (rs.next() && rs.getObject("maxid") != null) {
                    nextId = rs.getInt("maxid") + 1;
                }
                rs.close();
                
                // Insert new inventory item
                String sql = "INSERT INTO inventory (ingredientid, ingredientname, ingredientcount) VALUES (?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, nextId);
                pstmt.setString(2, name);
                pstmt.setInt(3, quantity);
                
                pstmt.executeUpdate();
                
                inventoryDisplayArea.append("✓ Inventory item added successfully!\n");
                inventoryDisplayArea.append("=".repeat(60) + "\n");
                inventoryDisplayArea.append(String.format("Ingredient ID: %d\n", nextId));
                inventoryDisplayArea.append(String.format("Name: %s\n", name));
                inventoryDisplayArea.append(String.format("Quantity: %d\n", quantity));
                
                pstmt.close();
                stmt.close();
                
            } catch (NumberFormatException e) {
                inventoryDisplayArea.append("\nERROR: Invalid quantity format. Please enter a valid number.\n");
            } catch (Exception e) {
                inventoryDisplayArea.append("\nERROR: " + e.getMessage() + "\n");
                e.printStackTrace();
            }
        }
    }
    
    private void updateInventoryQuantity() {
        try {
            // First, show current inventory
            Statement stmt = conn.createStatement();
            String sql = "SELECT ingredientid, ingredientname, ingredientcount FROM inventory ORDER BY ingredientid";
            ResultSet rs = stmt.executeQuery(sql);
            
            StringBuilder inventoryList = new StringBuilder("Current Inventory:\n\n");
            while (rs.next()) {
                inventoryList.append(String.format("ID %d: %s - Quantity: %d\n",
                    rs.getInt("ingredientid"),
                    rs.getString("ingredientname"),
                    rs.getInt("ingredientcount")));
            }
            rs.close();
            stmt.close();
            
            // Ask for item ID and new quantity
            JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
            
            JTextArea inventoryArea = new JTextArea(inventoryList.toString());
            inventoryArea.setEditable(false);
            inventoryArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
            JScrollPane scrollPane = new JScrollPane(inventoryArea);
            scrollPane.setPreferredSize(new Dimension(400, 200));
            
            JTextField idField = new JTextField();
            JTextField quantityField = new JTextField();
            
            JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
            inputPanel.add(scrollPane, BorderLayout.NORTH);
            
            JPanel fieldsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
            fieldsPanel.add(new JLabel("Ingredient ID:"));
            fieldsPanel.add(idField);
            fieldsPanel.add(new JLabel("New Quantity:"));
            fieldsPanel.add(quantityField);
            
            inputPanel.add(fieldsPanel, BorderLayout.CENTER);
            
            int result = JOptionPane.showConfirmDialog(this, inputPanel, 
                "Update Inventory Quantity", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            
            if (result == JOptionPane.OK_OPTION) {
                inventoryDisplayArea.setText("");
                int ingredientId = Integer.parseInt(idField.getText().trim());
                int newQuantity = Integer.parseInt(quantityField.getText().trim());
                
                // Update the quantity
                String updateSql = "UPDATE inventory SET ingredientcount = ? WHERE ingredientid = ?";
                PreparedStatement pstmt = conn.prepareStatement(updateSql);
                pstmt.setInt(1, newQuantity);
                pstmt.setInt(2, ingredientId);
                
                int rowsAffected = pstmt.executeUpdate();
                
                if (rowsAffected > 0) {
                    // Get updated item details
                    String selectSql = "SELECT * FROM inventory WHERE ingredientid = ?";
                    PreparedStatement selectStmt = conn.prepareStatement(selectSql);
                    selectStmt.setInt(1, ingredientId);
                    ResultSet updatedRs = selectStmt.executeQuery();
                    
                    if (updatedRs.next()) {
                        inventoryDisplayArea.append("✓ Inventory quantity updated successfully!\n");
                        inventoryDisplayArea.append("=".repeat(60) + "\n");
                        inventoryDisplayArea.append(String.format("Ingredient ID: %d\n", ingredientId));
                        inventoryDisplayArea.append(String.format("Name: %s\n", updatedRs.getString("ingredientname")));
                        inventoryDisplayArea.append(String.format("New Quantity: %d\n", newQuantity));
                    }
                    
                    updatedRs.close();
                    selectStmt.close();
                } else {
                    inventoryDisplayArea.append("\nERROR: Ingredient ID not found.\n");
                }
                
                pstmt.close();
            }
            
        } catch (NumberFormatException e) {
            inventoryDisplayArea.setText("");
            inventoryDisplayArea.append("\nERROR: Invalid input format. Please enter valid numbers.\n");
        } catch (Exception e) {
            inventoryDisplayArea.setText("");
            inventoryDisplayArea.append("\nERROR: " + e.getMessage() + "\n");
            e.printStackTrace();
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
            displayArea.setText("");
            try {
                String category = categoryField.getText().trim();
                String name = nameField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                
                if (category.isEmpty() || name.isEmpty()) {
                    throw new IllegalArgumentException("Category and name cannot be empty");
                }
                
                // Get the next menuitemid
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT MAX(menuitemid) as maxid FROM menuitems");
                int nextId = 1;
                if (rs.next() && rs.getObject("maxid") != null) {
                    nextId = rs.getInt("maxid") + 1;
                }
                rs.close();
                
                // Insert new menu item
                String sql = "INSERT INTO menuitems (menuitemid, drinkcategory, menuitemname, price) VALUES (?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, nextId);
                pstmt.setString(2, category);
                pstmt.setString(3, name);
                pstmt.setDouble(4, price);
                
                pstmt.executeUpdate();
                
                displayArea.append("✓ Menu item added successfully!\n");
                displayArea.append("=".repeat(60) + "\n");
                displayArea.append(String.format("Item ID: %d\n", nextId));
                displayArea.append(String.format("Category: %s\n", category));
                displayArea.append(String.format("Name: %s\n", name));
                displayArea.append(String.format("Price: $%.2f\n", price));
                
                pstmt.close();
                stmt.close();
                
            } catch (NumberFormatException e) {
                displayArea.append("\nERROR: Invalid price format. Please enter a valid number.\n");
            } catch (Exception e) {
                displayArea.append("\nERROR: " + e.getMessage() + "\n");
                e.printStackTrace();
            }
        }
    }
    
    private void updateMenuItemPrice() {
        try {
            // First, show current menu items
            Statement stmt = conn.createStatement();
            String sql = "SELECT menuitemid, menuitemname, price FROM menuitems ORDER BY menuitemid";
            ResultSet rs = stmt.executeQuery(sql);
            
            StringBuilder menuList = new StringBuilder("Current Menu Items:\n\n");
            while (rs.next()) {
                menuList.append(String.format("ID %d: %s - $%.2f\n",
                    rs.getInt("menuitemid"),
                    rs.getString("menuitemname"),
                    rs.getDouble("price")));
            }
            rs.close();
            stmt.close();
            
            // Ask for item ID and new price
            JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
            
            JTextArea menuArea = new JTextArea(menuList.toString());
            menuArea.setEditable(false);
            menuArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
            JScrollPane scrollPane = new JScrollPane(menuArea);
            scrollPane.setPreferredSize(new Dimension(400, 200));
            
            JTextField idField = new JTextField();
            JTextField priceField = new JTextField();
            
            JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
            inputPanel.add(scrollPane, BorderLayout.NORTH);
            
            JPanel fieldsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
            fieldsPanel.add(new JLabel("Menu Item ID:"));
            fieldsPanel.add(idField);
            fieldsPanel.add(new JLabel("New Price:"));
            fieldsPanel.add(priceField);
            
            inputPanel.add(fieldsPanel, BorderLayout.CENTER);
            
            int result = JOptionPane.showConfirmDialog(this, inputPanel, 
                "Update Menu Item Price", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            
            if (result == JOptionPane.OK_OPTION) {
                displayArea.setText("");
                int itemId = Integer.parseInt(idField.getText().trim());
                double newPrice = Double.parseDouble(priceField.getText().trim());
                
                // Update the price
                String updateSql = "UPDATE menuitems SET price = ? WHERE menuitemid = ?";
                PreparedStatement pstmt = conn.prepareStatement(updateSql);
                pstmt.setDouble(1, newPrice);
                pstmt.setInt(2, itemId);
                
                int rowsAffected = pstmt.executeUpdate();
                
                if (rowsAffected > 0) {
                    // Get updated item details
                    String selectSql = "SELECT * FROM menuitems WHERE menuitemid = ?";
                    PreparedStatement selectStmt = conn.prepareStatement(selectSql);
                    selectStmt.setInt(1, itemId);
                    ResultSet updatedRs = selectStmt.executeQuery();
                    
                    if (updatedRs.next()) {
                        displayArea.append("✓ Menu item price updated successfully!\n");
                        displayArea.append("=".repeat(60) + "\n");
                        displayArea.append(String.format("Item ID: %d\n", itemId));
                        displayArea.append(String.format("Category: %s\n", updatedRs.getString("drinkcategory")));
                        displayArea.append(String.format("Name: %s\n", updatedRs.getString("menuitemname")));
                        displayArea.append(String.format("New Price: $%.2f\n", newPrice));
                    }
                    
                    updatedRs.close();
                    selectStmt.close();
                } else {
                    displayArea.append("\nERROR: Menu item ID not found.\n");
                }
                
                pstmt.close();
            }
            
        } catch (NumberFormatException e) {
            displayArea.setText("");
            displayArea.append("\nERROR: Invalid input format. Please enter valid numbers.\n");
        } catch (Exception e) {
            displayArea.setText("");
            displayArea.append("\nERROR: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    // View all employees
    private void viewEmployees() {
        employeeDisplayArea.setText("");
        try {
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM employees ORDER BY employeeid";
            ResultSet rs = stmt.executeQuery(sql);
            
            employeeDisplayArea.append("EMPLOYEES\n");
            employeeDisplayArea.append("=".repeat(80) + "\n");
            employeeDisplayArea.append(String.format("%-12s %-25s %-20s %-15s\n",
                "Employee ID", "Employee Name", "Role", "Hours Worked"));
            employeeDisplayArea.append("-".repeat(80) + "\n");
            
            while (rs.next()) {
                employeeDisplayArea.append(String.format("%-12d %-25s %-20s %-15d\n",
                    rs.getInt("employeeid"),
                    rs.getString("employeename"),
                    rs.getString("employeerole"),
                    rs.getInt("hoursworked")));
            }
            
            rs.close();
            stmt.close();
        } catch (Exception e) {
            employeeDisplayArea.append("\nERROR: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    // Add new employee
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
                
                // Get the next employeeid
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT MAX(employeeid) as maxid FROM employees");
                int nextId = 1;
                if (rs.next() && rs.getObject("maxid") != null) {
                    nextId = rs.getInt("maxid") + 1;
                }
                rs.close();
                
                // Insert new employee
                String sql = "INSERT INTO employees (employeeid, employeename, employeerole, hoursworked) VALUES (?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, nextId);
                pstmt.setString(2, name);
                pstmt.setString(3, role);
                pstmt.setInt(4, hours);
                
                pstmt.executeUpdate();
                
                employeeDisplayArea.append("✓ Employee added successfully!\n");
                employeeDisplayArea.append("=".repeat(60) + "\n");
                employeeDisplayArea.append(String.format("Employee ID: %d\n", nextId));
                employeeDisplayArea.append(String.format("Name: %s\n", name));
                employeeDisplayArea.append(String.format("Role: %s\n", role));
                employeeDisplayArea.append(String.format("Hours Worked: %d\n", hours));
                
                pstmt.close();
                stmt.close();
                
            } catch (NumberFormatException e) {
                employeeDisplayArea.append("\nERROR: Invalid hours format. Please enter a valid number.\n");
            } catch (Exception e) {
                employeeDisplayArea.append("\nERROR: " + e.getMessage() + "\n");
                e.printStackTrace();
            }
        }
    }

    // Update employee information
    private void updateEmployee() {
        try {
            // First, show current employees
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM employees ORDER BY employeeid";
            ResultSet rs = stmt.executeQuery(sql);
            
            StringBuilder employeeList = new StringBuilder("Current Employees:\n\n");
            while (rs.next()) {
                employeeList.append(String.format("ID %d: %s - %s (Hours: %d)\n",
                    rs.getInt("employeeid"),
                    rs.getString("employeename"),
                    rs.getString("employeerole"),
                    rs.getInt("hoursworked")));
            }
            rs.close();
            stmt.close();
            
            // Create update dialog
            JTextArea employeeArea = new JTextArea(employeeList.toString());
            employeeArea.setEditable(false);
            employeeArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
            JScrollPane scrollPane = new JScrollPane(employeeArea);
            scrollPane.setPreferredSize(new Dimension(500, 200));
            
            JTextField idField = new JTextField();
            JTextField nameField = new JTextField();
            JTextField roleField = new JTextField();
            JTextField hoursField = new JTextField();
            
            JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
            inputPanel.add(scrollPane, BorderLayout.NORTH);
            
            JPanel fieldsPanel = new JPanel(new GridLayout(4, 2, 10, 10));
            fieldsPanel.add(new JLabel("Employee ID:"));
            fieldsPanel.add(idField);
            fieldsPanel.add(new JLabel("New Name (leave empty to keep):"));
            fieldsPanel.add(nameField);
            fieldsPanel.add(new JLabel("New Role (leave empty to keep):"));
            fieldsPanel.add(roleField);
            fieldsPanel.add(new JLabel("New Hours (leave empty to keep):"));
            fieldsPanel.add(hoursField);
            
            inputPanel.add(fieldsPanel, BorderLayout.CENTER);
            
            int result = JOptionPane.showConfirmDialog(this, inputPanel, 
                "Update Employee", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            
            if (result == JOptionPane.OK_OPTION) {
                employeeDisplayArea.setText("");
                int employeeId = Integer.parseInt(idField.getText().trim());
                
                // Get current employee data
                String selectSql = "SELECT * FROM employees WHERE employeeid = ?";
                PreparedStatement selectStmt = conn.prepareStatement(selectSql);
                selectStmt.setInt(1, employeeId);
                ResultSet currentRs = selectStmt.executeQuery();
                
                if (!currentRs.next()) {
                    employeeDisplayArea.append("\nERROR: Employee ID not found.\n");
                    currentRs.close();
                    selectStmt.close();
                    return;
                }
                
                // Get values (use current if new is empty)
                String newName = nameField.getText().trim();
                if (newName.isEmpty()) {
                    newName = currentRs.getString("employeename");
                }
                
                String newRole = roleField.getText().trim();
                if (newRole.isEmpty()) {
                    newRole = currentRs.getString("employeerole");
                }
                
                int newHours = currentRs.getInt("hoursworked");
                if (!hoursField.getText().trim().isEmpty()) {
                    newHours = Integer.parseInt(hoursField.getText().trim());
                }
                
                currentRs.close();
                selectStmt.close();
                
                // Update employee
                String updateSql = "UPDATE employees SET employeename = ?, employeerole = ?, hoursworked = ? WHERE employeeid = ?";
                PreparedStatement pstmt = conn.prepareStatement(updateSql);
                pstmt.setString(1, newName);
                pstmt.setString(2, newRole);
                pstmt.setInt(3, newHours);
                pstmt.setInt(4, employeeId);
                
                pstmt.executeUpdate();
                
                employeeDisplayArea.append("✓ Employee updated successfully!\n");
                employeeDisplayArea.append("=".repeat(60) + "\n");
                employeeDisplayArea.append(String.format("Employee ID: %d\n", employeeId));
                employeeDisplayArea.append(String.format("Name: %s\n", newName));
                employeeDisplayArea.append(String.format("Role: %s\n", newRole));
                employeeDisplayArea.append(String.format("Hours Worked: %d\n", newHours));
                
                pstmt.close();
            }
            
        } catch (NumberFormatException e) {
            employeeDisplayArea.setText("");
            employeeDisplayArea.append("\nERROR: Invalid input format. Please enter valid numbers.\n");
        } catch (Exception e) {
            employeeDisplayArea.setText("");
            employeeDisplayArea.append("\nERROR: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    // Remove employee
    private void removeEmployee() {
        try {
            // First, show current employees
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM employees ORDER BY employeeid";
            ResultSet rs = stmt.executeQuery(sql);
            
            StringBuilder employeeList = new StringBuilder("Current Employees:\n\n");
            while (rs.next()) {
                employeeList.append(String.format("ID %d: %s - %s\n",
                    rs.getInt("employeeid"),
                    rs.getString("employeename"),
                    rs.getString("employeerole")));
            }
            rs.close();
            stmt.close();
            
            // Ask for employee ID to remove
            JTextArea employeeArea = new JTextArea(employeeList.toString());
            employeeArea.setEditable(false);
            employeeArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
            JScrollPane scrollPane = new JScrollPane(employeeArea);
            scrollPane.setPreferredSize(new Dimension(400, 200));
            
            JTextField idField = new JTextField();
            
            JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
            inputPanel.add(scrollPane, BorderLayout.NORTH);
            
            JPanel fieldPanel = new JPanel(new GridLayout(1, 2, 10, 10));
            fieldPanel.add(new JLabel("Employee ID to Remove:"));
            fieldPanel.add(idField);
            
            inputPanel.add(fieldPanel, BorderLayout.CENTER);
            
            int result = JOptionPane.showConfirmDialog(this, inputPanel, 
                "Remove Employee", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            
            if (result == JOptionPane.OK_OPTION) {
                employeeDisplayArea.setText("");
                int employeeId = Integer.parseInt(idField.getText().trim());
                
                // Get employee info before deletion
                String selectSql = "SELECT * FROM employees WHERE employeeid = ?";
                PreparedStatement selectStmt = conn.prepareStatement(selectSql);
                selectStmt.setInt(1, employeeId);
                ResultSet empRs = selectStmt.executeQuery();
                
                if (!empRs.next()) {
                    employeeDisplayArea.append("\nERROR: Employee ID not found.\n");
                    empRs.close();
                    selectStmt.close();
                    return;
                }
                
                String empName = empRs.getString("employeename");
                String empRole = empRs.getString("employeerole");
                
                empRs.close();
                selectStmt.close();
                
                // Confirm deletion
                int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to remove:\n" + empName + " (" + empRole + ")?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    // Delete employee
                    String deleteSql = "DELETE FROM employees WHERE employeeid = ?";
                    PreparedStatement pstmt = conn.prepareStatement(deleteSql);
                    pstmt.setInt(1, employeeId);
                    
                    int rowsAffected = pstmt.executeUpdate();
                    
                    if (rowsAffected > 0) {
                        employeeDisplayArea.append("✓ Employee removed successfully!\n");
                        employeeDisplayArea.append("=".repeat(60) + "\n");
                        employeeDisplayArea.append(String.format("Removed: %s (ID: %d)\n", empName, employeeId));
                    }
                    
                    pstmt.close();
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
            e.printStackTrace();
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

    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BobaShopManagerGUI());
    }
}