-- 02_fixtures_and_queries.sql
BEGIN;

-- nettoyage minimal si tu relances souvent (à ajuster selon contraintes FK)
DELETE FROM order_line;
DELETE FROM orders;
DELETE FROM forfait;
DELETE FROM customer;

-- fixtures
INSERT INTO customer(id, name, credit_card) VALUES (1, 'Clienta', '1234567890');
INSERT INTO orders(id, price, state, customer_id) VALUES (10, 4200, 'PAID', 1);
INSERT INTO order_line(id, order_id, quantity, unit_price, product_code) VALUES
                                                                             (100, 10, 1, 2000, 'P1'),
                                                                             (101, 10, 1, 2200, 'P2');
INSERT INTO forfait(id, valid_date, age_category, pass_type, customer_id)
VALUES (50, '2026-02-10', 'ADULT', 'FULL', 1);

-- asserts (si count != attendu -> exception)
DO $$
DECLARE c int;
BEGIN
SELECT count(*) INTO c FROM orders WHERE customer_id=1;
IF c <> 1 THEN RAISE EXCEPTION 'expected 1 order for customer_id=1, got %', c; END IF;

SELECT count(*) INTO c
FROM order_line ol JOIN orders o ON o.id=ol.order_id
WHERE o.customer_id=1;
IF c <> 2 THEN RAISE EXCEPTION 'expected 2 order_lines for customer_id=1, got %', c; END IF;

SELECT count(*) INTO c FROM forfait WHERE customer_id=1;
IF c <> 1 THEN RAISE EXCEPTION 'expected 1 forfait for customer_id=1, got %', c; END IF;
END $$;

ROLLBACK;
