package New_Additions;

import java.sql.Timestamp;
import java.util.*;

/**
 * MockDataProvider provides mock data for testing and development purposes.
 * This class simulates database operations when a real database connection
 * is not available.
 * 
 * @author harry
 * @version 1.0
 * @since 2024
 */
public class MockDataProvider {
    private List<MenuItem> menuItems;
    private List<Inventory> inventory;
    private List<Employee> employees;
    private List<Order> orders;
    private List<OrderItem> orderItems;

    private int nextMenuItemId = 1;
    private int nextInventoryId = 1;
    private int nextEmployeeId = 1;
    private int nextOrderId = 1;
    private int nextOrderItemId = 1;

    /**
     * Constructs a new MockDataProvider and initializes mock data.
     * 
     * @author harry
     */
    public MockDataProvider() {
        initializeMockData();
    }

    /**
     * Initializes all mock data collections.
     * 
     * @author harry
     */
    private void initializeMockData() {
        initializeMenuItems();
        initializeInventory();
        initializeEmployees();
        initializeOrders();
    }

    /**
     * Initializes mock menu items data.
     * 
     * @author harry
     */
    private void initializeMenuItems() {
        menuItems = new ArrayList<>();

        // Milk Tea category
        menuItems.add(new MenuItem(nextMenuItemId++, "Milk Tea", "Classic Milk Tea", 4.50));
        menuItems.add(new MenuItem(nextMenuItemId++, "Milk Tea", "Taro Milk Tea", 5.00));
        menuItems.add(new MenuItem(nextMenuItemId++, "Milk Tea", "Thai Milk Tea", 4.75));
        menuItems.add(new MenuItem(nextMenuItemId++, "Milk Tea", "Matcha Milk Tea", 5.25));
        menuItems.add(new MenuItem(nextMenuItemId++, "Milk Tea", "Honeydew Milk Tea", 4.75));

        // Fruit Tea category
        menuItems.add(new MenuItem(nextMenuItemId++, "Fruit Tea", "Passion Fruit Tea", 4.25));
        menuItems.add(new MenuItem(nextMenuItemId++, "Fruit Tea", "Mango Green Tea", 4.50));
        menuItems.add(new MenuItem(nextMenuItemId++, "Fruit Tea", "Lychee Black Tea", 4.25));
        menuItems.add(new MenuItem(nextMenuItemId++, "Fruit Tea", "Peach Oolong Tea", 4.75));
        menuItems.add(new MenuItem(nextMenuItemId++, "Fruit Tea", "Strawberry Tea", 4.50));

        // Coffee category
        menuItems.add(new MenuItem(nextMenuItemId++, "Coffee", "Iced Coffee", 3.75));
        menuItems.add(new MenuItem(nextMenuItemId++, "Coffee", "Coffee Milk Tea", 5.00));
        menuItems.add(new MenuItem(nextMenuItemId++, "Coffee", "Caramel Macchiato", 5.50));

        // Smoothie category
        menuItems.add(new MenuItem(nextMenuItemId++, "Smoothie", "Mango Smoothie", 5.25));
        menuItems.add(new MenuItem(nextMenuItemId++, "Smoothie", "Avocado Smoothie", 5.50));
        menuItems.add(new MenuItem(nextMenuItemId++, "Smoothie", "Taro Smoothie", 5.25));

        // Specialty category
        menuItems.add(new MenuItem(nextMenuItemId++, "Specialty", "Brown Sugar Boba", 6.00));
        menuItems.add(new MenuItem(nextMenuItemId++, "Specialty", "Cheese Foam Tea", 5.75));
        menuItems.add(new MenuItem(nextMenuItemId++, "Specialty", "Dirty Boba", 6.25));
        menuItems.add(new MenuItem(nextMenuItemId++, "Specialty", "Seasonal Special", 6.50));
    }

    /**
     * Initializes mock inventory data.
     * 
     * @author harry
     */
    private void initializeInventory() {
        inventory = new ArrayList<>();

        // Tea bases
        inventory.add(new Inventory(nextInventoryId++, "Black Tea", 150));
        inventory.add(new Inventory(nextInventoryId++, "Green Tea", 120));
        inventory.add(new Inventory(nextInventoryId++, "Oolong Tea", 100));
        inventory.add(new Inventory(nextInventoryId++, "White Tea", 80));

        // Milk and dairy
        inventory.add(new Inventory(nextInventoryId++, "Whole Milk", 200));
        inventory.add(new Inventory(nextInventoryId++, "Almond Milk", 75));
        inventory.add(new Inventory(nextInventoryId++, "Coconut Milk", 60));
        inventory.add(new Inventory(nextInventoryId++, "Oat Milk", 45));

        // Sweeteners
        inventory.add(new Inventory(nextInventoryId++, "Cane Sugar", 300));
        inventory.add(new Inventory(nextInventoryId++, "Brown Sugar", 150));
        inventory.add(new Inventory(nextInventoryId++, "Honey", 80));

        // Fruits and flavors
        inventory.add(new Inventory(nextInventoryId++, "Mango Syrup", 90));
        inventory.add(new Inventory(nextInventoryId++, "Strawberry Syrup", 85));
        inventory.add(new Inventory(nextInventoryId++, "Passion Fruit Syrup", 70));
        inventory.add(new Inventory(nextInventoryId++, "Lychee Syrup", 65));
        inventory.add(new Inventory(nextInventoryId++, "Taro Powder", 110));
        inventory.add(new Inventory(nextInventoryId++, "Matcha Powder", 95));

        // Toppings
        inventory.add(new Inventory(nextInventoryId++, "Tapioca Pearls (Boba)", 500));
        inventory.add(new Inventory(nextInventoryId++, "Lychee Jelly", 200));
        inventory.add(new Inventory(nextInventoryId++, "Grass Jelly", 180));
        inventory.add(new Inventory(nextInventoryId++, "Pudding", 150));
        inventory.add(new Inventory(nextInventoryId++, "Aloe Vera", 120));
        inventory.add(new Inventory(nextInventoryId++, "Red Bean", 100));
        inventory.add(new Inventory(nextInventoryId++, "Popping Boba (Mango)", 300));
        inventory.add(new Inventory(nextInventoryId++, "Popping Boba (Strawberry)", 280));
        inventory.add(new Inventory(nextInventoryId++, "Crystal Boba", 250));

        // Supplies
        inventory.add(new Inventory(nextInventoryId++, "Plastic Cups (16oz)", 1000));
        inventory.add(new Inventory(nextInventoryId++, "Plastic Cups (20oz)", 800));
        inventory.add(new Inventory(nextInventoryId++, "Plastic Lids", 1200));
        inventory.add(new Inventory(nextInventoryId++, "Straws", 2000));
        inventory.add(new Inventory(nextInventoryId++, "Cup Sleeves", 500));
        inventory.add(new Inventory(nextInventoryId++, "Napkins", 800));
    }

    /**
     * Initializes mock employees data.
     * 
     * @author harry
     */
    private void initializeEmployees() {
        employees = new ArrayList<>();

        employees.add(new Employee(nextEmployeeId++, "John Smith", "Manager", 160));
        employees.add(new Employee(nextEmployeeId++, "Sarah Johnson", "Assistant Manager", 140));
        employees.add(new Employee(nextEmployeeId++, "Mike Chen", "Cashier", 120));
        employees.add(new Employee(nextEmployeeId++, "Emily Davis", "Cashier", 100));
        employees.add(new Employee(nextEmployeeId++, "Alex Rodriguez", "Barista", 110));
        employees.add(new Employee(nextEmployeeId++, "Lisa Wang", "Barista", 95));
        employees.add(new Employee(nextEmployeeId++, "David Kim", "Part-time Cashier", 60));
        employees.add(new Employee(nextEmployeeId++, "Jennifer Lee", "Part-time Barista", 45));
    }

    /**
     * Initializes mock orders and order items data.
     * 
     * @author harry
     */
    private void initializeOrders() {
        orders = new ArrayList<>();
        orderItems = new ArrayList<>();

        // Sample orders for demonstration
        long currentTime = System.currentTimeMillis();

        // Order 1
        Order order1 = new Order(nextOrderId++, new Timestamp(currentTime - 3600000), null, 3, 9.50, getCurrentWeek());
        orders.add(order1);
        orderItems.add(new OrderItem(nextOrderItemId++, order1.getOrderID(), 1, 2)); // 2x Classic Milk Tea

        // Order 2
        Order order2 = new Order(nextOrderId++, new Timestamp(currentTime - 1800000), null, 4, 5.25, getCurrentWeek());
        orders.add(order2);
        orderItems.add(new OrderItem(nextOrderItemId++, order2.getOrderID(), 14, 1)); // 1x Mango Smoothie

        // Order 3
        Order order3 = new Order(nextOrderId++, new Timestamp(currentTime - 900000), null, 3, 12.00, getCurrentWeek());
        orders.add(order3);
        orderItems.add(new OrderItem(nextOrderItemId++, order3.getOrderID(), 17, 2)); // 2x Brown Sugar Boba
    }

    /**
     * Retrieves all mock menu items.
     * 
     * @return List of MenuItem objects containing all mock menu items
     * @author harry
     */
    public List<MenuItem> getAllMenuItems() {
        return new ArrayList<>(menuItems);
    }

    /**
     * Adds a new menu item to the mock data.
     * 
     * @param item the MenuItem object to be added
     * @return always true for mock data operations
     * @author harry
     */
    public boolean addMenuItem(MenuItem item) {
        item.setMenuItemID(nextMenuItemId++);
        menuItems.add(item);
        return true;
    }

    /**
     * Updates the price of a menu item in mock data.
     * 
     * @param menuItemId the ID of the menu item to update
     * @param newPrice the new price for the menu item
     * @return true if menu item was found and updated, false otherwise
     * @author harry
     */
    public boolean updateMenuItemPrice(int menuItemId, double newPrice) {
        for (MenuItem item : menuItems) {
            if (item.getMenuItemID() == menuItemId) {
                item.setPrice(newPrice);
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves all mock inventory items.
     * 
     * @return List of Inventory objects containing all mock inventory items
     * @author harry
     */
    public List<Inventory> getAllInventory() {
        return new ArrayList<>(inventory);
    }

    /**
     * Adds a new inventory item to the mock data.
     * 
     * @param item the Inventory object to be added
     * @return always true for mock data operations
     * @author harry
     */
    public boolean addInventoryItem(Inventory item) {
        item.setIngredientID(nextInventoryId++);
        inventory.add(item);
        return true;
    }

    /**
     * Updates the quantity of an inventory item in mock data.
     * 
     * @param ingredientId the ID of the inventory item to update
     * @param newQuantity the new quantity for the inventory item
     * @return true if inventory item was found and updated, false otherwise
     * @author harry
     */
    public boolean updateInventoryQuantity(int ingredientId, int newQuantity) {
        for (Inventory item : inventory) {
            if (item.getIngredientID() == ingredientId) {
                item.setIngredientCount(newQuantity);
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves all mock employees.
     * 
     * @return List of Employee objects containing all mock employees
     * @author harry
     */
    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees);
    }

    /**
     * Adds a new employee to the mock data.
     * 
     * @param employee the Employee object to be added
     * @return always true for mock data operations
     * @author harry
     */
    public boolean addEmployee(Employee employee) {
        employee.setEmployeeID(nextEmployeeId++);
        employees.add(employee);
        return true;
    }

    /**
     * Updates an existing employee in mock data.
     * 
     * @param employee the Employee object with updated information
     * @return true if employee was found and updated, false otherwise
     * @author harry
     */
    public boolean updateEmployee(Employee employee) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getEmployeeID() == employee.getEmployeeID()) {
                employees.set(i, employee);
                return true;
            }
        }
        return false;
    }

    /**
     * Deletes an employee from mock data.
     * 
     * @param employeeId the ID of the employee to delete
     * @return true if employee was found and deleted, false otherwise
     * @author harry
     */
    public boolean deleteEmployee(int employeeId) {
        return employees.removeIf(emp -> emp.getEmployeeID() == employeeId);
    }

    /**
     * Retrieves all mock orders.
     * 
     * @return List of Order objects containing all mock orders
     * @author harry
     */
    public List<Order> getAllOrders() {
        return new ArrayList<>(orders);
    }

    /**
     * Retrieves product usage data for charts and analytics.
     * 
     * @return Map with product names as keys and usage counts as values
     * @author harry
     */
    public Map<String, Integer> getProductUsageData() {
        Map<String, Integer> usage = new HashMap<>();

        // Calculate usage based on order items
        Map<Integer, Integer> itemCounts = new HashMap<>();
        for (OrderItem item : orderItems) {
            itemCounts.put(item.getMenuItemID(),
                    itemCounts.getOrDefault(item.getMenuItemID(), 0) + item.getQuantity());
        }

        // Map to menu item names
        for (Map.Entry<Integer, Integer> entry : itemCounts.entrySet()) {
            for (MenuItem item : menuItems) {
                if (item.getMenuItemID() == entry.getKey()) {
                    usage.put(item.getMenuItemName(), entry.getValue());
                    break;
                }
            }
        }

        return usage;
    }

    /**
     * Calculates total sales for a given date range using mock data.
     * 
     * @param startDate the start date for sales calculation
     * @param endDate the end date for sales calculation
     * @return total sales amount for the specified date range
     * @author harry
     */
    public double getTotalSales(java.sql.Date startDate, java.sql.Date endDate) {
        double total = 0.0;

        for (Order order : orders) {
            java.sql.Date orderDate = new java.sql.Date(order.getTimeOfOrder().getTime());
            if ((orderDate.equals(startDate) || orderDate.after(startDate)) &&
                    (orderDate.equals(endDate) || orderDate.before(endDate))) {
                total += order.getTotalCost();
            }
        }

        return total;
    }

    /**
     * Creates a new order with associated order items in mock data.
     * 
     * @param order the Order object to be created
     * @param items List of OrderItem objects for the order
     * @return always true for mock data operations
     * @author harry
     */
    public boolean createOrder(Order order, List<OrderItem> items) {
        order.setOrderID(nextOrderId++);
        orders.add(order);

        for (OrderItem item : items) {
            item.setOrderItemID(nextOrderItemId++);
            item.setOrderID(order.getOrderID());
            orderItems.add(item);
        }

        return true;
    }

    /**
     * Gets the next available ID for a specified table.
     * 
     * @param table the table name to get the next ID for
     * @return the next available ID for the specified table
     * @author harry
     */
    public int getNextId(String table) {
        switch (table.toLowerCase()) {
            case "menuitems":
                return nextMenuItemId;
            case "inventory":
                return nextInventoryId;
            case "employees":
                return nextEmployeeId;
            case "orders":
                return nextOrderId;
            case "orderitems":
                return nextOrderItemId;
            default:
                return 1;
        }
    }
    
    /**
     * Gets the current week number of the year.
     * 
     * @return the current week number
     * @author harry
     */
    private int getCurrentWeek() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * Validates if there's sufficient inventory for an order in mock data mode.
     * 
     * @param orderItems List of OrderItem objects to validate
     * @return true if sufficient inventory exists, false otherwise
     * @author harry
     */
    public boolean validateInventoryForOrder(List<OrderItem> orderItems) {
        // In mock mode, always return true for simplicity
        // In a real implementation, you would check against mock inventory data
        return true;
    }

    /**
     * Updates inventory for an order in mock data mode.
     * 
     * @param orderItems List of OrderItem objects from the completed order
     * @return true if inventory update was successful, false otherwise
     * @author harry
     */
    public boolean updateInventoryForOrder(List<OrderItem> orderItems) {
        // In mock mode, always return true for simplicity
        // In a real implementation, you would update mock inventory data
        System.out.println("Mock: Inventory updated for " + orderItems.size() + " order items");
        return true;
    }
}
