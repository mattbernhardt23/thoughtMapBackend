  #!/bin/bash

echo "Stopping Docker container(s)..."

# Find and stop the container by name
CONTAINER_ID=$(docker ps -q --filter "name=thoughtmap-backend")
if [ ! -z "$CONTAINER_ID" ]; then
  echo "Stopping container with ID: $CONTAINER_ID"
  docker stop "$CONTAINER_ID"
  docker rm "$CONTAINER_ID"
else
  echo "No container named 'thoughtmap-backend' found."
fi

# Remove any orphaned Docker processes and resources
echo "Pruning unused Docker resources..."
docker container prune -f
docker network prune -f
docker image prune -f
docker volume prune -f

# Kill remaining Docker proxy processes if they persist
echo "Killing any lingering Docker proxy processes..."
DOCKER_PROXY_PIDS=$(sudo lsof -t -i:8080 -sTCP:LISTEN | xargs)
if [ ! -z "$DOCKER_PROXY_PIDS" ]; then
  sudo kill -9 $DOCKER_PROXY_PIDS
else
  echo "No lingering Docker proxy processes found."
fi

echo "Cleanup complete."
