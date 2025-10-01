-- top 5 best selling drinks, by qty
SELECT
  MenuItems.menuItemName,
  SUM(OrderItems.quantity) AS total_qty
FROM OrderItems
JOIN MenuItems ON OrderItems.menuItemID = MenuItems.menuItemID
GROUP BY MenuItems.menuItemName
ORDER BY total_qty DESC
LIMIT 5;

-- ingedients that are out of stock
SELECT
  ingredientID,
  ingredientName,
  ingredientCount
FROM Inventory
WHERE ingredientCount = 0; 

-- average average order cost
SELECT AVG(Orders.totalCost) AS avg_order_cost
FROM Orders;

-- sugar level popularity
SELECT
  sugarLevel,
  SUM(quantity) AS drinks_count
FROM OrderItems
GROUP BY sugarLevel
ORDER BY drinks_count DESC;


-- ice level pop 
SELECT
  iceLevel,
  SUM(quantity) AS drinks_count
FROM OrderItems
GROUP BY iceLevel
ORDER BY drinks_count DESC;

-- number of orders so far today
SELECT COUNT(*) AS orders_today
FROM Orders
WHERE DATE(timeOfOrder) = CURRENT_DATE;

-- revenue today
SELECT SUM(totalCost) AS revenue_today
FROM Orders
WHERE DATE(timeOfOrder) = CURRENT_DATE;

-- 5 most frequent customers
SELECT customerID, COUNT(*) AS order_count
FROM Orders
GROUP BY customerID
ORDER BY order_count DESC
LIMIT 5;

-- total revenue all time
SELECT SUM(totalCost) AS total_revenue
FROM Orders;

-- 5 worst drinks
SELECT
  MenuItems.menuItemName,
  SUM(OrderItems.quantity) AS total_qty
FROM OrderItems
JOIN MenuItems ON OrderItems.menuItemID = MenuItems.menuItemID
GROUP BY MenuItems.menuItemName
ORDER BY total_qty ASC
LIMIT 5;

-- yearly revenue
SELECT
  EXTRACT(YEAR FROM timeOfOrder) AS year,
  SUM(totalCost) AS revenue
FROM Orders
GROUP BY year
ORDER BY year;

