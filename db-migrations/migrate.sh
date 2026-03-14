#!/bin/bash
# Run Liquibase migrations against local PostgreSQL
# Usage:
#   ./migrate.sh                          # local postgres (localhost:5432)
#   ./migrate.sh status                   # show migration status
#   ./migrate.sh rollback                 # rollback last changeset
#   DB_URL=jdbc:postgresql://host:5432/db ./migrate.sh   # custom DB

set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$SCRIPT_DIR"

ACTION="${1:-update}"

DB_URL="${DB_URL:-jdbc:postgresql://localhost:5432/ecommerce_db}"
DB_USERNAME="${DB_USERNAME:-postgres}"
DB_PASSWORD="${DB_PASSWORD:-password}"

echo "=== Liquibase $ACTION ==="
echo "  DB: $DB_URL"

mvn liquibase:$ACTION \
  -Dliquibase.url="$DB_URL" \
  -Dliquibase.username="$DB_USERNAME" \
  -Dliquibase.password="$DB_PASSWORD" \
  -q

echo "=== Done ==="
