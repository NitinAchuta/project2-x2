CREATE TABLE MenuItemIngredients (
    menuItemIngredientID INT PRIMARY KEY,
    menuItemID INT,
    ingredientID INT,
    ingredientQty INT NOT NULL,
    FOREIGN KEY (menuItemID) REFERENCES MenuItems(menuItemID),
    FOREIGN KEY (ingredientID) REFERENCES Inventory(ingredientID)
);

