SpringBoot + REST + MySQL
============================

This example relies on Spring Boot to create a REST API with a MySQL database. Check the database configuration in the *application.properties* file and the JDBC driver dependency specified in the *pom.xml* file. In addition, the project contains the server and client example codes.

Make sure that the database was correctly configured before running the application. Use the contents of the file *create-message.sql* to create the database and grant privileges to the database user,

      mysql –uroot -p < sql/create-messages.sql

To launch the server run the command

    mvn spring-boot:run

Now, the client sample code can be executed in a new command window with

    mvn exec:java -Pclient

