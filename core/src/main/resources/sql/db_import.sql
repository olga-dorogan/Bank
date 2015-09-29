CREATE TABLE IF NOT EXISTS client(id INT PRIMARY KEY AUTO_INCREMENT, firstName VARCHAR(45), lastName VARCHAR(45) );
CREATE TABLE IF NOT EXISTS account(id INT PRIMARY KEY AUTO_INCREMENT, client_id INT, title VARCHAR(45), amount bigint CHECK(amount>0),
FOREIGN KEY (client_id) REFERENCES client(id));
CREATE TABLE IF NOT EXISTS transaction(id INT PRIMARY KEY AUTO_INCREMENT, id_account_from INT, id_account_to INT, date TIMESTAMP, amount BIGINT,
       FOREIGN KEY (id_account_from) REFERENCES account(id), FOREIGN KEY (id_account_to) REFERENCES account(id));