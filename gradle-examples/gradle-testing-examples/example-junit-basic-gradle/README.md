Gradle JUnit Basic
===================

This project contains a basic Gradle skeleton for the JUnit example used in
the testing samples.

Compiling the project

	./gradlew compileJava

Launching the tests

	./gradlew test

Create the distributable JAR

	./gradlew build

The packaged JAR will be written to the *build/libs* folder. You can run it by
adding the generated JAR to the classpath and specifying a main class, if any.

Provide build properties on the command line, for example:

	./gradlew build -Pcommand.line.prop="hello"

Generate JaCoCo coverage report

    ./gradlew jacocoTestReport

The HTML report will be written to `build/reports/jacoco/test/html/index.html`.

Generate Javadoc

	./gradlew javadoc

Clean build artifacts

	./gradlew clean
