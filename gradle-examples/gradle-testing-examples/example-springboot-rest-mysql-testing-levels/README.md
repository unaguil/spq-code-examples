SpringBoot + REST + MySQL (Multimodule)
=====================================

This multimodule example uses Spring Boot to provide a REST API backed by MySQL.
The project is organized into four submodules:

- `client` — sample Java command-line client using RestTemplate
- `commons` — shared domain/model code used by other modules
- `server` — Spring Boot REST service
- `web-client` — browser-based Thymeleaf UI that exercises all server endpoints

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

Run the web client on port 8081 (server must already be running) with:

    ./gradlew :web-client:bootRun

Then open `http://localhost:8081` in a browser. The home page links to all four features:
register a user, view all users, post a message, and view messages by user.

The server URL used by the web client can be overridden at runtime:

    ./gradlew :web-client:bootRun --args='--server.api.base-url=http://other-host:8080'

Notes
-----

- Check `server/src/main/resources/application.properties` for the database connection settings.
- The SQL file is at `server/sql/create-messages.sql`.
- This project uses a multimodule Gradle layout: each module has its own `build.gradle` and outputs under its `build/` folder.

Testing
-------

The project provides four isolated test lanes:

| Lane | Command | What runs | Tooling |
|------|---------|-----------|---------|
| Unit | `./gradlew test` | Mocked controller tests, entity/DTO tests, client tests | JUnit 5 + Mockito + MockMvc |
| Integration | `./gradlew :server:integrationTest` | Full API ↔ external MySQL | JUnit 5 + Spring Boot Test |
| Performance | `./gradlew :server:performanceTest` | Multi-threaded load against external MySQL | JUnit 5 + JUnitPerf |
| E2E | `./gradlew :web-client:e2eTest` | Browser-driven full-stack journey | JUnit 5 + Playwright |

### Unit tests only (fast, no DB required)

```
./gradlew test
```

Runs all tests **without** the `integration` or `performance` JUnit 5 tags. Covers:
- `UserControllerTest` — POST /users/add, POST /users/say, GET /users/all (MockMvc + MockitoBean)
- `MessageControllerTest` — GET /messages/all (MockMvc + MockitoBean)
- `UserTest`, `MessageTest`, `UserDataTest` — entity/DTO invariants (commons module)
- `ExampleClientTest` — HTTP client behaviour (client module)
- `UserWebControllerTest` — GET/POST /users/register, GET /users/list (`@WebMvcTest` + `@MockitoBean ServerApiService`)
- `MessageWebControllerTest` — GET/POST /messages/post, GET/POST /messages/list (`@WebMvcTest` + `@MockitoBean ServerApiService`)
- `ServerApiServiceTest` — all four RestTemplate call patterns (plain Mockito, no Spring context)

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
./gradlew :web-client:jacocoTestReport
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

### E2E tests (Playwright + full stack)

```
./gradlew :web-client:e2eTest
```

Runs `WebClientE2ETest` using a headless Chromium browser against the live web-client at `http://localhost:8081`. Requires the full stack to be running beforehand:

```bash
./gradlew :server:bootRun &
./gradlew :web-client:bootRun &
./gradlew :web-client:e2eTest
```

Covers the complete user journey through the browser UI:
- Home page navigation links
- User registration → "Saved" confirmation
- List all users
- Post a message → echoed response
- View messages by user

Playwright downloads Chromium automatically on first run (`~/.cache/ms-playwright`).


### Run all lanes sequentially

```
./gradlew test :server:integrationTest :server:performanceTest :web-client:e2eTest
```

