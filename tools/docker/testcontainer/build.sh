#!/bin/bash
SCRIPT_PATH="$( cd "$(dirname "$0")" ; pwd -P )"
pushd $SCRIPT_PATH
echo "Current working directory switched to: $(pwd)"

docker build -t sparkles/testcontainer .
