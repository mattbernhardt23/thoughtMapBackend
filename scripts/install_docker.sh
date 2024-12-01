#!/bin/bash
# Install Docker
yum update -y
yum install -y docker

# Start Docker service
service docker start

# Enable Docker to start on boot
systemctl enable docker
