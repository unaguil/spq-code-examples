DataNucleus + MySQL
===================

This example relies on the DataNucleus Maven plugin. Check the database configuration in the *datanucleus.properties* file and the JDBC driver dependency specified in the *pom.xml* file.

Run the following command to build everything and enhance the DB classes:

      mvn clean compile

Make sure that the database was correctly configured. Use the contents of the file *create-productsdb.sql* to create the database and grant privileges. For example,

      mysql –uroot -p < sql/create-productsdb.sql

Run the following command to create database schema for this sample.

      mvn datanucleus:schema-create

Run the following command to launch the main example class 
   
      mvn exec:java

Run the following command to remove the database schema
   
      mvn datanucleus:schema-delete

Run the following command to generate the Javadoc documentation inside the *target/site/apidocs* directory

      mvn javadoc:javadoc
