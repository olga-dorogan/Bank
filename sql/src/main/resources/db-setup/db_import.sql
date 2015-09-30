USE `bank_db`;

INSERT INTO client(id, firstName, lastName, passport, version) VALUES(1, 'Ivan', 'Ivanov', 'ЕР123456', 1);
INSERT INTO client(id, firstName, lastName, passport, version) VALUES(2, 'Petr', 'Petrov', 'ЕР121212', 1);
INSERT INTO account(id, client_id, title, amount, version) VALUES(1, 1, 'student card', 1000, 1);
INSERT INTO account(id, client_id, title, amount, version) VALUES(2, 1, 'credit card', 0, 1);
INSERT INTO account(id, client_id, title, amount, version) VALUES(3, 2, 'student card', 0, 1);
INSERT INTO account(id, client_id, title, amount, version) VALUES(4, 2, 'credit card', 0, 1);