JUnitPerf + Mockito + Jersey + DataNucleus + MySQL
==================================================

This example shows how to use JUnitPerf to test the performance of the REST API.

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

Performance tests can be launched using the following command (the server will be automatically launched for the tests an a
data fixture will be added to perform the tests)

      mvn verify -Pintegration-tests

To launch the server run the command

    mvn jetty:run

Now, the client sample code can be executed in a new command window with

    mvn exec:java -Pclient
