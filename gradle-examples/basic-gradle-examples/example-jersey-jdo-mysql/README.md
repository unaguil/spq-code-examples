Jersey + DataNucleus + MySQL
============================

This example relies on the DataNucleus Gradle plugin. Check the database configuration in the *datanucleus.properties* file and the JDBC driver dependency specified in the *build.gradle* file. In addition, the project contains the server and client example codes.

Run the following command to build everything and enhance the DB classes:

      ./gradlew clean compileJava

Make sure that the database was correctly configured. Use the contents of the file *create-messages.sql* to create the database and grant privileges. For example,

      mysql -uroot -p < sql/create-messages.sql

Run the following command to create database schema for this sample.

      ./gradlew dnSchemaCreate

To launch the server run the command

    ./gradlew appRun

Now, the client sample code can be executed in a new command window with

    ./gradlew runClient
