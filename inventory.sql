DROP TABLE IF EXISTS Inventory;

CREATE TABLE Inventory (
    IngredientID INT PRIMARY KEY,
    IngredientName VARCHAR(100) NOT NULL,
    Quantity INT NOT NULL,
    Unit VARCHAR(50) NOT NULL
);
