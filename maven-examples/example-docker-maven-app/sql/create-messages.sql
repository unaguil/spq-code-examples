DROP SCHEMA IF EXISTS messagesDB;
DROP USER IF EXISTS 'spq'@'localhost';

CREATE SCHEMA messagesDB;
CREATE USER IF NOT EXISTS 'spq'@'localhost' IDENTIFIED BY 'spq';
GRANT ALL ON messagesDB.* TO 'spq'@'localhost';
CREATE USER IF NOT EXISTS 'spq'@'%' IDENTIFIED BY 'spq';
GRANT ALL ON messagesDB.* TO 'spq'@'%';