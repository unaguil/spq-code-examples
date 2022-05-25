Datanucleus + MySQL + I18N
==========================

This example relies on the DataNucleus Maven plugin. Check the database configuration in the *datanucleus.properties* file and the JDBC driver dependency specified in the *pom.xml* file.

Run the following command to build everything and enhancing the DB classes:

   mvn clean compile

Make sure that the database was correctly configured. Use the contents of the file *create-productsdb.sql* to create the database and grant privileges. For example,

   mysql –uroot < create-productsdb.sql

Run the following command to create database schema for this sample.

   mvn datanucleus:schema-create

Run the following command to launche the main example class 
   
   mvn exec:java

Run the following command to remove the database schema
   
   mvn datanucleus:schema-delete

Run the following command to generate the Javadoc documentation inside the *target/site/apidocs* directory

   mvn javadoc:javadoc

Finally, to generate the distributable application with its dependencies run the command

   mvn package

that will build the application jar and copy all the required libraries to the *target/jar*.
Finally, run the following command to execute the application from the command line

(Windows classpath separator ;)

   java -cp "target/jars/*";target/Datanucleus-Samples-JDO-MySQL-I18N-3.0.jar org.datanucleus.samples.jdo.tutorial.Main

(Linux classpath separator :)

   java -cp "target/jars/*":target/Datanucleus-Samples-JDO-MySQL-I18N-3.0.jar org.datanucleus.samples.jdo.tutorial.Main
