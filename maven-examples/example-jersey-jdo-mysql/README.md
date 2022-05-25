Jersey + Datanucleus + MySQL
============================

This example relies on the DataNucleus Maven plugin. Check the database configuration in the *datanucleus.properties* file and the JDBC driver dependency specified in the *pom.xml* file.

In addition, the project contains the server and client example codes.
First, the server and client can be compiled using

Run the following command to build everything and enhancing the DB classes:

   mvn compile

Make sure that the database was correctly configured. Use the contents of the file *create-message.sql* to create the database and grant privileges. For example,

   mysql –uroot < create-messages.sql

Run the following command to create database schema for this sample.

   mvn datanucleus:schema-create

To launch the server run the command

    mvn jetty:run

Now, the client sample code can be executed in a new command window with

    mvn exec:java -Pclient

