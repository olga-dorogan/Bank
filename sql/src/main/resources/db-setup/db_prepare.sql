-- delete the bank_db database if existent
DROP DATABASE IF EXISTS bank_db;

-- create the bank_db database
CREATE DATABASE bank_db;

-- drop 'bank_clerk' user
GRANT USAGE ON *.* TO 'bank_clerk'@'localhost';
DROP USER 'bank_clerk'@'localhost';

-- create the development user 'bank_clerk' identified by the password 'bank_clerk'
CREATE USER 'bank_clerk'@'localhost' IDENTIFIED BY 'clerkpswrd';

-- verify user creation
select host, user, password from mysql.user;

-- verify user privileges
SELECT host, user, select_priv, insert_priv, update_priv, delete_priv, create_priv, alter_priv, password FROM mysql.user WHERE user='bank_clerk';
-- should have no privilegesuser

-- grant full privileges to the user, for easy development
GRANT ALL PRIVILEGES ON *.* TO 'bank_clerk'@'localhost' IDENTIFIED BY 'clerkpswrd' WITH GRANT OPTION;

-- verify privileges set
SELECT host, user, select_priv, insert_priv, update_priv, delete_priv, create_priv, alter_priv, password FROM mysql.user WHERE user='bank_clerk';
