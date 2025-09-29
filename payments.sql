CREATE TABLE IF NOT EXISTS payments (
    paymentID int PRIMARY KEY,
    order_id int REFERENCES orders(order_id),
    paymentMethod varchar(255),
    amount decimal,
    status varchar(255)
);
