#!/bin/bash
set -e

GIT_URL=`node -e "console.log(require('./package.json').repository.url)"`
COMMITLINT_FROM=commitlint/from
COMMITLINT_TO=HEAD

git fetch $GIT_URL refs/heads/master:${COMMITLINT_FROM}

yarn commitlint --from ${COMMITLINT_FROM} --to ${COMMITLINT_TO}

git branch -D ${COMMITLINT_FROM}
