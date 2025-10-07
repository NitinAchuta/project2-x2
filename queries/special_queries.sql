-- 1. count of orders grouped by week
SELECT orderWeek, COUNT(*) FROM orders GROUP BY orderWeek;

-- 2. count of orders grouped by hour
SELECT
  EXTRACT(HOUR FROM "timeoforder") AS hour_of_day,
  COUNT(*) AS orders,
  SUM(totalcost) AS cost
FROM orders
GROUP BY hour_of_day
ORDER BY hour_of_day;

-- 3. peak sales day
SELECT
    "timeoforder"::date AS order_date,
    SUM(totalcost) AS total
FROM orders 
GROUP BY order_date
ORDER BY total DESC
LIMIT 10;

--4. Menu Item Inventory 
SELECT mi.menuitemid, menuitemname, COUNT(*)
FROM MenuItemIngredients m
INNER JOIN MenuItems mi ON m.menuitemid = mi.menuitemid
GROUP BY mi.menuitemid;