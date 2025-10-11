\copy orders (
  order_id,
  datetime,
  customer_id,
  employee_id,
  item_id,
  sugar_content_pct,
  boba_content_pct,
  ice_content_pct,
  total_amount
) FROM 'order.csv' WITH (FORMAT csv, HEADER true);
gang_x2_db=> \copy orders (
  order_id,
  datetime,
  customer_id,
  employee_id,
  item_id,
  sugar_content_pct,
  boba_content_pct,
  ice_content_pct,
  total_amount
) FROM 'order.csv' WITH (FORMAT csv, HEADER true);
