Gradle JUnitPerf Basic
======================

This project contains a basic Gradle skeleton for the JUnitPerf example used
in the testing samples.

Compiling the project

    ./gradlew compileJava

Launching the tests

    ./gradlew test

Create the distributable JAR

    ./gradlew build

The packaged JAR will be written to the *build/libs* folder.

Generate Javadoc

    ./gradlew javadoc

Clean build artifacts

    ./gradlew clean

JUnitPerf HTML report

The JUnitPerf HTML report is written to `build/reports/junitperf/report.html` after running the performance tests.

Running performance tests

The project provides a `perfTest` Gradle task that runs tests matching `*PerfTest`. It is executed automatically as part of `./gradlew check`.

To run performance tests only:

    ./gradlew perfTest


