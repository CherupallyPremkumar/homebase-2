# Build Stage
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the jar
COPY web/target/ecommerce-web-1.0.0.jar app.jar

# Copy entrypoint script
COPY entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh

# Create certs directory and safely copy files if they exist in context (local build)
# In CI, this folder is git-ignored and will be skipped without error
RUN mkdir -p /app/certs
COPY web/src/main/resources/ /tmp/resources/
RUN if [ -d /tmp/resources/certs ]; then cp -r /tmp/resources/certs/* /app/certs/; fi && \
    rm -rf /tmp/resources

EXPOSE 8080
ENTRYPOINT ["/app/entrypoint.sh"]
