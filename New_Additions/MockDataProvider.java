package New_Additions;

import java.sql.Timestamp;
import java.util.*;

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

    public MockDataProvider() {
        initializeMockData();
    }

    private void initializeMockData() {
        initializeMenuItems();
        initializeInventory();
        initializeEmployees();
        initializeOrders();
    }

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
    private int getCurrentWeek() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.WEEK_OF_YEAR);
    }
}
