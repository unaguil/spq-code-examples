DataNucleus + MongoDB
=====================

This example relies on the DataNucleus Maven plugin. Check the database configuration in the *datanucleus.properties* file and the JDBC driver dependency specified in the *pom.xml* file.

Run the following command to build everything and enhancing the DB classes:

      mvn clean compile

Make sure that the MongoDB instance is up and running before continuing.

Run the following command to create database schema for this sample.

      mvn datanucleus:schema-create

Run the following command to launche the main example class

      mvn exec:java

Run the following command to remove the database schema

      mvn datanucleus:schema-delete

Run the following command to generate the Javadoc documentation inside the *target/site/apidocs* directory

      mvn javadoc:javadoc
