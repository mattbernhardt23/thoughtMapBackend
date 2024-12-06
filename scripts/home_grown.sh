# Connect to EC2 Instance - Accessed Remotely
ssh -i /Users/matthewbernhardt/Desktop/thoughtMap2Backend/ThoughtMapDataKey.pem ec2-user@ec2-3-19-238-206.us-east-2.compute.amazonaws.com


# Copy Spring Boot Jar File - Sent Locally
cp -o ServerAliveInterval=60 -i /Users/matthewbernhardt/Desktop/thoughtMap2Backend/ThoughtMapDataKey.pem /Users/matthewbernhardt/Desktop/thoughtMapBackend/target/backend-0.0.1-SNAPSHOT.jar ec2-user@ec2-3-19-238-206.us-east-2.compute.amazonaws.com:/home/ec2-user/

# Create Spring Boot Directory - Accessed Remotely
mkdir spring-boot-app
cd spring-boot-app

# Move Jar file - Accessed Remotely
mv ../backend-0.0.1-SNAPSHOT.jar .

# Copy Dockerfile - Sent Locally
scp -o ServerAliveInterval=60 -i /Users/matthewbernhardt/Desktop/thoughtMap2Backend/ThoughtMapDataKey.pem /Users/matthewbernhardt/Desktop/thoughtMapBackend/Dockerfile ec2-user@ec2-3-19-238-206.us-east-2.compute.amazonaws.com:/home/ec2-user/spring-boot-app/

# Build Docker Image - Accessed Remotely
docker build -t spring-boot-app .

# Run Docker Image - Accessed Remotely
docker run -p 8080:8080 spring-boot-app
