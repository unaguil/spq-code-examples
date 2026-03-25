SpringBoot + REST + MySQL (Multimodule)
=====================================

This multimodule example uses Spring Boot to provide a REST API backed by MySQL.
The project is organized into three submodules:

- `client` — sample client code and launch scripts
- `commons` — shared domain/model code used by other modules
- `server` — Spring Boot REST service

Database
--------

Create the database and grant privileges using the provided SQL script:

    mysql -uroot -p < sql/create-messages.sql

Build
-----

Build all modules with the Gradle wrapper from the project root:

    ./gradlew build

Run
---

Run the server module with:

    ./gradlew :server:bootRun

Run the client sample (if a `run` task is configured) with:

    ./gradlew :client:run

Alternatively, the `client` module contains generated scripts in `client/build/scripts/` that can be used to run the sample client.

Notes
-----

- Check `server/src/main/resources/application.properties` for the database connection settings.
- The SQL file is at `sql/create-messages.sql`.
- This project uses a multimodule Gradle layout: each module has its own `build.gradle` and outputs under its `build/` folder.

Code coverage (Jacoco)
----------------------

Generate a code-coverage report for the whole multimodule project using the Gradle Jacoco plugin. From the project root run:

```
./gradlew jacocoTestReport
``+

After the build completes the HTML report is available at `build/reports/jacoco/test/html/index.html` in the root project. To produce per-module reports, run the task for each module:

```
./gradlew :commons:jacocoTestReport
./gradlew :client:jacocoTestReport
```

If you want, I can commit this README or update it with more module-specific instructions (examples, ports, expected endpoints).
