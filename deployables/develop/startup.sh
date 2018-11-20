#!/bin/bash
SCRIPT_PATH="$( cd "$(dirname "$0")" ; pwd -P )"
COMPOSE_PROJECT_NAME=sparkles

docker network inspect $COMPOSE_PROJECT_NAME
if [ $? -gt 0 ]; then
    echo "Creating docker network $COMPOSE_PROJECT_NAME..."
    docker network create $COMPOSE_PROJECT_NAME
    echo "Created docker network."
fi

docker-compose up -d certs

docker container cp \
    $SCRIPT_PATH/../../tools/ssl/certs/. \
    certs:/etc/nginx/certs

docker-compose up -d
