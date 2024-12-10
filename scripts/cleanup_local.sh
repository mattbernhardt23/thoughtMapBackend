#!/bin/bash
PORT=8080

PID=$(sudo lsof -tiTCP:$PORT -sTCP:LISTEN)

if [ -z "$PID" ]; then
  echo "No process running on port $PORT."
else
  echo "Terminating process $PID on port $PORT..."
  sudo kill -9 $PID
  echo "Process terminated."
fi
