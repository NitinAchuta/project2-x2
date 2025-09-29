DROP TABLE IF EXISTS Inventory;

CREATE TABLE Inventory (
    ingredientID INT PRIMARY KEY,
    ingredientName VARCHAR(100) NOT NULL,
    ingredientCount INT NOT NULL
);
