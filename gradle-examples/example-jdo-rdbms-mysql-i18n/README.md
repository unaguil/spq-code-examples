DataNucleus + MySQL + I18N
==========================

This example relies on the DataNucleus Gradle plugin. Check the database configuration in the *datanucleus.properties* file and the JDBC driver dependency specified in the *build.gradle* file.

The example contains I18N support for string localization in Main.java class.

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

Finally, to generate the distributable application with its dependencies run the command

      ./gradlew build

that will build the application jar and copy all the required libraries to the *build/jars*.
Finally, run the following command to execute the application from the command line

(Windows classpath separator ;)

      java -cp "build/jars/*;build/libs/example-jdo-rdbms-mysql-i18n-3.0.jar" org.datanucleus.samples.jdo.tutorial.Main

(Linux classpath separator :)

      java -cp "build/jars/*:build/libs/example-jdo-rdbms-mysql-i18n-3.0.jar" org.datanucleus.samples.jdo.tutorial.Main
