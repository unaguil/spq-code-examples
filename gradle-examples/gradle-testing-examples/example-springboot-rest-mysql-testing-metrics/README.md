SpringBoot + REST + MySQL (Multimodule)
=====================================

This multimodule example uses Spring Boot to provide a REST API backed by MySQL.
The project is organized into four submodules:

- `client` — sample Java command-line client using RestTemplate
- `commons` — shared domain/model code used by other modules
- `server` — Spring Boot REST service with Micrometer/Prometheus metrics
- `web-client` — browser-based Thymeleaf UI that exercises all server endpoints

Docker Compose (recommended)
----------------------------

The easiest way to run the full stack — server, web-client, MySQL, Prometheus, Grafana, and Nginx — is with Docker Compose:

```bash
docker compose build
docker compose up
```

The following ports are exposed to the host:

| URL | Service |
|-----|---------|
| `http://localhost:8080` | REST API (server) |
| `http://localhost:8080/actuator/prometheus` | Prometheus metrics endpoint |
| `http://localhost:8081` | Web UI (Thymeleaf) |
| `http://localhost/grafana/` | Grafana dashboard (admin / admin) |

MySQL and Prometheus are internal to the Docker network and not reachable from the host. Grafana is accessible via the Nginx reverse proxy on port 80.

The Grafana **SPQ Metrics** dashboard is provisioned automatically and shows:
- Total user registrations
- Total messages posted
- Total authentication failures
- Request rate over time (per second)

Database (manual setup)
-----------------------

If running without Docker Compose, create the database and grant privileges:

    mysql -uroot -p < server/sql/create-messages.sql

Build
-----

Build all modules with the Gradle wrapper from the project root:

    ./gradlew build

Run (manual)
------------

Run the server module:

    ./gradlew :server:bootRun

Run the web client on port 8081 (server must already be running):

    ./gradlew :web-client:bootRun

Then open `http://localhost:8081` in a browser. The home page links to all four features:
register a user, view all users, post a message, and view messages by user.

The server URL used by the web client can be overridden at runtime:

    ./gradlew :web-client:bootRun --args='--server.api.base-url=http://other-host:8080'

Run the CLI client sample:

    ./gradlew :client:run

Notes
-----

- Check `server/src/main/resources/application.properties` for database and actuator settings.
- The SQL schema is at `server/sql/create-messages.sql`.
- This project uses a multimodule Gradle layout: each module has its own `build.gradle` and outputs under its `build/` folder.

Metrics
-------

The server exposes Spring Boot Actuator endpoints including a Prometheus-compatible scrape endpoint:

    GET /actuator/prometheus
    GET /actuator/health
    GET /actuator/info

Three custom business counters are tracked:

| Metric | Prometheus name | Description |
|--------|----------------|-------------|
| User registrations | `user_registrations_total` | Every call to `POST /users/add` |
| Messages posted | `message_posted_total` | Successful `POST /users/say` |
| Auth failures | `auth_failures_total` | Failed credential check in `POST /users/say` |

When running via Docker Compose, Prometheus scrapes these automatically every 15 seconds and Grafana visualises them.

Testing
-------

The project provides four isolated test lanes:

| Lane | Command | What runs | Tooling |
|------|---------|-----------|---------|
| Unit | `./gradlew test` | Mocked controller tests, entity/DTO tests, client tests | JUnit 5 + Mockito + MockMvc |
| Integration | `./gradlew :server:integrationTest` | Full API + containerised MySQL | JUnit 5 + Spring Boot Test + Testcontainers |
| Performance | `./gradlew :server:performanceTest` | Multi-threaded load against containerised MySQL | JUnit 5 + JUnitPerf + Testcontainers |
| E2E | `./gradlew :web-client:e2eTest` | Browser-driven full-stack journey | JUnit 5 + Playwright |

### Unit tests (fast, no DB required)

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

### Code coverage (JaCoCo)

```
./gradlew jacocoTestReport
```

HTML report: `<module>/build/reports/jacoco/index.html`

Per-module:

```
./gradlew :commons:jacocoTestReport
./gradlew :client:jacocoTestReport
./gradlew :server:jacocoTestReport
./gradlew :web-client:jacocoTestReport
```

### Integration tests (Testcontainers — no external MySQL needed)

```
./gradlew :server:integrationTest
```

Runs the full Spring application context on a random port. A MySQL 8 container is started automatically via Testcontainers and stopped when the tests finish — no external database required.

Validates:
- User registration and password update
- Message posting with valid/invalid credentials
- Retrieval of all users and per-user messages

Source: `server/src/test/java/` (tagged `@Tag("integration")`)

### Performance tests (JUnitPerf + Testcontainers)

```
./gradlew :server:performanceTest
```

Runs `ServerPerformanceTest` against a MySQL 8 container started automatically by Testcontainers. Each test method is exercised with **10 threads for 2 seconds** (500 ms warm-up) via `@JUnitPerfTest`. Covers:
- `registerUser_underLoad`
- `sayMessage_underLoad`
- `getAllUsers_underLoad`
- `getMessagesByUser_underLoad`

HTML report: `server/build/reports/junitperf/report.html`

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

Or use Docker Compose and run the E2E tests against it:

```bash
docker compose up -d
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
