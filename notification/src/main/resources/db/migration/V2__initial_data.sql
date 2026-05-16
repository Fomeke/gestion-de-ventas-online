INSERT INTO notification (user_id, type, subject, message, order_id) VALUES
(1, 'EMAIL', 'Pedido confirmado', 'Tu pedido #1 ha sido confirmado y será enviado pronto', 1),
(2, 'EMAIL', 'Pago recibido', 'Hemos recibido tu pago para el pedido #2', 2),
(1, 'SMS', 'Pedido enviado', 'Tu pedido #3 ha sido enviado. Número de seguimiento disponible', 3),
(3, 'EMAIL', 'Entrega en camino', 'Tu pedido #4 llegará hoy entre las 14:00 y 18:00', 4),
(2, 'SMS', 'Pedido entregado', 'Tu pedido #5 ha sido entregado exitosamente', 5);