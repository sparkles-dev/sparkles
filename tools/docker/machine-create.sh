#!/bin/bash
set -e
NAME=$1
if [ -z $NAME ]
  then echo "usage: $0 [name]"; exit 1
fi

docker-machine create \
  --driver "virtualbox" \
  --virtualbox-cpu-count "1" \
  --virtualbox-disk-size "20000" \
  --virtualbox-memory "2048" \
  $NAME

docker-machine scp shopy.key.crt $NAME:/
