#!/bin/bash

BASE_URL="http://localhost:8000"

echo "🚀 Creating User..."
CREATE_RESPONSE=$(curl -s -X POST $BASE_URL/user \
  -H "Content-Type: application/json" \
  -d '{
        "name": "Premkumar Cherupally",
        "branch": "Computer Science",
        "percentage": "90",
        "phone": "1234567890",
        "email": "prem@example.com"
      }')

echo "Create Response:"
echo "$CREATE_RESPONSE"

# Extract ID from response
USER_ID=$(echo $CREATE_RESPONSE | grep -o '"id":"[^"]*"' | cut -d'"' -f4)

echo ""
echo "🆔 Extracted User ID: $USER_ID"

echo ""
echo "🔎 Querying User..."
curl -s -X POST $BASE_URL/q/user \
  -H "Content-Type: application/json" \
  -d "{
        \"filters\": {
            \"userId\": \"$USER_ID\"
        }
      }"

echo ""
echo ""
echo "📥 Retrieving User by ID..."
curl -s -X GET $BASE_URL/user/$USER_ID \
  -H "Accept: application/json"

echo ""
echo ""
echo "✅ Done!"