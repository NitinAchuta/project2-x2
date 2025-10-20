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
}
