# Stage 1: Build stage
FROM maven:3.8.4-openjdk-17 AS builder

WORKDIR /app
COPY pom.xml .
COPY src/ ./src

# You can skip the 'RUN mvn clean package' here, as it will be done when you run the container

# Stage 2: Runtime stage
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the pom.xml and source code (we're relying on volume mounts now)
COPY pom.xml /app
COPY src/ /app/src

# Run Maven build when the container starts
CMD mvn clean package -DskipTests && java -jar target/udise_portal-0.0.1-SNAPSHOT.jar
