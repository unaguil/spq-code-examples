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

    mysql -uroot -p < server/sql/create-messages.sql

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
- The SQL file is at `server/sql/create-messages.sql`.
- This project uses a multimodule Gradle layout: each module has its own `build.gradle` and outputs under its `build/` folder.

Testing
-------

The project provides three isolated test lanes, equivalent to the Maven Surefire/Failsafe profile separation in the Jersey baseline:

| Lane | Command | What runs | Tooling |
|------|---------|-----------|---------|
| Unit | `./gradlew test` | Mocked controller tests, entity/DTO tests, client tests | JUnit 5 + Mockito + MockMvc |
| Integration | `./gradlew :server:integrationTest` | Full API ↔ external MySQL | JUnit 5 + Spring Boot Test |
| Performance | `./gradlew :server:performanceTest` | Multi-threaded load against external MySQL | JUnit 5 + JUnitPerf |

### Unit tests only (fast, no DB required)

```
./gradlew test
```

Runs all tests **without** the `integration` or `performance` JUnit 5 tags. Covers:
- `UserControllerTest` — POST /users/add, POST /users/say, GET /users/all (MockMvc + MockitoBean)
- `MessageControllerTest` — GET /messages/all (MockMvc + MockitoBean)
- `UserTest`, `MessageTest`, `UserDataTest` — entity/DTO invariants (commons module)
- `ExampleClientTest` — HTTP client behaviour (client module)

Code coverage (Jacoco)
----------------------

Generate a code-coverage report for the unit test run from the project root:

```
./gradlew jacocoTestReport
```

HTML report: `<module>/build/reports/jacoco/index.html`

Per-module reports:

```
./gradlew :commons:jacocoTestReport
./gradlew :client:jacocoTestReport
./gradlew :server:jacocoTestReport
```

### Integration tests (external MySQL)

```
./gradlew :server:integrationTest
```

Runs the full Spring application context on a random port and validates:
- User registration and password update
- Message posting with valid/invalid credentials
- Retrieval of all users and per-user messages

These tests use the external DB settings from `server/src/main/resources/application.properties`:

- `DB_HOST` (default: `localhost`)
- `DB_PORT` (default: `3306`)
- `DB_USER` (default: `spq`)
- `DB_PASS` (default: `spq`)

Example:

```bash
DB_HOST=127.0.0.1 DB_PORT=3306 DB_USER=spq DB_PASS=spq ./gradlew :server:integrationTest
```

Source: `server/src/test/java/` (tagged `@Tag("integration")`)

### Performance tests (JUnitPerf + external MySQL)

```
./gradlew :server:performanceTest
```

Runs `ServerPerformanceTest` against the same external MySQL connection. Each test method is exercised with **10 threads for 2 seconds** (500 ms warm-up) via `@JUnitPerfTest`. Covers:
- `registerUser_underLoad`
- `sayMessage_underLoad`
- `getAllUsers_underLoad`
- `getMessagesByUser_underLoad`

Source: `server/src/test/java/` (tagged `@Tag("performance")`)

### Run all lanes sequentially

```
./gradlew test :server:integrationTest :server:performanceTest
```

