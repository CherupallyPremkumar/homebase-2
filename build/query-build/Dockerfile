# Dockerfile for Java 25
FROM eclipse-temurin:25-jre
WORKDIR /app
COPY query-build.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-Xmx512m", "-jar", "app.jar"]