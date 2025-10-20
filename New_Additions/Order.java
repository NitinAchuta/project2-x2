package New_Additions;

import java.sql.Timestamp;

/**
 * Order model class representing customer orders in the boba shop
 * Based on the Orders table schema
 */
public class Order {
    private int orderID;
    private Timestamp timeOfOrder;
    private Integer customerID; // Nullable for walk-in customers
    private int employeeID;
    private double totalCost;
    private int orderWeek;

    // Constructors
    public Order() {
    }

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
    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public Timestamp getTimeOfOrder() {
        return timeOfOrder;
    }

    public void setTimeOfOrder(Timestamp timeOfOrder) {
        this.timeOfOrder = timeOfOrder;
    }

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public int getOrderWeek() {
        return orderWeek;
    }

    public void setOrderWeek(int orderWeek) {
        this.orderWeek = orderWeek;
    }

    @Override
    public String toString() {
        return "Order #" + orderID + " - $" + String.format("%.2f", totalCost) +
                " (" + timeOfOrder + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Order order = (Order) obj;
        return orderID == order.orderID;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(orderID);
    }
}