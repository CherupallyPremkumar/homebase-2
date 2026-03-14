# Build Stage
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the jar from build-package module
COPY build/build-package/target/*.jar app.jar

# Copy entrypoint script if it exists
COPY entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh

EXPOSE 8080
ENTRYPOINT ["/app/entrypoint.sh"]
