# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /backend

# Copy the pom.xml and source code
COPY pom.xml .
COPY backend ./backend

# Package the application (skipping tests to speed up deployment)
RUN mvn clean package -DskipTests

# Stage 2: Create the lightweight runtime image
FROM eclipse-temurin:17-jre-alpine
WORKDIR /backend

# Copy the built JAR from the build stage
COPY --from=build /backend/target/*.jar app.jar

# Expose the port defined in your application.yml
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "cafe-ai-recipes.jar"]