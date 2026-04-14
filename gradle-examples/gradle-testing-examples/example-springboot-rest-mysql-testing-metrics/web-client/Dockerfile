# Stage 1: Build
FROM gradle:9.4.0-jdk21 AS builder
WORKDIR /workspace
COPY . .
RUN gradle :web-client:bootJar --no-daemon -x test

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /workspace/web-client/build/libs/web-client-*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
