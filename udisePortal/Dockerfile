# Use a Maven image that includes OpenJDK
FROM maven:3.8.4-openjdk-17

# Set the working directory
WORKDIR /app

# Copy the pom.xml and src separately
COPY pom.xml /app
COPY src /app/src

# Run Maven build
RUN mvn clean package -DskipTests

# Set the entrypoint for the application
ENTRYPOINT ["java", "-jar", "target/udise_portal-0.0.1-SNAPSHOT.jar"]


