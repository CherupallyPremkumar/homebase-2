# Stage 1: Build the app
FROM eclipse-temurin:25-jdk AS builder

RUN apt-get update && apt-get install -y maven
WORKDIR /app

COPY . .

# If using Maven
RUN mvn clean package -DskipTests

# If using Gradle
# RUN ./gradlew build -x test

# Stage 2: Package the app
FROM eclipse-temurin:25-jdk
WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/rules-engine-app-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]