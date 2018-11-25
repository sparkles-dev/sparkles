#!/bin/bash
SCRIPT_PATH="$( cd "$(dirname "$0")" ; pwd -P )"
COMPOSE_PROJECT_NAME=sparkles

docker-compose down
