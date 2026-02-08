BEGIN;

-- nettoyage minimal (ajuste si contraintes)
DELETE FROM order_line;
DELETE FROM orders;
DELETE FROM forfait;
DELETE FROM customer;

INSERT INTO customer(id, name, credit_card)
VALUES (1, 'Clienta', '1234567890');

COMMIT;
