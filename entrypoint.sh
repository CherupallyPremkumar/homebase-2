#!/bin/bash
set -e

# HomeBase Application Entrypoint
# Configures JVM options and starts the Spring Boot application

# Default JVM options for containers
JVM_OPTS="${JVM_OPTS:--XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:InitialRAMPercentage=50.0}"

# Spring profiles
PROFILES="${SPRING_PROFILES_ACTIVE:-prod}"

echo "Starting HomeBase with profiles: ${PROFILES}"
echo "JVM options: ${JVM_OPTS}"

exec java ${JVM_OPTS} \
  -Djava.security.egd=file:/dev/./urandom \
  -Dspring.profiles.active="${PROFILES}" \
  -jar /app/app.jar "$@"
