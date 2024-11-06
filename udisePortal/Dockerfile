# Stage 1: Build stage
FROM maven:3.8.4-openjdk-17 AS builder

WORKDIR /app
COPY pom.xml .
COPY src/ ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime stage
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy only the built JAR from the builder stage
COPY --from=builder /app/target/udise_portal-0.0.1-SNAPSHOT.jar /app/app.jar

# Set the entrypoint
ENTRYPOINT ["java", "-jar", "/app/app.jar"]




