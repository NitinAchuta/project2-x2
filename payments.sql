CREATE TABLE Payments (
    paymentID INT PRIMARY KEY,
    orderID INT,
    paymentMethod VARCHAR(50),
    status VARCHAR(50),
    FOREIGN KEY (orderID) REFERENCES Orders(orderID)
);