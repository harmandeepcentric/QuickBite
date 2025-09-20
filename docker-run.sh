#!/bin/bash

# Run script for QuickBite Docker container

set -e

echo "🚀 Starting QuickBite container..."

# Stop and remove existing container if it exists
docker stop quickbite-api 2>/dev/null || true
docker rm quickbite-api 2>/dev/null || true

# Run the container
docker run -d \
  --name quickbite-api \
  -p 8080:8080 \
  -v quickbite-data:/app/data \
  --restart unless-stopped \
  quickbite-api:latest

echo "✅ QuickBite container started successfully!"
echo ""
echo "🌐 Application URL: http://localhost:8080"
echo "📖 Swagger UI: http://localhost:8080/swagger-ui.html"
echo "🏥 Health Check: http://localhost:8080/actuator/health"
echo ""
echo "📋 Container logs:"
echo "docker logs -f quickbite-api"