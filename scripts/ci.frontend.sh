#!/bin/bash
set -e

###
# On master, build all. On branches and pull requests, only build affected by change.
# https://blog.nrwl.io/nrwl-nx-6-1-better-dev-ergonomics-faster-builds-3198bb310e39
if [ "${CIRCLE_BRANCH}" = "master" ] || [ "${CIRCLE_TAG}" ]
  AFFECTED_ARGS = "--all"
else
  AFFECTED_ARGS = "--base=origin/master --head=HEAD"
fi

yarn packages:styles:build
yarn packages:components:build
yarn affected:build "{$AFFECTED_ARGS}"
xvfb-run -a yarn affected:test "{$AFFECTED_ARGS}"
