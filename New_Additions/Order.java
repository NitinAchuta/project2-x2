package New_Additions;

import java.sql.Timestamp;

/**
 * Order model class representing customer orders in the boba shop.
 * 
 * This class serves as a data model for customer orders in the POS system,
 * corresponding to the Orders table in the database schema. It encapsulates
 * all the essential information about an order including its unique identifier,
 * timestamp, customer association, employee who processed it, total cost, and
 * the week number for reporting purposes.
 * 
 * The class supports both registered customers (with customerID) and walk-in
 * customers (customerID can be null). It provides proper encapsulation through
 * getter and setter methods, implements standard Object methods for comparison
 * and string representation, and follows JavaBean conventions.
 * 
 */
public class Order {
    /** The unique identifier for the order in the database */
    private int orderID;

    /** The timestamp when the order was placed */
    private Timestamp timeOfOrder;

    /** The customer ID (nullable for walk-in customers) */
    private Integer customerID; // Nullable for walk-in customers

    /** The ID of the employee who processed this order */
    private int employeeID;

    /** The total cost of the order in dollars */
    private double totalCost;

    /** The week number for reporting and analytics purposes */
    private int orderWeek;

    // Constructors
    /**
     * Default constructor for Order.
     * 
     * Creates a new Order instance with default values. This constructor
     * is useful for frameworks that require a no-argument constructor or
     * when creating objects that will be populated later through setter methods.
     */
    public Order() {
    }

    /**
     * Parameterized constructor for Order.
     * 
     * Creates a new Order instance with all fields initialized to the
     * provided values. This constructor is useful for creating fully
     * populated Order objects from database records or when processing
     * new orders in the POS system.
     * 
     * @param orderID     the unique identifier for the order
     * @param timeOfOrder the timestamp when the order was placed
     * @param customerID  the customer ID (can be null for walk-in customers)
     * @param employeeID  the ID of the employee processing the order
     * @param totalCost   the total cost of the order in dollars
     * @param orderWeek   the week number for reporting purposes
     */
    public Order(int orderID, Timestamp timeOfOrder, Integer customerID,
            int employeeID, double totalCost, int orderWeek) {
        this.orderID = orderID;
        this.timeOfOrder = timeOfOrder;
        this.customerID = customerID;
        this.employeeID = employeeID;
        this.totalCost = totalCost;
        this.orderWeek = orderWeek;
    }

    // Getters and Setters
    /**
     * Gets the order ID.
     * 
     * @return the unique identifier for this order
     */
    public int getOrderID() {
        return orderID;
    }

    /**
     * Sets the order ID.
     * 
     * @param orderID the unique identifier to set for this order
     */
    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    /**
     * Gets the time when the order was placed.
     * 
     * @return the timestamp of when this order was placed
     */
    public Timestamp getTimeOfOrder() {
        return timeOfOrder;
    }

    /**
     * Sets the time when the order was placed.
     * 
     * @param timeOfOrder the timestamp to set for when this order was placed
     */
    public void setTimeOfOrder(Timestamp timeOfOrder) {
        this.timeOfOrder = timeOfOrder;
    }

    /**
     * Gets the customer ID associated with this order.
     * 
     * @return the customer ID, or null if this is a walk-in customer
     */
    public Integer getCustomerID() {
        return customerID;
    }

    /**
     * Sets the customer ID for this order.
     * 
     * @param customerID the customer ID to associate with this order,
     *                   or null for walk-in customers
     */
    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    /**
     * Gets the employee ID who processed this order.
     * 
     * @return the ID of the employee who processed this order
     */
    public int getEmployeeID() {
        return employeeID;
    }

    /**
     * Sets the employee ID who processed this order.
     * 
     * @param employeeID the ID of the employee who processed this order
     */
    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    /**
     * Gets the total cost of the order.
     * 
     * @return the total cost of this order in dollars
     */
    public double getTotalCost() {
        return totalCost;
    }

    /**
     * Sets the total cost of the order.
     * 
     * @param totalCost the total cost to set for this order in dollars
     */
    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    /**
     * Gets the order week number.
     * 
     * @return the week number when this order was placed, used for reporting
     */
    public int getOrderWeek() {
        return orderWeek;
    }

    /**
     * Sets the order week number.
     * 
     * @param orderWeek the week number to set for this order, used for reporting
     */
    public void setOrderWeek(int orderWeek) {
        this.orderWeek = orderWeek;
    }

    /**
     * Returns a string representation of this Order.
     * 
     * The string format is "Order #orderID - $totalCost (timeOfOrder)" with
     * the total cost formatted to two decimal places. This method is commonly
     * used for display purposes in user interfaces such as order lists or
     * transaction summaries.
     * 
     * @return a formatted string containing the order ID, total cost, and timestamp
     */
    @Override
    public String toString() {
        return "Order #" + orderID + " - $" + String.format("%.2f", totalCost) +
                " (" + timeOfOrder + ")";
    }

    /**
     * Determines whether this Order is equal to another object.
     * 
     * Two Order objects are considered equal if they have the same orderID.
     * This follows the principle that the primary key uniquely identifies
     * each order in the database, making it the appropriate basis for equality.
     * 
     * @param obj the object to compare with this Order
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Order order = (Order) obj;
        return orderID == order.orderID;
    }

    /**
     * Returns a hash code value for this Order.
     * 
     * The hash code is based on the orderID, which is the unique identifier
     * for each order. This ensures that equal Order objects have the same
     * hash code, maintaining the contract between equals() and hashCode().
     * This is important for proper behavior in hash-based collections.
     * 
     * @return a hash code value for this Order
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(orderID);
    }
}