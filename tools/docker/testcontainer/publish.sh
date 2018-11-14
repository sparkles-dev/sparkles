#!/bin/bash
# Absolute path to this script, e.g. /home/user/bin/foo.sh
SCRIPT_PATH=$(dirname -- $(readlink -f "${BASH_SOURCE[0]}"))
pushd $SCRIPT_PATH
echo "Current working directory switched to: $(pwd)"

set -e

TAG=$1
if [ -z $TAG ]
  then echo "usage: $0 [tag]"; exit 1
fi
IMAGE=sparkles/testcontainer

docker build . -t $IMAGE:$TAG
docker tag $IMAGE:$TAG $IMAGE:latest
docker push $IMAGE:$TAG
docker push $IMAGE:latest
git tag -a "testcontainer_${TAG}" -m "published to docker"
git push --tags
