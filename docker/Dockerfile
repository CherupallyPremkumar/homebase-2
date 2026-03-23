# Build Stage
FROM eclipse-temurin:25-jre
WORKDIR /app

# Copy the jar from build-package module
COPY root-build/build-package/target/*.jar app.jar

# Copy entrypoint script if it exists
COPY entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh

EXPOSE 8080
ENTRYPOINT ["/app/entrypoint.sh"]
