# Stage 1: Build
FROM gradle:9.4.0-jdk21 AS builder
WORKDIR /workspace
COPY . .
RUN gradle :server:bootJar --no-daemon -x test

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /workspace/server/build/libs/server-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
