#!/bin/bash

# Run both user-query and user-service (via build-package) locally
# Note: PostgreSQL must be running locally on port 5432

echo "=== Starting user-query service on port 8000 ==="
cd /Users/premkumar/Documents/HomeBase
# Ensure JAVA_HOME is set to JDK 25 as identified in environment
export JAVA_HOME=/Users/premkumar/Library/Java/JavaVirtualMachines/openjdk-25/Contents/Home
mvn spring-boot:run -pl user/user-query &

QUERY_PID=$!

sleep 15

echo "=== Starting build-package (includes user-service) on port 8080 ==="
mvn spring-boot:run -pl build/build-package &

SERVICE_PID=$!

echo "user-query PID: $QUERY_PID"
echo "build-package PID: $SERVICE_PID"
echo ""
echo "Services starting..."
echo "- user-query: http://localhost:8000"
echo "- build-package: http://localhost:8080"
echo ""
echo "Press Ctrl+C to stop"

# Wait for both processes
wait
