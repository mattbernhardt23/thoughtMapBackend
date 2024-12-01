#!/bin/bash
# Authenticate Docker with Amazon ECR
aws ecr get-login-password --region us-east-2 | docker login --username AWS --password-stdin 761018876037.dkr.ecr.us-east-2.amazonaws.com

# Pull the latest image
docker pull 761018876037.dkr.ecr.us-east-2.amazonaws.com/thoughtmap-backend:latest

# Run the new container
docker run -d \
  --name thoughtmap-backend \
  -p 8080:8080 \
  761018876037.dkr.ecr.us-east-2.amazonaws.com/thoughtmap-backend:latest

