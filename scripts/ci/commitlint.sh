#!/bin/bash
set -e

GIT_URL=`node -e "console.log(require('./package.json').repository.url)"`
COMMITLINT_TO=HEAD

if [ "${CIRCLE_BRANCH}" = "master" ] || [ -n "${CIRCLE_TAG}" ]; then
  COMMITLINT_FROM=`git log --format="format:%h" -n 1 HEAD~1`
  COMMITLINT_ARGS="--from ${COMMITLINT_FROM} --to ${COMMITLINT_TO}"
  yarn commitlint ${COMMITLINT_ARGS}
else
  COMMITLINT_FROM='commitlint/from'
  git fetch $GIT_URL refs/heads/master:${COMMITLINT_FROM}
  COMMITLINT_ARGS="--from ${COMMITLINT_FROM} --to ${COMMITLINT_TO}"
  yarn commitlint ${COMMITLINT_ARGS}
  git branch -D ${COMMITLINT_FROM}
fi
