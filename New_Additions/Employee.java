package New_Additions;

/**
 * Employee model class representing employees in the boba shop.
 * Based on the Employees table schema.
 * 
 * @author harry
 * @version 1.0
 * @since 2024
 */
public class Employee {
    private int employeeID;
    private String employeeName;
    private String employeeRole;
    private int hoursWorked;

    /**
     * Default constructor for Employee.
     * 
     * @author harry
     */
    public Employee() {
    }

    /**
     * Constructs a new Employee with specified parameters.
     * 
     * @param employeeID the unique identifier for the employee
     * @param employeeName the name of the employee
     * @param employeeRole the role/position of the employee
     * @param hoursWorked the number of hours worked by the employee
     * @author harry
     */
    public Employee(int employeeID, String employeeName, String employeeRole, int hoursWorked) {
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.employeeRole = employeeRole;
        this.hoursWorked = hoursWorked;
    }

    /**
     * Gets the employee ID.
     * 
     * @return the employee ID
     * @author harry
     */
    public int getEmployeeID() {
        return employeeID;
    }

    /**
     * Sets the employee ID.
     * 
     * @param employeeID the employee ID to set
     * @author harry
     */
    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    /**
     * Gets the employee name.
     * 
     * @return the employee name
     * @author harry
     */
    public String getEmployeeName() {
        return employeeName;
    }

    /**
     * Sets the employee name.
     * 
     * @param employeeName the employee name to set
     * @author harry
     */
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    /**
     * Gets the employee role.
     * 
     * @return the employee role
     * @author harry
     */
    public String getEmployeeRole() {
        return employeeRole;
    }

    /**
     * Sets the employee role.
     * 
     * @param employeeRole the employee role to set
     * @author harry
     */
    public void setEmployeeRole(String employeeRole) {
        this.employeeRole = employeeRole;
    }

    /**
     * Gets the hours worked by the employee.
     * 
     * @return the hours worked
     * @author harry
     */
    public int getHoursWorked() {
        return hoursWorked;
    }

    /**
     * Sets the hours worked by the employee.
     * 
     * @param hoursWorked the hours worked to set
     * @author harry
     */
    public void setHoursWorked(int hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    /**
     * Returns a string representation of the Employee.
     * 
     * @return a formatted string containing employee name, role, and hours worked
     * @author harry
     */
    @Override
    public String toString() {
        return employeeName + " (" + employeeRole + ") - " + hoursWorked + " hours";
    }

    /**
     * Compares this Employee with another object for equality.
     * 
     * @param obj the object to compare with
     * @return true if the objects are equal, false otherwise
     * @author harry
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Employee employee = (Employee) obj;
        return employeeID == employee.employeeID;
    }

    /**
     * Returns a hash code for this Employee.
     * 
     * @return a hash code value for this object
     * @author harry
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(employeeID);
    }
}