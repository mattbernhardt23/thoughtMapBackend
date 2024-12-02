  #!/bin/bash
CONTAINER_ID=$(docker ps -q --filter "name=thoughtmap-backend")
if [ ! -z "$CONTAINER_ID" ]; then
  echo "Stopping existing container..."
  docker stop $CONTAINER_ID
  docker rm $CONTAINER_ID
else
  echo "No existing container found. Skipping stop."
fi