#!/bin/bash
set -e

# Log cleanup activity
LOG_FILE="/var/log/cleanup.log"
exec > >(tee -i $LOG_FILE)
exec 2>&1

echo "$(date) - Starting cleanup process..."

# Remove stopped Docker containers
docker container prune -f

# Remove unused Docker images
docker image prune -f

# Optional: Remove old deployment files
DEPLOYMENT_ROOT="/opt/codedeploy-agent/deployment-root/"
find $DEPLOYMENT_ROOT -type d -mtime +7 -exec rm -rf {} \;

echo "$(date) - Cleanup completed successfully."
