INSERT INTO orders (user_id,order_date,total,status,payment_method) VALUES
(1,'2026-05-14 21:30:00',1530,"PENDING","PAYPAL");

INSERT INTO order_item (order_id,product_id,quantity) VALUES
(1,1,1),
(1,2,3),
(1,3,1);