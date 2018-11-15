#!/bin/bash
# Absolute path to this script, e.g. /home/user/bin/foo.sh
SCRIPT_PATH=$(dirname -- $(readlink -f "${BASH_SOURCE[0]}"))
pushd $SCRIPT_PATH
echo "Current working directory switched to: $(pwd)"

set -e

ZIP=$1
if [ -z $ZIP ]
  then echo "usage: $0 [zip]"; exit 1
fi

cp $ZIP .
docker build -t sparkles/$BIN .
