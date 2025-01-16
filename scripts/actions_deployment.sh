#!/bin/bash

# Set strict error handling
set -euo pipefail

# Variables
TARGET_DIR="/var/app/target"
LOG_FILE="/var/log/cleanup.log"
DEPLOYMENT_ROOT="/opt/codedeploy-agent/deployment-root"

# Log setup
exec > >(tee -i "$LOG_FILE") 2>&1

# Log the start of the cleanup process
echo "$(date) - Starting cleanup process..."

# Step 1: Clean target directory
echo "Cleaning up target directory: $TARGET_DIR"
if [ -d "$TARGET_DIR" ]; then
  rm -rf "$TARGET_DIR"/*
else
  echo "Target directory $TARGET_DIR does not exist. Skipping..."
fi

# Step 2: Remove stopped Docker containers
echo "Removing stopped Docker containers..."
docker container prune -f || echo "Docker container prune failed. Ensure Docker is installed and running."

# Step 3: Remove unused Docker images
echo "Removing unused Docker images..."
docker image prune -f || echo "Docker image prune failed. Ensure Docker is installed and running."

# Step 4: Remove old deployment files (older than 7 days)
echo "Removing old deployment files from $DEPLOYMENT_ROOT..."
if [ -d "$DEPLOYMENT_ROOT" ]; then
  find "$DEPLOYMENT_ROOT" -type d -mtime +7 -exec rm -rf {} + || echo "Failed to remove old deployment files."
else
  echo "Deployment root directory $DEPLOYMENT_ROOT does not exist. Skipping..."
fi

# Log the completion of the cleanup process
echo "$(date) - Cleanup completed successfully."
