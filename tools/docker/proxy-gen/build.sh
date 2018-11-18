#!/bin/bash
SCRIPT_PATH="$( cd "$(dirname "$0")" ; pwd -P )"
pushd $SCRIPT_PATH

docker build -t sparkles/proxy-gen .

popd
