 TOKEN=$(curl -s -X POST http://localhost:8180/realms/homebase/protocol/openid-connect/token \
    -d "client_id=homebase-system" \
    -d "client_secret=system-secret-dev" \
    -d "grant_type=client_credentials" | python3 -c "import sys,json; print(json.load(sys.stdin)['access_token'])")

  curl -s -X POST http://localhost:8000/cart/carts \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -H "x-chenile-tenant-id: homebase" \
    -d '{"pageSize":10,"pageNum":1,"filters":{}}' | python3 -m json.tool