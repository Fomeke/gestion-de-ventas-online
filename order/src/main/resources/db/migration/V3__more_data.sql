INSERT INTO orders(user_id,order_date,total,status,payment_method) VALUES
(1, '2026-05-15 00:00:00',3300,"PENDING","DEBIT_CARD");

INSERT INTO order_item (order_id, product_id, quantity) VALUES
(2,1,3),
(2,2,3);