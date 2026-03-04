DataNucleus + MongoDB
=====================

This example relies on the DataNucleus Gradle plugin. Check the database configuration in the *datanucleus.properties* file and the dependency specified in the *build.gradle* file.

Run the following command to build everything and enhance the DB classes:

      ./gradlew clean compileJava

Make sure that the MongoDB instance is up and running before continuing.

Run the following command to launch the main example class

      ./gradlew run

Run the following command to generate the Javadoc documentation inside the *build/docs/javadoc* directory

      ./gradlew javadoc
