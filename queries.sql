-- 1. count of orders grouped by week
SELECT COUNT(*) FROM orders GROUP BY week;

-- 2. count of orders grouped by hour
SELECT
  EXTRACT(HOUR FROM "datetime") AS hour_of_day,
  COUNT(*)
FROM orders
GROUP BY hour_of_day
ORDER BY hour_of_day;

-- 3. peak sales day
SELECT
    EXTRACT(DAY FROM "datetime") AS _date,
    SUM(total_amount) AS total
FROM orders 
GROUP BY _date
ORDER BY _date
LIMIT 10;

--4. Menu Item Inventory


--5. Best of the Worst