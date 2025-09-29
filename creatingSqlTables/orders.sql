-- Orders Table - Nitin Achuta
CREATE TABLE Orders (
    orderID INT PRIMARY KEY,
    timeOfOrder TIMESTAMP NOT NULL,
    customerID INT,
    employeeID INT,
    totalCost DECIMAL(8,2) NOT NULL,
    orderWeek INT,
    FOREIGN KEY (customerID) REFERENCES Customers(customerID),
    FOREIGN KEY (employeeID) REFERENCES Employees(employeeID)
);
