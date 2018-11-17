#!/bin/bash
set -e
APP=$1
if [ -z $APP ]
  then echo "usage: $0 [app]"; exit 1
fi

SCRIPT_PATH="$( cd "$(dirname "$0")" ; pwd -P )"
pushd $SCRIPT_PATH/..

VERSION="$(git describe --tags --always --first-parent)"
echo "Git version: $VERSION"

echo "Building $APP with gradle..."
./gradlew apps:$APP:clean
./gradlew apps:$APP:assemble

echo "Copying distributions to Docker build context..."
rm -rf tools/docker/sparkling-container/dist
mkdir -p tools/docker/sparkling-container/dist
cp apps/$APP/build/distributions/*.zip tools/docker/sparkling-container/dist

echo "Building the docker container..."
cd tools/docker/sparkling-container
docker build \
  --build-arg bin=$APP \
  --build-arg zip=$(basename *.zip .zip) \
  -t sparkles/$APP:latest \
  -t sparkles/$APP:$VERSION \
  .

popd
