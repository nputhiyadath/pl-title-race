#!/bin/bash
# Build the backend using Docker with JDK 21

docker run --rm \
  -v "$(pwd)":/app \
  -w /app \
  maven:3.9-eclipse-temurin-21 \
  mvn clean package -DskipTests

echo "Build complete! JAR file created in target/"
