CREATE DATABASE jersey;
CREATE USER IF NOT EXISTS 'spq'@'localhost' IDENTIFIED BY 'spq';
GRANT ALL ON jersey.* TO 'spq'@'localhost';
CREATE USER IF NOT EXISTS 'spq'@'%' IDENTIFIED BY 'spq';
GRANT ALL ON jersey.* TO 'spq'@'%';