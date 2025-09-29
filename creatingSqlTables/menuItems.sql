-- MenuItems Table - Nitin Achuta
-- Holds the individual drinks 
CREATE TABLE MenuItems (
    menuItemID INT PRIMARY KEY,
    drinkCategory VARCHAR(50),
    menuItemName VARCHAR(100) NOT NULL,
    price DECIMAL(6,2) NOT NULL
);
