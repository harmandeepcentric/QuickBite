#!/bin/bash

# Build script for QuickBite Docker image

set -e

echo "🚀 Building QuickBite Docker image..."

# Build the image
docker build -t quickbite-api:latest .

echo "✅ Docker image built successfully!"
echo "📊 Image size:"
docker images quickbite-api:latest --format "table {{.Repository}}\t{{.Tag}}\t{{.Size}}"

echo ""
echo "🔧 To run the container:"
echo "docker run -p 8080:8080 -v quickbite-data:/app/data quickbite-api:latest"
echo ""
echo "🐳 Or use docker-compose:"
echo "docker-compose up -d"