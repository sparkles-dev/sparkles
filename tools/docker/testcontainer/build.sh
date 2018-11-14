#!/bin/bash
# Absolute path to this script, e.g. /home/user/bin/foo.sh
SCRIPT_PATH=$(dirname -- $(readlink -f "${BASH_SOURCE[0]}"))
pushd $SCRIPT_PATH
echo "Current working directory switched to: $(pwd)"

docker build -t sparkles/testcontainer .
