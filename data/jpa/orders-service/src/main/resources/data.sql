INSERT INTO products (id, name, price)
VALUES (1,'Drill', 156.34);

INSERT INTO products (id, name, price)
VALUES (2,'Saw', 86.23);

INSERT INTO orders (id)
VALUES (1);

INSERT INTO order_lines (id, product_id, quantity, order_id)
VALUES (1,1,1,1);

INSERT INTO order_lines (id, product_id, quantity, order_id)
VALUES (2,2,5,1);

--INSERT INTO orders_lines (order_id, lines_id)
--VALUES (1,1);

--INSERT INTO orders_lines (order_id, lines_id)
--VALUES (1,2);