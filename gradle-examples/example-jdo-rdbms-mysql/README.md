DataNucleus + MySQL
===================

This example relies on the DataNucleus Gradle plugin. Check the database configuration in the *datanucleus.properties* file and the JDBC driver dependency specified in the *build.gradle* file.

Run the following command to build everything and enhance the DB classes:

      ./gradlew clean compileJava

Make sure that the database was correctly configured. Use the contents of the file *create-productsdb.sql* to create the database and grant privileges. For example,

      mysql -uroot -p < sql/create-productsdb.sql

Run the following command to create database schema for this sample.

      ./gradlew dnSchemaCreate

Run the following command to launch the main example class

      ./gradlew run

Run the following command to remove the database schema

      ./gradlew dnSchemaDelete

Run the following command to generate the Javadoc documentation inside the *build/docs/javadoc* directory

      ./gradlew javadoc
