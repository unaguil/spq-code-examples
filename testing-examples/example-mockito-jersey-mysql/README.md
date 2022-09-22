Mockito + Jersey + DataNucleus + MySQL
======================================

This example shows how to use Mockito to perform tests of the different classes and components of the project.

Run the following command to compile all classes and launch the unit tests:

      mvn test

Make sure that the database was correctly configured. Use the contents of the file *create-message.sql* to create the database and grant privileges. For example,

      mysql –uroot -p < sql/create-messages.sql

In this example, the class enhancement required by DataNucleus must be manually executed after the unit testing is performed.
This is required to avoid cluttering the JaCoCo report with all the methods generated automatically by DataNucleus.

Therefore, execute the following command to enhance the database classes

      mvn datanucleus:enhance

Run the following command to create database schema for this sample.

      mvn datanucleus:schema-create

To launch the server run the command

    mvn jetty:run

Now, the client sample code can be executed in a new command window with

    mvn exec:java -Pclient
