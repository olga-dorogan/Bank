USE `bank_db`;
CREATE TABLE client(id INT PRIMARY KEY AUTO_INCREMENT, firstName VARCHAR(45), lastName VARCHAR(45) );
CREATE TABLE account(id INT PRIMARY KEY AUTO_INCREMENT, client_id INT, title VARCHAR(45), amount bigint CHECK(amount>0),
FOREIGN KEY (client_id) REFERENCES client(id));
CREATE TABLE transaction(id INT PRIMARY KEY AUTO_INCREMENT, id_account_from INT, id_account_to INT, date TIMESTAMP, amount BIGINT,
       FOREIGN KEY (id_account_from) REFERENCES account(id), FOREIGN KEY (id_account_to) REFERENCES account(id));

INSERT INTO client(id, firstName, lastName) VALUES(1, 'Ivan', 'Ivanov');
INSERT INTO client(id, firstName, lastName) VALUES(2, 'Petr', 'Petrov');
INSERT INTO account(id, client_id, title, amount) VALUES(1, 1, 'student card', 1000);
INSERT INTO account(id, client_id, title, amount) VALUES(2, 1, 'credit card', 0);
INSERT INTO account(id, client_id, title, amount) VALUES(3, 2, 'student card', 0);
INSERT INTO account(id, client_id, title, amount) VALUES(4, 2, 'credit card', 0);