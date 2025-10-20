package New_Additions;

/**
 * Employee model class representing employees in the boba shop
 * Based on the Employees table schema
 */
public class Employee {
    private int employeeID;
    private String employeeName;
    private String employeeRole;
    private int hoursWorked;

    // Constructors
    public Employee() {
    }

    public Employee(int employeeID, String employeeName, String employeeRole, int hoursWorked) {
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.employeeRole = employeeRole;
        this.hoursWorked = hoursWorked;
    }

    // Getters and Setters
    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeRole() {
        return employeeRole;
    }

    public void setEmployeeRole(String employeeRole) {
        this.employeeRole = employeeRole;
    }

    public int getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(int hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    @Override
    public String toString() {
        return employeeName + " (" + employeeRole + ") - " + hoursWorked + " hours";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Employee employee = (Employee) obj;
        return employeeID == employee.employeeID;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(employeeID);
    }
}