#!/bin/bash
# Wait for the application to become available
echo "Waiting for application to start..."
for i in {1..10}; do
  curl -f http://localhost:8080/actuator/health
  if [ $? -eq 0 ]; then
    echo "Application is running."
    exit 0
  fi
  echo "Retrying health check..."
  sleep 5
done

echo "Application failed to start."
exit 1
