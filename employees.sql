CREATE TABLE Employees (
    employeeID INT PRIMARY KEY,
    employeeName VARCHAR(100) NOT NULL,
    employeeRole VARCHAR(50),
    hoursWorked INT DEFAULT 0
);
