# Stage 1: Build the application
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app

# Copy the pom.xml and src separately
COPY pom.xml /app
COPY src /app/src

# Run Maven build
RUN mvn clean package -DskipTests

# Stage 2: Create the final image
FROM openjdk:17-jdk-slim
COPY --from=build /app/target/udise_portal-0.0.1-SNAPSHOT.jar /app.jar

# Set the entrypoint for the application
ENTRYPOINT ["java", "-jar", "/app.jar"]

