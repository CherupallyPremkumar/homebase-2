#!/bin/bash
# Creates all HomeBase Kafka topics from homebase-topics.yaml
# Usage: ./create-topics.sh [bootstrap-server]
#
# Local:  ./create-topics.sh localhost:9092
# K8s:    kubectl exec kafka-0 -n homebase-infra -- /bin/bash < create-topics.sh

BOOTSTRAP=${1:-localhost:9092}

echo "Creating HomeBase Kafka topics on $BOOTSTRAP..."

# Parse topics from YAML and create
grep "name:" ../topics/homebase-topics.yaml | sed 's/.*name: //' | while read topic; do
  partitions=$(grep -A2 "name: $topic" ../topics/homebase-topics.yaml | grep "partitions:" | sed 's/.*partitions: //')
  partitions=${partitions:-3}

  echo "Creating topic: $topic (partitions: $partitions)"
  kafka-topics --create \
    --bootstrap-server "$BOOTSTRAP" \
    --topic "$topic" \
    --partitions "$partitions" \
    --replication-factor 1 \
    --if-not-exists \
    2>/dev/null
done

echo ""
echo "=== All topics ==="
kafka-topics --list --bootstrap-server "$BOOTSTRAP"
